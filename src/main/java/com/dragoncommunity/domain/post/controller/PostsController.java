package com.dragoncommunity.domain.post.controller;

import com.dragoncommunity.common.dto.ApiResponse;
import com.dragoncommunity.domain.post.dto.request.CreatePostRequestDto;
import com.dragoncommunity.domain.post.service.PostsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.dragoncommunity.domain.post.constant.SuccessMessage.POST_UPLOAD_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostsController {

    private final PostsService postsService;

    /**
     * 게시글 등록 API
     * request Attribute에 있는 user Id를 추출하여 게시글을 등록한다.
     */
    @PostMapping
    public ApiResponse<Void> createPost(@Valid @RequestBody CreatePostRequestDto createPostRequestDto, HttpServletRequest request){
        postsService.createPost(createPostRequestDto, extractUserId(request));

        return ApiResponse.of(POST_UPLOAD_SUCCESS);
    }

    private Long extractUserId(HttpServletRequest request){
        return (Long) request.getAttribute("userId");
    }
}
