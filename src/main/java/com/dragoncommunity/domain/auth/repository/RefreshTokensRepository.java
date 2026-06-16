package com.dragoncommunity.domain.auth.repository;

import com.dragoncommunity.domain.auth.model.RefreshTokens;
import com.dragoncommunity.domain.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefreshTokensRepository extends JpaRepository<RefreshTokens,Long> {

    List<RefreshTokens> findByUserOrderByCreatedAtAsc(Users user);
}
