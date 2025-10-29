package com.LakeSide.LakeSide.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.LakeSide.LakeSide.Exception.PhotoRetrievalException;
import com.LakeSide.LakeSide.Exception.ResourceNotFoundException;
import com.LakeSide.LakeSide.model.BookedRoom;
import com.LakeSide.LakeSide.model.Room;
import com.LakeSide.LakeSide.response.bookedRoomResponse;
import com.LakeSide.LakeSide.response.roomResponse;
import com.LakeSide.LakeSide.service.IBookedRoomService;
import com.LakeSide.LakeSide.service.IRoomService;

import jakarta.transaction.Transactional;

@RequestMapping("/rooms")
//CORS policy override for diffrent paths 
@CrossOrigin(origins="*")
@RestController
public class RoomController {
	
	//DO NOT FORGET TO WIRE
	@Autowired
	private IRoomService roomService;
	
	//method that will return response entity of roomresponse
	//roomresponse is dto class 
	
	public IRoomService getRoomService() {
		return roomService;
	}

	public void setRoomService(IRoomService roomService) {
		this.roomService = roomService;
	}
	

	@PostMapping("/add/new-room")
	public ResponseEntity<roomResponse> addNewRoom(
			@RequestParam("photo") MultipartFile photo,
			@RequestParam("roomType") String roomType,
			@RequestParam("roomPrice") BigDecimal roomPrice) throws SerialException, SQLException{
		Room savedRoom=roomService.addNewRoom(photo,roomType,roomPrice);
		
		//convert to response entity
		roomResponse response=new roomResponse(savedRoom.getId(), 
				savedRoom.getRoomType(), 
				savedRoom.getRoomPrice());
		
		return ResponseEntity.ok(response);
		
	}
	
	//get all room types from database
	@GetMapping("/room-types")
	public List<String> getRoomTypes(){
		return roomService.getAllRoomTypes();
	}
	
	//fetch all rooms from database
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
	
	//function that will delete room from databases
	@DeleteMapping("/delete/room/{roomId}")
	public ResponseEntity<Void> deleteRoom(@PathVariable("roomId") Long roomId){
		roomService.deleteRoom(roomId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@Transactional
	private roomResponse getRoomResponse(Room room) {
//		List<BookedRoom> bookings=getAllBookingsById(room.getId());
//		//get booking history
//		List<bookedRoomResponse> bookingsInfo = bookings.stream().map(booking -> new
//				bookedRoomResponse(
//						booking.getBookingId(),
//						booking.getCheckInDate(),
//						booking.getCheckOutDate(), 
//						booking.getBookingConfCode())).toList();
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

//	private List<BookedRoom> getAllBookingsById(Long id) {
//		return broomService.getAllBookingsByRoomId(id);
//	}
	
	//function that will update room in Edit room view
	@PutMapping(value="/update/room/{roomId}")
	public ResponseEntity<roomResponse> updateRoom(@PathVariable Long roomId,
			@RequestParam(required=false) String roomType,
			@RequestParam(required=false) BigDecimal roomPrice,
		@RequestParam(required=false) MultipartFile photo) throws IOException, SQLException{
		
		//check if photo has picture
		byte[] photoByte=photo != null && !photo.isEmpty() ? 
				photo.getBytes() : roomService.getRoomPhotoByRoomID(roomId);
		
		//we are converting bytes to Blob  
		Blob photoBlob = photoByte != null && photoByte.length>0 ? new SerialBlob(photoByte) : null;
		Room theRoom = roomService.updateRoom(roomId,roomType,roomPrice,photoByte);
		theRoom.setPhoto(photoBlob);
		
		roomResponse RoomResponse=getRoomResponse(theRoom);
		return ResponseEntity.ok(RoomResponse);
	}
	
	//function that will generate random conf code for booking
	private String generateConfirmationCode(Room room) {
		//combine timestamp+ID+random
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        String roomCode = String.format("%03d", room.getId());
        String random = String.format("%04d", ThreadLocalRandom.current().nextInt(1000, 9999));
        
        return "BK"+timestamp+roomCode+random;
	}
	
	//function to get single room by ID to update
	@GetMapping("/room/{roomId}")
	@Transactional
	public ResponseEntity<Optional<roomResponse>> getRoomByID(@PathVariable Long roomId){
		Optional<Room> theRoom=roomService.getRoomID(roomId);
		return theRoom.map(room ->{
			roomResponse roomResponse = getRoomResponse(room);
			return ResponseEntity.ok(Optional.of(roomResponse));
			}).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
	}
	

	// DO NOT FORGET TO WIRE
	@Autowired
	private IBookedRoomService broomService;

	// method that will return response entity of roomresponse
	// roomresponse is dto class
	public IBookedRoomService getBroomService() {
		return broomService;
	}

	public void setBroomService(IBookedRoomService broomService) {
		this.broomService = broomService;
	}

	//dto function for booking a room
	@Transactional
	private bookedRoomResponse getBookedRoomResponse(BookedRoom broom) {
		//LocalDate checkInDate, LocalDate checkOutDate, String guestFullName,
        //String guestEmail, int numOfAdults, int numOfChildren,roomResponse room
		return new bookedRoomResponse(broom.getCheckInDate(),
				broom.getCheckOutDate(),
				broom.getGuestFullName(),
				broom.getGuestEmail(),
				broom.getNumOfAdults(),
				broom.getNumOfChildren(),
				broom.calculateTotalGuest(),
				getRoomResponse(broom.getRoom()));
	}
	
	//book a single room
	@PostMapping(value="/browse-rooms/booking/{roomId}")
	@Transactional
	public ResponseEntity<bookedRoomResponse> roomBooking(
			@PathVariable Long roomId,
			@RequestParam String guestFullName,
			@RequestParam String guestEmail,
			@RequestParam LocalDate checkInDate,
			@RequestParam LocalDate checkOutDate,
			@RequestParam int numOfAdults,
			@RequestParam int numOfChildren) throws IOException, SQLException{
		Room room=roomService.getRoomInfoById(roomId);
		int totalNumberOfGuests=numOfAdults+numOfChildren;
		
		//function for confCode
		String bookingCode=generateConfirmationCode(room);
		BookedRoom broom=broomService.bookRoom(checkInDate, checkOutDate, guestFullName,
				guestEmail, numOfAdults,bookingCode, numOfChildren,totalNumberOfGuests, room);
		//in case of removed booking, room should be listed as available	
		if(broom.getRoom().getId()!=null) {
			room.setBooked(true);
		}else {
			room.setBooked(false);
		}
		
		bookedRoomResponse broomResponse=getBookedRoomResponse(broom);
	return ResponseEntity.ok(broomResponse);
	}

}
