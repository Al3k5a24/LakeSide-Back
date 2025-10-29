package com.LakeSide.LakeSide.service;

import java.time.LocalDate;
import java.util.List;

import com.LakeSide.LakeSide.model.BookedRoom;
import com.LakeSide.LakeSide.model.Room;

public interface IBookedRoomService {
	
	public List<BookedRoom> getAllBookingsByRoomId();

	BookedRoom bookRoom(LocalDate checkInDate, LocalDate checkOutDate, String guestFullName, String guestEmail,
			int numOfAdults, String BookingConfCode, int numOfChildren, int totalNumberOfGuests, Room room);
	
}
