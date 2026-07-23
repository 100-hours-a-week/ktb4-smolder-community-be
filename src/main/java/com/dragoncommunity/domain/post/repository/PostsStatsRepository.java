package com.dragoncommunity.domain.post.repository;

import com.dragoncommunity.domain.post.model.Posts;
import com.dragoncommunity.domain.post.model.PostsStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsStatsRepository extends JpaRepository<PostsStats, Posts> {
}
