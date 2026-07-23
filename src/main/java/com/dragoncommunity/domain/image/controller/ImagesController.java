package com.dragoncommunity.domain.image.controller;

import com.dragoncommunity.common.dto.ApiResponse;
import com.dragoncommunity.common.exception.ApplicationException;
import com.dragoncommunity.domain.image.dto.response.PostImageUploadResponseDto;
import com.dragoncommunity.domain.image.dto.response.ProfileUploadResponseDto;
import com.dragoncommunity.domain.image.service.ImagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.dragoncommunity.common.config.constant.FileConfigConstant.ALLOWED_IMAGE_EXTENSIONS;
import static com.dragoncommunity.common.config.constant.FileConfigConstant.MAX_PROFILE_SIZE;
import static com.dragoncommunity.common.exception.enums.ApplicationErrorCode.FILE_UPLOAD_FAILED;
import static com.dragoncommunity.common.exception.enums.GlobalErrorCode.UNSUPPORTED_MEDIA_TYPE;
import static com.dragoncommunity.domain.image.constant.SuccessMessage.FILE_UPLOAD_SUCCESS;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImagesController {

    private final ImagesService imagesService;

    @PostMapping("/post")
    public ResponseEntity<ApiResponse<PostImageUploadResponseDto>> createPostImage(@RequestParam("file") MultipartFile file) {
        imageAvailabilityCheck(file);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of(
                    FILE_UPLOAD_SUCCESS,
                    PostImageUploadResponseDto.from(imagesService.createPostImage(file)
                    )));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProfileUploadResponseDto>> createProfileImage(@RequestParam("file") MultipartFile file) {
        imageAvailabilityCheck(file);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of(
                        FILE_UPLOAD_SUCCESS,
                        ProfileUploadResponseDto.from(imagesService.createUserProfileImage(file)
                        )));
    }

    /**
     * 이미지 유효성 검사
     * 1. 이름 검사
     * 2. 파일 확장자 검사
     * 3. 파일 크기 검사
     * TODO : 추후 커스텀 어노테이션 정의하여 유효성 검사하는 방법으로 리팩토링
     */
    private void imageAvailabilityCheck(MultipartFile file){
        if (file.getSize() >= MAX_PROFILE_SIZE) {
            throw new ApplicationException(FILE_UPLOAD_FAILED);
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.isBlank()) {
            throw new ApplicationException(FILE_UPLOAD_FAILED);
        }

        String extension = StringUtils.getFilenameExtension(originalName);
        if (extension == null || !ALLOWED_IMAGE_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new ApplicationException(UNSUPPORTED_MEDIA_TYPE);
        }
    }
}
