package com.LakeSide.LakeSide.service.Room;
import java.util.List;

import com.LakeSide.LakeSide.model.BookedRoom;
import com.LakeSide.LakeSide.model.UserAccount;
import com.LakeSide.LakeSide.requests.BookRoomRBody;
import com.LakeSide.LakeSide.response.bookedRoomResponse;

public interface IBookedRoomService {
	
	public List<BookedRoom> getAllBookingsByRoomId();

	bookedRoomResponse bookRoom(Long roomID, BookRoomRBody request, UserAccount user);
	
}
