package com.LakeSide.LakeSide.service;

import java.time.LocalDate;
import java.util.List;

import com.LakeSide.LakeSide.model.BookedRoom;
import com.LakeSide.LakeSide.model.Room;

public interface IBookedRoomService {
	
	BookedRoom bookRoom(LocalDate checkInDate, LocalDate checkOutDate, String guestFullName,
            String guestEmail, int numOfAdults, int numOfChildren, int totalNumberOfGuests, Room room);
	
	public List<BookedRoom> getAllBookingsByRoomId(Long id);
	
}
