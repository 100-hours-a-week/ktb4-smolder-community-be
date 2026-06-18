package com.dragoncommunity.domain.post.dto;

import java.time.LocalDateTime;

public record PostInfoDto(
        Long postId,
        String writer,
        String title,
        Long viewCount,
        Long likeCount,
        Long commentCount,
        LocalDateTime updatedAt
) {
}
