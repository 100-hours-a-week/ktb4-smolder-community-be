package com.dragoncommunity.domain.post.controller;

import com.dragoncommunity.common.dto.ApiResponse;
import com.dragoncommunity.domain.post.dto.request.CreatePostRequestDto;
import com.dragoncommunity.domain.post.dto.request.GetPostsRequestDto;
import com.dragoncommunity.domain.post.dto.response.GetPostResponseDto;
import com.dragoncommunity.domain.post.service.PostsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.dragoncommunity.domain.post.constant.SuccessMessage.POSTS_LOAD_SUCCESS;
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

    /**
     * 게시글 조회  API
     * query parameter에 있는 lastSeenId부터 게시글을 10개 조회한다.
     */
    @GetMapping
    public ApiResponse<GetPostResponseDto> getPosts(@ModelAttribute GetPostsRequestDto getPostsRequestDto){
        return ApiResponse.of(POSTS_LOAD_SUCCESS,postsService.getPosts(getPostsRequestDto));
    }

    private Long extractUserId(HttpServletRequest request){
        return (Long) request.getAttribute("userId");
    }
}
