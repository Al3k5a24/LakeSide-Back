package com.LakeSide.LakeSide.repository;

import com.LakeSide.LakeSide.model.RoomBookings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomBookingRepository extends JpaRepository<RoomBookings,Long> {
    @Override
    public <S extends RoomBookings> S save(S entity);
}
