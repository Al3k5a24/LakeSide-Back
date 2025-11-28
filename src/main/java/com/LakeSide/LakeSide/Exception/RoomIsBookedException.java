package com.LakeSide.LakeSide.Exception;

public class RoomIsBookedException extends RuntimeException{
    public RoomIsBookedException(String message) {
        super(message);
    }
}
