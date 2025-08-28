package com.LakeSide.LakeSide.service;

import java.io.IOException;
import java.math.BigDecimal;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.LakeSide.LakeSide.model.Room;
import com.LakeSide.LakeSide.repository.RoomRepository;

import jakarta.websocket.server.ServerEndpoint;
import java.sql.Blob;
import java.sql.SQLException;

@Service
public class RoomServiceImpl implements IRoomService{
	
	private RoomRepository roomRepository;

	@Override
	public Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) throws SerialException, SQLException {
		Room room=new Room();
		room.setRoomType(roomType);
		room.setRoomPrice(roomPrice);
		
		//picture
		if(!file.isEmpty()) {
			try {
				byte[] photoByte;
				photoByte = file.getBytes();
				Blob photoBlob=new SerialBlob(photoByte);
				room.setPhoto(photoBlob);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();}
	}
		return roomRepository.save(room);
	}
}
    
