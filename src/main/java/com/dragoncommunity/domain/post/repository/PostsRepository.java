package com.dragoncommunity.domain.post.repository;

import com.dragoncommunity.domain.post.model.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {
}
