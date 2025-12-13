package com.LakeSide.LakeSide.response;

import com.LakeSide.LakeSide.Enums.BookingStatus;
import com.LakeSide.LakeSide.model.RoomBookings;
import com.LakeSide.LakeSide.model.UserAccount;

import java.awt.print.Book;
import java.math.BigDecimal;
import java.time.LocalDate;

public class roomBookingsResponse {
    private Long id;

    private String guestFullName;

    private String guestEmail;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private int totalNumOfGuests;

    private String bookingConfirmationCode;

    private BigDecimal totalPrice;

    private String BookedRoomType;

    private BookingStatus status;


    public roomBookingsResponse(Long id, String guestFullName, String guestEmail,
                                LocalDate checkInDate, LocalDate checkOutDate, int totalNumOfGuests,
                                String bookingConfirmationCode, String bookedRoomType, BigDecimal totalPrice,
                                BookingStatus status) {
        this.id=id;
        this.guestFullName = guestFullName;
        this.guestEmail = guestEmail;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalNumOfGuests = totalNumOfGuests;
        this.bookingConfirmationCode = bookingConfirmationCode;
        this.BookedRoomType = bookedRoomType;
        this.totalPrice = totalPrice;
        this.status=status;
    }

    public String getBookedRoomType() {
        return BookedRoomType;
    }

    public void setBookedRoomType(String bookedRoomType) {
        BookedRoomType = bookedRoomType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getTotalNumOfGuests() {
        return totalNumOfGuests;
    }

    public void setTotalNumOfGuests(int totalNumOfGuests) {
        this.totalNumOfGuests = totalNumOfGuests;
    }

    public String getBookingConfirmationCode() {
        return bookingConfirmationCode;
    }

    public void setBookingConfirmationCode(String bookingConfirmationCode) {
        this.bookingConfirmationCode = bookingConfirmationCode;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}
