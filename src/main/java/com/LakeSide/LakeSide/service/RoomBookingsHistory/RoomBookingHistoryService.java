package com.LakeSide.LakeSide.service.RoomBookingsHistory;

import com.LakeSide.LakeSide.model.BookedRoom;
import com.LakeSide.LakeSide.model.RoomBookings;
import com.LakeSide.LakeSide.model.UserAccount;
import com.LakeSide.LakeSide.response.roomBookingsResponse;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.List;

public interface   RoomBookingHistoryService {

    RoomBookings saveBookingHistory (BookedRoom broom, UserAccount user);

    List<roomBookingsResponse> getAllBookingsByUser(UserAccount user);
}
