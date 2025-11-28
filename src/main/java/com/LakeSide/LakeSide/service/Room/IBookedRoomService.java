package com.LakeSide.LakeSide.service.Room;

import java.time.LocalDate;
import java.util.List;

import com.LakeSide.LakeSide.model.BookedRoom;
import com.LakeSide.LakeSide.model.Room;
import com.LakeSide.LakeSide.model.UserAccount;
import com.LakeSide.LakeSide.requests.BookRoomRBody;

public interface IBookedRoomService {
	
	public List<BookedRoom> getAllBookingsByRoomId();

	BookedRoom bookRoom(Long roomID, BookRoomRBody request, UserAccount user);
	
}
