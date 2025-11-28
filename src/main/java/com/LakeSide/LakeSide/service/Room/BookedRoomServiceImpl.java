package com.LakeSide.LakeSide.service.Room;

import java.beans.Transient;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.sql.rowset.serial.SerialBlob;

import com.LakeSide.LakeSide.Exception.RoomIsBookedException;
import com.LakeSide.LakeSide.model.UserAccount;
import com.LakeSide.LakeSide.requests.BookRoomRBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.LakeSide.LakeSide.Exception.PhotoRetrievalException;
import com.LakeSide.LakeSide.model.BookedRoom;
import com.LakeSide.LakeSide.model.Room;
import com.LakeSide.LakeSide.repository.BookedRoomRepository;

import jakarta.transaction.Transactional;

@Service
@CrossOrigin(origins="*")
public class BookedRoomServiceImpl implements IBookedRoomService{
    
	@Autowired
	private BookedRoomRepository broomrepository;

    @Autowired
    private IRoomService roomService;


    private String generateConfirmationCode(Room room) {
        //combine timestamp+ID+random = conf code
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        String roomCode = String.format("%03d", room.getId());
        String random = String.format("%04d", ThreadLocalRandom.current().nextInt(1000, 9999));
        return "BK"+timestamp+roomCode+random;
    }

	@Override
	@Transactional
	public BookedRoom bookRoom(Long roomID, BookRoomRBody request, UserAccount user) {
        Room room = roomService.getRoomInfoById(roomID);
        BookedRoom bRoom = new BookedRoom();
        if(room.isBooked()){
            throw  new RoomIsBookedException("Room has been already booked!");
        }
        bRoom.setGuestFullName(user.getEmail());
        bRoom.setGuestEmail(user.getEmail());
        bRoom.setCheckInDate(request.getCheckInDate());
        bRoom.setCheckOutDate(request.getCheckOutDate());
        bRoom.setNumOfAdults(request.getNumOfAdults());
        bRoom.setNumOfChildren(request.getNumOfChildren());
        String confCode=generateConfirmationCode(room);
        bRoom.setBookingConfCode(confCode);
        int totalNumOfGuests= request.getNumOfAdults()+ request.getNumOfChildren();
        bRoom.setTotalGuests(totalNumOfGuests);
        broomrepository.save(bRoom);
        return bRoom;
	}

	@Override
	public List<BookedRoom> getAllBookingsByRoomId() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
