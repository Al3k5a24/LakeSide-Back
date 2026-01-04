package com.LakeSide.LakeSide.repository;

import com.LakeSide.LakeSide.model.RefreshToken;
import com.LakeSide.LakeSide.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Override
    public <S extends RefreshToken> S save(S entity);

    Optional<RefreshToken> findByToken(String token);
    void deleteByEmail(String Email);

    // NEW: Delete all tokens for a user
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.user = :user")
    void deleteAllByUser(@Param("user") UserAccount user);

    // NEW: Find all active tokens for a user
    List<RefreshToken> findAllByUserAndRevokedFalse(UserAccount user);

    // NEW: Revoke token
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.token = :token")
    void revokeToken(@Param("token") String token);

    // NEW: Revoke all tokens for a user
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.user = :user")
    void revokeAllByUser(@Param("user") UserAccount user);

    // NEW: Count active tokens for a user (for rate limiting)
    long countByUserAndRevokedFalse(UserAccount user);
}
