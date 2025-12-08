package com.LakeSide.LakeSide.controller;

import com.LakeSide.LakeSide.JWT.JWTService;
import com.LakeSide.LakeSide.model.RoomBookings;
import com.LakeSide.LakeSide.model.UserAccount;
import com.LakeSide.LakeSide.service.IUserAccountService;
import com.LakeSide.LakeSide.service.RoomBookingsHistory.RoomBookingHistoryService;
import jakarta.persistence.Lob;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/my-bookings")
//CORS policy override for diffrent paths 
@CrossOrigin(origins="*")
@ComponentScan(basePackages = {"com.LakeSide.LakeSide", "com/LakeSide/LakeSide/JWT"})
@RestController
public class BookedRoomController {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private IUserAccountService userService;

    @Autowired
    private RoomBookingHistoryService bookingHistoryService;

    public BookedRoomController(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/all-booked-Rooms")
    @Lob
    @Transactional
    private ResponseEntity<Object> returnBookedRooms(
            @CookieValue(name="AUTH_TOKEN", required = false) String token){
        String userEmail=jwtService.extractEmail(token);
        UserAccount loggedUser = userService.loadUserbyEmail(userEmail);
        List<RoomBookings> bookingHistoryList = bookingHistoryService.getAllBookingsByUser(loggedUser);
        return ResponseEntity.ok(bookingHistoryList);
    }
}
