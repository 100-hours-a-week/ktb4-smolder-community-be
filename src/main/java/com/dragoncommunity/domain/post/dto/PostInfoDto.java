package com.dragoncommunity.domain.post.dto;

import com.dragoncommunity.common.util.FileUtil;

import java.time.LocalDateTime;

public record PostInfoDto(
        Long postId,
        String writer,
        String profileImageUrl,
        String title,
        Long viewCount,
        Long likeCount,
        Long commentCount,
        LocalDateTime updatedAt
) {
    public PostInfoDto(
            Long postId,
            String writer,
            String profileImageUrl,
            String title,
            Long viewCount,
            Long likeCount,
            Long commentCount,
            LocalDateTime updatedAt) {
        this.postId = postId;
        this.writer = writer;
        this.profileImageUrl = profileImageUrl != null ? FileUtil.toFullUrl(profileImageUrl) : null;
        this.title = title;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.updatedAt = updatedAt;
    }
}
