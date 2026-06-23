package com.dragoncommunity.domain.post.repository;

import com.dragoncommunity.domain.image.model.Images;
import com.dragoncommunity.domain.post.model.Posts;
import com.dragoncommunity.domain.post.model.PostsImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostsImagesRepository extends JpaRepository<PostsImages, PostsImages.PostsImagesId> {
    boolean existsByImage(Images image);

    Optional<PostsImages> findByPost(Posts post);
}
