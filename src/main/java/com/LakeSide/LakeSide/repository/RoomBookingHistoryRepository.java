package com.LakeSide.LakeSide.repository;

import com.LakeSide.LakeSide.model.RoomBookings;
import com.LakeSide.LakeSide.response.bookedRoomResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomBookingHistoryRepository extends JpaRepository<RoomBookings,Long> {
    @Override
    public <S extends RoomBookings> S save(S entity);

    List<bookedRoomResponse> findBookingsByUserId(Long userID);
}

