package com.LakeSide.LakeSide.service.RoomBookingsHistory;

import com.LakeSide.LakeSide.model.BookedRoom;
import com.LakeSide.LakeSide.model.RoomBookings;
import com.LakeSide.LakeSide.model.UserAccount;
import com.LakeSide.LakeSide.repository.RoomBookingHistoryRepository;
import com.LakeSide.LakeSide.response.bookedRoomResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoomBookingsHistoryServiceImpl implements RoomBookingHistoryService {

    @Autowired
    private RoomBookingHistoryRepository roomBookingHistoryRepository;

    @Override
    @Transactional
    public RoomBookings saveBookingHistory(BookedRoom broom, UserAccount user) {
        RoomBookings roomHistory = new RoomBookings();

        roomHistory.setUser(user);
        roomHistory.setGuestFullName(user.getFullName());
        roomHistory.setGuestEmail(user.getEmail());

        roomHistory.setBookedRoom(broom);
        roomHistory.setCheckInDate(broom.getCheckInDate());
        roomHistory.setCheckOutDate(broom.getCheckOutDate());

        roomHistory.setNumOfAdults(broom.getNumOfAdults());
        roomHistory.setNumOfChildren(broom.getNumOfChildren());
        roomHistory.setTotalNumOfGuests(broom.getTotalGuests());

        roomHistory.setBookingConfirmationCode(broom.getBookingConfCode());

        roomHistory.setTotalPrice(broom.getRoom().getRoomPrice());

        roomHistory.setStatusConfirmed();

        roomHistory.setCreatedAt(LocalDateTime.now());

        roomBookingHistoryRepository.save(roomHistory);
        return roomHistory;
    }

    @Override
    @Transactional
    public List<bookedRoomResponse> getAllBookingsByUser(UserAccount user) {
        List<bookedRoomResponse> listOfBookedRooms = roomBookingHistoryRepository.findBookingsByUserId(user.getId());
        return listOfBookedRooms;
    }
}
