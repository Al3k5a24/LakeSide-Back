package com.LakeSide.LakeSide.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.LakeSide.LakeSide.model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>{
    
	@Override
	public <S extends Room> S save(S entity);

	//return diffrent room types
	//baeldung
	@Query("SELECT DISTINCT roomType FROM Room")
	public List<String> findDistinctRoomTypes();
}
