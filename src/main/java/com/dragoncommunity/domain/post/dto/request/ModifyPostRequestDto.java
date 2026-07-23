package com.dragoncommunity.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;

import static com.dragoncommunity.domain.post.constant.ValidationMessage.CONTENT_NOT_BLANK;
import static com.dragoncommunity.domain.post.constant.ValidationMessage.TITLE_NOT_BLANK;

public record ModifyPostRequestDto (
        @NotBlank(message = TITLE_NOT_BLANK)
        String title,

        @NotBlank(message = CONTENT_NOT_BLANK)
        String content,

        String postImageUrl
){
}
