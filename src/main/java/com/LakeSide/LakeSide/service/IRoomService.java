package com.LakeSide.LakeSide.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.serial.SerialException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.LakeSide.LakeSide.model.Room;

public interface IRoomService {

	Room addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice) throws SerialException, SQLException;

	List<String> getAllRoomTypes();
}
