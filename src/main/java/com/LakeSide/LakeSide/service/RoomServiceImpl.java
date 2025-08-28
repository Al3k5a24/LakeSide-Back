package com.LakeSide.LakeSide.service;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import com.LakeSide.LakeSide.model.Room;

public class RoomServiceImpl implements IRoomService{

	@Override
	public Room addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice) {
		Room room=new Room();
		room.setRoomType(roomType);
		room.setRoomPrice(roomPrice);
		return null;
	}
    
}
