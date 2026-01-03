package com.LakeSide.LakeSide.repository;

import com.LakeSide.LakeSide.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Override
    public <S extends RefreshToken> S save(S entity);

    Optional<RefreshToken> findByToken(String token);
    void deleteByEmail(String Email);
}
