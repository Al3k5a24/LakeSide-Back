package com.LakeSide.LakeSide.service;

import java.math.BigDecimal;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialException;

import org.springframework.web.multipart.MultipartFile;

import com.LakeSide.LakeSide.model.Room;

public interface IRoomService {

	Room addNewRoom(MultipartFile photo,
			String roomType, 
			BigDecimal roomPrice) throws SerialException, SQLException;
    
}
