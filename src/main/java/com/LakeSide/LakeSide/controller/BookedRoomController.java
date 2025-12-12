package com.LakeSide.LakeSide.controller;

import com.LakeSide.LakeSide.JWT.JWTService;
import com.LakeSide.LakeSide.model.RoomBookings;
import com.LakeSide.LakeSide.model.UserAccount;
import com.LakeSide.LakeSide.service.IUserAccountService;
import com.LakeSide.LakeSide.service.RoomBookingsHistory.RoomBookingHistoryService;
import jakarta.persistence.Lob;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/my-bookings")
//CORS policy override for diffrent paths 
@CrossOrigin(origins="http://localhost:5173",allowCredentials = "true")
@RestController
public class BookedRoomController {

    @Autowired
    private IUserAccountService userService;

    @Autowired
    private RoomBookingHistoryService bookingHistoryService;

    @Autowired
    private JWTService jwtService;

    @GetMapping("/all-booked-Rooms")
    @Transactional
    public ResponseEntity<Object> returnBookedRooms(
            @CookieValue(name="AUTH_TOKEN", required = false) String token){
        String userEmail=jwtService.extractEmail(token);
        UserAccount loggedUser = userService.loadUserbyEmail(userEmail);
        System.out.println(loggedUser);
        List<RoomBookings> bookingHistoryList = bookingHistoryService.getAllBookingsByUser(loggedUser);

        return ResponseEntity.ok(bookingHistoryList);
    }
}
