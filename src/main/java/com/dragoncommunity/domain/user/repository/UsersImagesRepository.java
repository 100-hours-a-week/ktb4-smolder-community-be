package com.dragoncommunity.domain.user.repository;

import com.dragoncommunity.domain.user.model.Users;
import com.dragoncommunity.domain.user.model.UsersImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersImagesRepository extends JpaRepository<UsersImages, UsersImages.UsersImagesId> {
    Optional<UsersImages> findByUser(Users user);
}
