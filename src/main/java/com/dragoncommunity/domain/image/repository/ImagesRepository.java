package com.dragoncommunity.domain.image.repository;

import com.dragoncommunity.domain.image.model.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagesRepository extends JpaRepository<Images,Long> {
}
