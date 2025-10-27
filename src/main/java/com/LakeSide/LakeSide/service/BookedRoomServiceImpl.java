package com.LakeSide.LakeSide.service;

import java.beans.Transient;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.LakeSide.LakeSide.model.BookedRoom;
import com.LakeSide.LakeSide.model.Room;
import com.LakeSide.LakeSide.repository.BookedRoomRepository;

import jakarta.transaction.Transactional;

@Service
@CrossOrigin(origins="*")
public class BookedRoomServiceImpl implements IBookedRoomService{
    
	@Autowired
	private BookedRoomRepository broomrepository;

	public BookedRoomRepository getBroomrepository() {
		return broomrepository;
	}

	@Override
	@Transactional
	public BookedRoom bookRoom(LocalDate checkInDate, LocalDate checkOutDate, String guestFullName, String guestEmail,
			int numOfAdults, int numOfChildren,int totalNumberOfGuests ,Room room) {
		BookedRoom BRoom = new BookedRoom();
		BRoom.setCheckInDate(checkInDate);
		BRoom.setCheckOutDate(checkOutDate);
		BRoom.setGuestFullName(guestFullName);
		BRoom.setGuestEmail(guestEmail);
		BRoom.setNumOfAdults(numOfAdults);
		BRoom.setNumOfChildren(numOfChildren);
		BRoom.calculateTotalGuest();
		BRoom.setRoom(room);
		
		broomrepository.save(BRoom);
		return BRoom;
	}
	
	public List<BookedRoom> getAllBookingsByRoomId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
