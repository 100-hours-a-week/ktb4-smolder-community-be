package com.dragoncommunity.domain.post.repository;

import com.dragoncommunity.domain.post.dto.PostInfoDto;
import com.dragoncommunity.domain.post.model.Posts;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query("SELECT new com.dragoncommunity.domain.post.dto.PostInfoDto(" +
            "       p.postId, u.nickname, p.title, s.viewCount, s.likeCount, s.commentCount, p.updatedAt) " +
            "FROM Posts p " +
            "JOIN p.user u " +
            "JOIN PostsStats s ON p.postId = s.postId " +
            "ORDER BY p.postId DESC")
    Slice<PostInfoDto> findFirstPage(Pageable pageable);

    @Query("SELECT new com.dragoncommunity.domain.post.dto.PostInfoDto(" +
            "       p.postId, u.nickname, p.title, s.viewCount, s.likeCount, s.commentCount, p.updatedAt) " +
            "FROM Posts p " +
            "JOIN p.user u " +
            "JOIN PostsStats s ON p.postId = s.postId " +
            "WHERE p.postId < :lastSeenId " +
            "ORDER BY p.postId DESC")
    Slice<PostInfoDto> findNextPage(@Param("lastSeenId") Long lastSeenId, Pageable pageable);

    Optional<Posts> findByPostId(Long postId);
}
