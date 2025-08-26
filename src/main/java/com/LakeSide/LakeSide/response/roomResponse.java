package com.LakeSide.LakeSide.response;

import java.math.BigDecimal;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;

import lombok.Data;

//Response DTOs
//we can choose what data should be sent and seen by guest
public class roomResponse {
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private boolean isBooked=false;
    private String photo;

    private List<bookedRoomResponse> bookings;

    public roomResponse(Long id, String roomType, BigDecimal roomPrice) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
    }

    public roomResponse(Long id, String roomType, BigDecimal roomPrice, boolean isBooked, byte[] photoByte,
            List<bookedRoomResponse> bookings) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.isBooked = isBooked;
        //converting image from string to base64 for easier load, display and transport
        this.photo = photoByte != null ? Base64.encodeBase64String(photoByte) : null;
        this.bookings = bookings;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public BigDecimal getRoomPrice() {
		return roomPrice;
	}

	public void setRoomPrice(BigDecimal roomPrice) {
		this.roomPrice = roomPrice;
	}

	public boolean isBooked() {
		return isBooked;
	}

	public void setBooked(boolean isBooked) {
		this.isBooked = isBooked;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public List<bookedRoomResponse> getBookings() {
		return bookings;
	}

	public void setBookings(List<bookedRoomResponse> bookings) {
		this.bookings = bookings;
	}   
    
    
}
