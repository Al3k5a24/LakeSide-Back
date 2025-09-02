package com.LakeSide.LakeSide.controller;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.LakeSide.LakeSide.model.Room;
import com.LakeSide.LakeSide.response.roomResponse;
import com.LakeSide.LakeSide.service.IRoomService;

@RequestMapping("/rooms")
//CORS policy override for diffrent paths 
@CrossOrigin
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
	
	@GetMapping("/rooms/room-types")
	public List<String> getRoomTypes(){
		return roomService.getAllRoomTypes();
	}
}
