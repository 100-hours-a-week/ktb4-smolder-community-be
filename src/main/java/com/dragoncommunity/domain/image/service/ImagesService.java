package com.dragoncommunity.domain.image.service;

import com.dragoncommunity.infrastructure.storage.FileManager;
import com.dragoncommunity.domain.image.model.Images;
import com.dragoncommunity.domain.image.repository.ImagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImagesService {

    private final FileManager fileManager;
    private final ImagesRepository imagesRepository;

    /**
     * 프로필 이미지 업로드
     * 1.파일을 Storage에 저장하고
     * 2.이미지 테이블에 파일 관련 데이터 생성
     * TODO : 배치 처리로 고아 파일 및 칼럼 삭제
     */
    @Transactional
    public Images createUserProfileImage(MultipartFile file){
        String profileUrl = fileManager.profileImageUpload(file);

        return imagesRepository.save(Images.createImage(profileUrl));
    }

    /**
     * 게시글 이미지 업로드
     * 1.파일을 Storage에 저장하고
     * 2.이미지 테이블에 파일 관련 데이터 생성
     * TODO : 배치 처리로 고아 파일 및 칼럼 삭제
     */
    public Images createPostImage(MultipartFile file) {
        String postImageUrl = fileManager.postImageUpload(file);

        return imagesRepository.save(Images.createImage(postImageUrl));
    }
}
