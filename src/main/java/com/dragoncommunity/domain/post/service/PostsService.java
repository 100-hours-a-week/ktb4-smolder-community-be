package com.dragoncommunity.domain.post.service;

import com.dragoncommunity.common.exception.ApplicationException;
import com.dragoncommunity.common.util.FileUtil;
import com.dragoncommunity.domain.image.model.Images;
import com.dragoncommunity.domain.image.repository.ImagesRepository;
import com.dragoncommunity.domain.post.dto.request.CreatePostRequestDto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static com.dragoncommunity.common.exception.enums.ApplicationErrorCode.*;

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

}
