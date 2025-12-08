package com.LakeSide.LakeSide.service.RoomBookingsHistory;

import com.LakeSide.LakeSide.model.BookedRoom;
import com.LakeSide.LakeSide.model.RoomBookings;
import com.LakeSide.LakeSide.model.UserAccount;

public interface RoomBookingHistoryService {

    public RoomBookings saveBookingHistory (BookedRoom broom, UserAccount user);
}
