package com.dragoncommunity.domain.post.service;

import com.dragoncommunity.common.exception.ApplicationException;
import com.dragoncommunity.common.util.FileUtil;
import com.dragoncommunity.domain.image.model.Images;
import com.dragoncommunity.domain.image.repository.ImagesRepository;
import com.dragoncommunity.domain.post.dto.PostInfoDto;
import com.dragoncommunity.domain.post.dto.request.CreatePostRequestDto;
import com.dragoncommunity.domain.post.dto.request.GetPostsRequestDto;
import com.dragoncommunity.domain.post.dto.response.GetPostResponseDto;
import com.dragoncommunity.domain.post.model.Posts;
import com.dragoncommunity.domain.post.model.PostsImages;
import com.dragoncommunity.domain.post.model.PostsStats;
import com.dragoncommunity.domain.post.repository.PostsImagesRepository;
import com.dragoncommunity.domain.post.repository.PostsRepository;
import com.dragoncommunity.domain.post.repository.PostsStatsRepository;
import com.dragoncommunity.domain.user.model.Users;
import com.dragoncommunity.domain.user.repository.UsersRepository;
import com.dragoncommunity.infrastructure.storage.FileManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static com.dragoncommunity.common.exception.enums.ApplicationErrorCode.*;
import static com.dragoncommunity.domain.post.constant.PostConstant.DEFAULT_POST_GET_SIZE;

@Service
@RequiredArgsConstructor
@Validated
public class PostsService {

    private final PostsRepository postsRepository;
    private final UsersRepository usersRepository;
    private final PostsImagesRepository postsImagesRepository;
    private final PostsStatsRepository postsStatsRepository;
    private final ImagesRepository imagesRepository;
    private final FileManager fileManager;

    /**
     * 게시글 등록 서비스
     * 1. 유저 ID로 유저 엔티티 가져온다.
     * 2. 게시글 등록 시 첨부파일 Url이 있을 경우, 파일을 검증한다.
     * 3. 게시글을 저장한다
     * 4. 게시글 이미지가 있다면 저장한다
     * 5. 게시글 통계 정보를 저장한다.
     */
    @Transactional
    public void createPost(@Valid CreatePostRequestDto createPostRequestDto, Long userId){
        Images image = null;

        Users user = usersRepository.findByUserId(userId)
                .orElseThrow(()-> new ApplicationException(USER_NOT_EXIST));

        if (createPostRequestDto.postImageUrl() != null  && !createPostRequestDto.postImageUrl().isEmpty()) {
            String postImagePath = FileUtil.extractPathFromUrl(createPostRequestDto.postImageUrl());

            fileManager.validatePostImageExists(postImagePath);

            image = imagesRepository.findByImageUrl(postImagePath)
                    .orElseThrow(() -> new ApplicationException(PROFILE_IMAGE_NOT_FOUND));

            if (postsImagesRepository.existsByImage(image)) {
                throw new ApplicationException(FILE_UPLOAD_FAILED);
            }
        }

        Posts post =Posts.createPost(
                createPostRequestDto.title(),
                createPostRequestDto.content(),
                user);

        postsRepository.save(post);

        if(image !=null){
            postsImagesRepository.save(PostsImages.createPostsImages(post,image));
        }

        postsStatsRepository.save(PostsStats.createPostsStats(post));
    }

    /**
     * 게시글 조회 서비스
     * 마지막 본 게시글의 ID 이후 10개의 게시글을 조회한다.
     * 1. 조회할 게시글의 버퍼 개수 설정
     * 2. 조회 쿼리 실행
     * 2-1. 쿼리파라미터가 없다면 가장 최근 게시글 10개 조회
     * 2-2. 쿼리파라미터가 있다면 해당 ID 이후 10개 조회
     */
    public GetPostResponseDto getPosts(GetPostsRequestDto getPostsRequestDto){
        Pageable pageable = PageRequest.of(0, DEFAULT_POST_GET_SIZE);

        Slice<PostInfoDto> sliceResult;

        if (getPostsRequestDto.lastSeenId() == null) {
            sliceResult = postsRepository.findFirstPage(pageable);
        } else {
            sliceResult = postsRepository.findNextPage(getPostsRequestDto.lastSeenId(), pageable);
        }

        List<PostInfoDto> contents = sliceResult.getContent();
        boolean hasNext = sliceResult.hasNext();

        Long nextCursorId = contents.isEmpty() ? null : contents.get(contents.size() - 1).postId();

        return GetPostResponseDto.of(contents, nextCursorId, hasNext);
    }

}
