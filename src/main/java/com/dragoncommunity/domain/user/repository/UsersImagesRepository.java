package com.dragoncommunity.domain.user.repository;

import com.dragoncommunity.domain.user.model.UsersImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersImagesRepository extends JpaRepository<UsersImages, UsersImages.UsersImagesId> {
}
