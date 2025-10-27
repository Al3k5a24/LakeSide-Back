package com.LakeSide.LakeSide.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.LakeSide.LakeSide.model.BookedRoom;

@Repository
public interface BookedRoomRepository extends JpaRepository<BookedRoom, Long>{
	
	@Override
	public <S extends BookedRoom> S save(S entity);
}
