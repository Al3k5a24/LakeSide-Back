package com.LakeSide.LakeSide.service;

import java.io.IOException;
import java.math.BigDecimal;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.LakeSide.LakeSide.model.Room;
import com.LakeSide.LakeSide.repository.RoomRepository;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

@Service
public class RoomServiceImpl implements IRoomService{
	
	//DO NOT FORGET TO WIRE
	@Autowired
	private RoomRepository roomRepository;

	public RoomRepository getRoomRepository() {
		return roomRepository;
	}

	public void setRoomRepository(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	public Room addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice) throws SerialException, SQLException {
		Room room=new Room();
		room.setRoomType(roomType);
		room.setRoomPrice(roomPrice);
		
		//picture
		if(!photo.isEmpty()) {
			try {
				byte[] photoByte;
				photoByte = photo.getBytes();
				Blob photoBlob=new SerialBlob(photoByte);
				room.setPhoto(photoBlob);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();}
	}
		return roomRepository.save(room);
	}

	@Override
	public List<String> getAllRoomTypes() {
		// TODO Auto-generated method stub
		return null;
	}
}
    
