package com.LakeSide.LakeSide.service.RoomBookingsHistory;

import com.LakeSide.LakeSide.model.BookedRoom;
import com.LakeSide.LakeSide.model.RoomBookings;
import com.LakeSide.LakeSide.model.UserAccount;
import com.LakeSide.LakeSide.repository.RoomBookingHistoryRepository;
import com.LakeSide.LakeSide.response.roomBookingsResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

        roomHistory.setCheckInDate(broom.getCheckInDate());
        roomHistory.setCheckOutDate(broom.getCheckOutDate());

        roomHistory.setNumOfAdults(broom.getNumOfAdults());
        roomHistory.setNumOfChildren(broom.getNumOfChildren());
        roomHistory.setTotalNumOfGuests(broom.getTotalGuests());

        roomHistory.setBookingConfirmationCode(broom.getBookingConfCode());

        roomHistory.setTotalPrice(broom.getRoom().getRoomPrice());

        roomHistory.setStatusConfirmed();

        roomHistory.setCreatedAt(LocalDateTime.now());

        roomHistory.setBookedRoomType(broom.getRoom().getRoomType());

        roomBookingHistoryRepository.save(roomHistory);
        return roomHistory;
    }

    @Override
    @Transactional
    public List<roomBookingsResponse> getAllBookingsByUser(UserAccount user) {
        List<RoomBookings> listOfBookedRooms = roomBookingHistoryRepository.findBookingsByUserId(user.getId());
        List<roomBookingsResponse> bookedRoomsList = new ArrayList<>();
        for(RoomBookings room:listOfBookedRooms) {
            roomBookingsResponse rr= getBookedRoomHistoryResponse(room);
            bookedRoomsList.add(rr);
        }
        return bookedRoomsList;
    }

    @Transactional
    private roomBookingsResponse getBookedRoomHistoryResponse(RoomBookings bookedRoom) {
        return new roomBookingsResponse(
                bookedRoom.getId(),
                bookedRoom.getGuestFullName(),
                bookedRoom.getGuestEmail(),
                bookedRoom.getCheckInDate(),
                bookedRoom.getCheckOutDate(),
                bookedRoom.getTotalNumOfGuests(),
                bookedRoom.getBookingConfirmationCode(),
                bookedRoom.getBookedRoomType(),
                bookedRoom.getTotalPrice(),
                bookedRoom.getStatus()
        );
    }
}
