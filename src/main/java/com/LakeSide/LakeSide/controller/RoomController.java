package com.LakeSide.LakeSide.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import com.LakeSide.LakeSide.JWT.JWTService;
import com.LakeSide.LakeSide.model.UserAccount;
import com.LakeSide.LakeSide.requests.BookRoomRBody;
import com.LakeSide.LakeSide.service.IUserAccountService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.LakeSide.LakeSide.Exception.PhotoRetrievalException;
import com.LakeSide.LakeSide.Exception.ResourceNotFoundException;
import com.LakeSide.LakeSide.model.Room;
import com.LakeSide.LakeSide.response.bookedRoomResponse;
import com.LakeSide.LakeSide.response.roomResponse;
import com.LakeSide.LakeSide.service.Room.IBookedRoomService;
import com.LakeSide.LakeSide.service.Room.IRoomService;

import jakarta.transaction.Transactional;

@RequestMapping("/rooms")
//CORS policy override for diffrent paths 
@CrossOrigin(origins="http://localhost:5173",allowCredentials = "true")
@RestController
public class RoomController {

	@Autowired
	private IRoomService roomService;

    @Autowired
    private IBookedRoomService broomService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private IUserAccountService userService;

	@PostMapping("/add/new-room")
	public ResponseEntity<roomResponse> addNewRoom(
			@RequestParam("photo") MultipartFile photo,
			@RequestParam("roomType") String roomType,
			@RequestParam("roomPrice") BigDecimal roomPrice) throws SerialException, SQLException{
		roomResponse savedRoom=roomService.addNewRoom(photo,roomType,roomPrice);
		return ResponseEntity.ok(savedRoom);
		
	}

	@GetMapping("/room-types")
	public List<String> getRoomTypes(){
        return roomService.getAllRoomTypes();
	}

	@GetMapping("/all-rooms")
	@Transactional
	public ResponseEntity<List<roomResponse>> getAllRooms() throws SQLException{
		List<Room> rooms=roomService.getAllRooms();
		List<roomResponse> roomResponse = new ArrayList<roomResponse>();
		
		//for every room we will get it from database and post it on frontend
		for(Room room:rooms) {
			if(!room.isBooked()) {
			byte[] photoBytes=roomService.getRoomPhotoByRoomID(room.getId());
			if(photoBytes!=null && photoBytes.length>0) {
				String base64Photo=Base64.encodeBase64String(photoBytes);
				roomResponse rr=getRoomResponse(room);
				rr.setPhoto(base64Photo);
				roomResponse.add(rr);
			}
			}
		}
		return ResponseEntity.ok(roomResponse);
	}

	@DeleteMapping("/delete/room/{roomId}")
	public ResponseEntity<Void> deleteRoom(@PathVariable("roomId") Long roomId){
		roomService.deleteRoom(roomId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@Transactional
	private roomResponse getRoomResponse(Room room) {
		byte[] photoByte=null;
		Blob photoBlob = room.getPhoto();
		
		//convert photo so we can display it
		if(photoBlob != null) {
			try {
				photoByte=photoBlob.getBytes(1, (int)photoBlob.length());
			} catch (SQLException e) {
				throw new PhotoRetrievalException("Error retrieving photo");
			}
		}
		return new roomResponse(room.getId(),
				room.getRoomType(),
				room.getRoomPrice(),
				room.isBooked(),photoByte);
	}

	@PutMapping(value="/update/room/{roomId}")
	public ResponseEntity<roomResponse> updateRoom(@PathVariable Long roomId,
			@RequestParam(required=false) String roomType,
			@RequestParam(required=false) BigDecimal roomPrice,
		@RequestParam(required=false) MultipartFile photo) throws IOException, SQLException{

		byte[] photoByte=photo != null && !photo.isEmpty() ? 
				photo.getBytes() : roomService.getRoomPhotoByRoomID(roomId);

		Blob photoBlob = photoByte != null && photoByte.length>0 ? new SerialBlob(photoByte) : null;
		Room theRoom = roomService.updateRoom(roomId,roomType,roomPrice,photoByte);
		theRoom.setPhoto(photoBlob);
		
		roomResponse RoomResponse=getRoomResponse(theRoom);
		return ResponseEntity.ok(RoomResponse);
	}

	@GetMapping("/room/{roomId}")
	@Transactional
	public ResponseEntity<Optional<roomResponse>> getSingleRoomByID(@PathVariable Long roomId){
		Optional<Room> theRoom=roomService.getRoomID(roomId);
		return theRoom.map(room ->{
			roomResponse roomResponse = getRoomResponse(room);
			return ResponseEntity.ok(Optional.of(roomResponse));
			}).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
	}

    //modelattribute added for room picture
	@PostMapping(value="/browse-rooms/booking/{roomId}")
	@Transactional
	public ResponseEntity<bookedRoomResponse> bookARoom(
            @PathVariable Long roomId,
            @RequestBody @ModelAttribute BookRoomRBody body,
            @CookieValue(name="AUTH_TOKEN", required = false)
            String token) throws IOException, SQLException{
        String email = jwtService.extractEmail(token);
        UserAccount loggedUser = userService.loadUserbyEmail(email);
        bookedRoomResponse response = broomService.bookRoom(roomId,body,loggedUser);
	return ResponseEntity.ok(response);
	}

}
