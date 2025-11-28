package com.LakeSide.LakeSide.response;

import java.time.LocalDate;

import com.LakeSide.LakeSide.model.Room;
import lombok.Data;

//Response DTO
//we can choose what data should be sent and seen by guest
public class bookedRoomResponse {

    private Long bookingId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private String guestFullName;

    private String guestEmail;
    
    private int NumOfAdults;
    
    private int NumOfChildren;
    
    private int totalGuests;
    
    private String bookingConfCode;

    private roomResponse room;

    public bookedRoomResponse(Long bookingId, LocalDate checkInDate, LocalDate checkOutDate, String bookingConfCode) {
        this.bookingId = bookingId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingConfCode = bookingConfCode;
    }

    public bookedRoomResponse(Long bookingId, LocalDate checkInDate, LocalDate checkOutDate, String guestFullName,
            String guestEmail, int numOfAdults, int numOfChildren, int totalGuests,
            roomResponse room) {
        this.bookingId = bookingId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.guestFullName = guestFullName;
        this.guestEmail = guestEmail;
        NumOfAdults = numOfAdults;
        NumOfChildren = numOfChildren;
        this.totalGuests = totalGuests;
        this.room = room;
    }
    
    public bookedRoomResponse( String guestFullName,
                               String guestEmail,
                               LocalDate checkInDate,
                               LocalDate checkOutDate,
                               int numOfAdults,
                               int numOfChildren,
                               String bookingConfCode) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.guestFullName = guestFullName;
        this.guestEmail = guestEmail;
        this.NumOfAdults = numOfAdults;
        this.NumOfChildren = numOfChildren;
        this.bookingConfCode=bookingConfCode;
    }

    public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	public LocalDate getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(LocalDate checkInDate) {
		this.checkInDate = checkInDate;
	}

	public LocalDate getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(LocalDate checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public String getGuestFullName() {
		return guestFullName;
	}

	public void setGuestFullName(String guestFullName) {
		this.guestFullName = guestFullName;
	}

	public String getGuestEmail() {
		return guestEmail;
	}

	public void setGuestEmail(String guestEmail) {
		this.guestEmail = guestEmail;
	}

	public int getNumOfAdults() {
		return NumOfAdults;
	}

	public void setNumOfAdults(int numOfAdults) {
		NumOfAdults = numOfAdults;
	}

	public int getNumOfChildren() {
		return NumOfChildren;
	}

	public void setNumOfChildren(int numOfChildren) {
		NumOfChildren = numOfChildren;
	}

	public int getTotalGuests() {
		return totalGuests;
	}

	public void setTotalGuests(int totalGuests) {
		this.totalGuests = totalGuests;
	}

	public String getBookingConfCode() {
		return bookingConfCode;
	}

	public void setBookingConfCode(String bookingConfCode) {
		this.bookingConfCode = bookingConfCode;
	}

	public roomResponse getRoom() {
		return room;
	}

	public void setRoom(roomResponse room) {
		this.room = room;
	}
    
    
}
