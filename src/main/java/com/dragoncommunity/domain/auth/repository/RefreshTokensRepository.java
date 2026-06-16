package com.dragoncommunity.domain.auth.repository;

import com.dragoncommunity.domain.auth.model.RefreshTokens;
import com.dragoncommunity.domain.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokensRepository extends JpaRepository<RefreshTokens,Long> {

    List<RefreshTokens> findByUserOrderByCreatedAtAsc(Users user);

    void deleteByDeviceId(String deviceId);

    Optional<RefreshTokens> findByDeviceId(String deviceId);

    RefreshTokens findByRefreshToken(String refreshToken);

    void deleteByRefreshToken(String refreshToken);
}
