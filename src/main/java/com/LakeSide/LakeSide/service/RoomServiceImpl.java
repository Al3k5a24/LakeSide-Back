package com.LakeSide.LakeSide.service;

import java.io.IOException;
import java.math.BigDecimal;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import com.LakeSide.LakeSide.Exception.InternalServerExeption;
import com.LakeSide.LakeSide.Exception.ResourceNotFoundException;
import com.LakeSide.LakeSide.model.Room;
import com.LakeSide.LakeSide.repository.RoomRepository;

import jakarta.transaction.Transactional;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@CrossOrigin(origins="*")
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
		if(!photo.isEmpty() && roomPrice!=null) {
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
		return roomRepository.findDistinctRoomTypes();
	}

	@Override
	public List<Room> getAllRooms() {
		return roomRepository.findAll();
	}

	@Override
	@Transactional //for LOBs (pictures)
	public byte[] getRoomPhotoByRoomID(Long roomId) throws SQLException {
		//if room exists,return 
		Optional<Room> theRoom=roomRepository.findById(roomId);
		if(theRoom.isEmpty()) {
			throw new ResourceNotFoundException("Sorry, Room was not found!");
		}
		Blob photoBlob=theRoom.get().getPhoto();
		if(photoBlob != null) {
			return photoBlob.getBytes(1, (int) photoBlob.length());
		}

		return null;
	}

	@Override
	public void deleteRoom(Long roomId) {
		Optional<Room> theRoom = roomRepository.findById(roomId);
		if(theRoom.isPresent()) {
			roomRepository.deleteById(roomId);
		}	
		}

	@Override
	@Transactional //for LOBs (pictures)
	public Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoByte) {
		Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room not found!"));
		if(roomType!=null) room.setRoomType(roomType);
		if(roomPrice!=null) room.setRoomPrice(roomPrice);
		if(photoByte!=null && photoByte.length>0) {
			try {
				room.setPhoto(new SerialBlob(photoByte));
			} catch (SQLException ex) {
				throw new InternalServerExeption("Error updating room");
			}
		}
		return roomRepository.save(room);
	}

	@Override
	public Optional<Room> getRoomID(Long roomId) {
		return Optional.of(roomRepository.findById(roomId).get());
	}
}
    
