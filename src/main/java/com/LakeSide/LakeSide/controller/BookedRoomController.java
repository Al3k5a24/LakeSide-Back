package com.LakeSide.LakeSide.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/my-bookings")
//CORS policy override for diffrent paths 
@CrossOrigin(origins="*")
@RestController
public class BookedRoomController {

    
}
