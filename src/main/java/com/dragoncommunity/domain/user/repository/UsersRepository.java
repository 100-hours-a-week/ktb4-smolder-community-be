package com.dragoncommunity.domain.user.repository;

import com.dragoncommunity.domain.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<Users> findByEmail(String email);

    Optional<Users> findByUserId(Long userId);
}
