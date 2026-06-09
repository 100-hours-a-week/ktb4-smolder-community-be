package com.dragoncommunity.domain.user.repository;

import com.dragoncommunity.domain.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
