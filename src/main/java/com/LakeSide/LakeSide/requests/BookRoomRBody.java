package com.LakeSide.LakeSide.requests;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class BookRoomRBody {
    @NotNull
    @FutureOrPresent(message = "Check-in date must be today or in the future")
    private LocalDate checkInDate;

    @NotNull
    @Future(message = "Check-out date must be in the future")
    private LocalDate checkOutDate;

    @Min(value = 1, message = "At least one adult required")
    private int numOfAdults;

    @Min(value = 0, message = "Number of children cannot be negative")
    private int numOfChildren;

    @AssertTrue(message = "Check-out must be after check-in")
    public boolean isValidDateRange() {
        return checkOutDate.isAfter(checkInDate);
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public int getNumOfAdults() {
        return numOfAdults;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public int getNumOfChildren() {
        return numOfChildren;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void setNumOfAdults(int numOfAdults) {
        this.numOfAdults = numOfAdults;
    }

    public void setNumOfChildren(int numOfChildren) {
        this.numOfChildren = numOfChildren;
    }
}
