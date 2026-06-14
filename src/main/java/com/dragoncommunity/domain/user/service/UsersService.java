package com.dragoncommunity.domain.user.service;

import com.dragoncommunity.common.exception.ApplicationException;
import com.dragoncommunity.common.security.util.PasswordEncoder;
import com.dragoncommunity.common.util.FileUtil;
import com.dragoncommunity.domain.image.model.Images;
import com.dragoncommunity.domain.image.repository.ImagesRepository;
import com.dragoncommunity.domain.user.dto.request.EmailAvailabilityRequestDto;
import com.dragoncommunity.domain.user.dto.request.NicknameAvailabilityRequestDto;
import com.dragoncommunity.domain.user.dto.request.SignUpRequestDto;
import com.dragoncommunity.domain.user.model.Users;
import com.dragoncommunity.domain.user.model.UsersImages;
import com.dragoncommunity.domain.user.repository.UsersImagesRepository;
import com.dragoncommunity.domain.user.repository.UsersRepository;
import com.dragoncommunity.infrastructure.storage.FileManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;


import static com.dragoncommunity.common.exception.enums.ApplicationErrorCode.*;

@Service
@Validated
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileManager fileManager;
    private final ImagesRepository imagesRepository;
    private final UsersImagesRepository usersImagesRepository;

    /**
     * 회원 가입
     * 1. 이메일 중복 검사
     * 2. 닉네임 중복 검사
     * 3. 프로필 이미지 URL이 있다면, 유효성 검사 후 이미지 엔티티를 가져온다.
     * 4. 비밀번호 인코딩
     * 5. 유저 테이블에 유저 저장
     * 6. 프로필 이미지가 있다면, 이미지 저장
     */
    @Transactional
    public void signUp(@Valid SignUpRequestDto signUpRequestDto) {

        Images image = null;

        if (usersRepository.existsByEmail(signUpRequestDto.email())) {
            throw new ApplicationException(USER_DUPLICATE_EMAIL);
        }

        if (usersRepository.existsByNickname(signUpRequestDto.nickname())) {
            throw new ApplicationException(USER_DUPLICATE_NICKNAME);
        }

        if (signUpRequestDto.profileImageUrl() != null  && !signUpRequestDto.profileImageUrl().isEmpty()) {
            String profileImagePath = FileUtil.extractPathFromUrl(signUpRequestDto.profileImageUrl());

            fileManager.validateProfileImageExists(profileImagePath);

            image = imagesRepository.findByImageUrl(profileImagePath)
                    .orElseThrow(() -> new ApplicationException(PROFILE_IMAGE_NOT_FOUND));
        }

        String encodedPassword = passwordEncoder.encode(signUpRequestDto.password());

        Users users = Users.createUser(
                signUpRequestDto.email(),
                signUpRequestDto.nickname(),
                encodedPassword);

        usersRepository.save(users);

        if(image != null){
            UsersImages usersImages = UsersImages.createUsersImages(users,image);
            usersImagesRepository.save(usersImages);
        }

    }

    // TODO : 해당 API는 비회원이 DB에 쉽게 접근할 수 있기 때문에 캐싱이 필요함.
    public Boolean emailAvailability(
            @Valid EmailAvailabilityRequestDto emailAvailabilityRequestDto) {
        return !usersRepository.existsByEmail(emailAvailabilityRequestDto.email());
    }

    // TODO : 해당 API는 비회원이 DB에 쉽게 접근할 수 있기 때문에 캐싱이 필요함.
    public Boolean nicknameAvailability(
            @Valid NicknameAvailabilityRequestDto emailAvailabilityRequestDto) {
        return !usersRepository.existsByNickname(emailAvailabilityRequestDto.nickname());
    }
}
