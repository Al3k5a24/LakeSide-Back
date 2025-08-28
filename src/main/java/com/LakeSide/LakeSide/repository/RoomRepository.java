package com.LakeSide.LakeSide.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.LakeSide.LakeSide.model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>{
    
	@Override
	public <S extends Room> S save(S entity);
}
