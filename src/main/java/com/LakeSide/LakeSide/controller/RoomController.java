package com.LakeSide.LakeSide.controller;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialException;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.LakeSide.LakeSide.Exception.PhotoRetrievalException;
import com.LakeSide.LakeSide.model.BookedRoom;
import com.LakeSide.LakeSide.model.Room;
import com.LakeSide.LakeSide.response.bookedRoomResponse;
import com.LakeSide.LakeSide.response.roomResponse;
import com.LakeSide.LakeSide.service.BookingService;
import com.LakeSide.LakeSide.service.IRoomService;

@RequestMapping("/rooms")
//CORS policy override for diffrent paths 
@CrossOrigin
@RestController
public class RoomController {
	
	//DO NOT FORGET TO WIRE
	@Autowired
	private IRoomService roomService;
	
	@Autowired
	private BookingService bookingService;
    
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
	public ResponseEntity<List<roomResponse>> getAllRooms() throws SQLException{
		List<Room> rooms=roomService.getAllRooms();
		List<roomResponse> roomResponse = new ArrayList<roomResponse>();
		
		//for every room we will get it from database and post it on frontend
		for(Room room:rooms) {
			byte[] photoBytes=roomService.getRoomPhotoByRoomID(room.getId());
			if(photoBytes!=null && photoBytes.length>0) {
				String base64Photo=Base64.encodeBase64String(photoBytes);
				roomResponse rr=getRoomResponse(room);
				rr.setPhoto(base64Photo);
				roomResponse.add((com.LakeSide.LakeSide.response.roomResponse) roomResponse);
			}
		}
		return ResponseEntity.ok(roomResponse);
	}

	private roomResponse getRoomResponse(Room room) {
		List<BookedRoom> bookings=getAllBookingsById(room.getId());
		
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

	private List<BookedRoom> getAllBookingsById(Long id) {
		return bookingService.getAllBookingsByRoomId(id);
	}
}
