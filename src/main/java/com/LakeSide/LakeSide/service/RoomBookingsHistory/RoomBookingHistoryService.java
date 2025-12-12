package com.LakeSide.LakeSide.service.RoomBookingsHistory;

import com.LakeSide.LakeSide.model.BookedRoom;
import com.LakeSide.LakeSide.model.RoomBookings;
import com.LakeSide.LakeSide.model.UserAccount;
import com.LakeSide.LakeSide.response.bookedRoomResponse;

import java.util.List;
import java.util.Optional;

public interface RoomBookingHistoryService {

    public RoomBookings saveBookingHistory (BookedRoom broom, UserAccount user);

    List<bookedRoomResponse> getAllBookingsByUser(UserAccount user);
}
