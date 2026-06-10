package com.dragoncommunity.domain.user.service;

import com.dragoncommunity.common.dto.ApiResponse;
import com.dragoncommunity.common.exception.ApplicationException;
import com.dragoncommunity.common.security.util.PasswordEncoder;
import com.dragoncommunity.domain.user.dto.request.EmailAvailabilityRequestDto;
import com.dragoncommunity.domain.user.dto.request.NicknameAvailabilityRequestDto;
import com.dragoncommunity.domain.user.dto.request.SignUpRequestDto;
import com.dragoncommunity.domain.user.dto.response.AvailabilityResponseDto;
import com.dragoncommunity.domain.user.model.Users;
import com.dragoncommunity.domain.user.repository.UsersRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

import static com.dragoncommunity.common.exception.enums.ApplicationErrorCode.*;
import static com.dragoncommunity.domain.user.constant.UsersServiceMessage.*;

@Service
@Validated
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(@Valid SignUpRequestDto signUpRequestDto) {
        if (usersRepository.existsByEmail(signUpRequestDto.email())) {
            throw new ApplicationException(USER_DUPLICATE_EMAIL);
        }

        if (usersRepository.existsByNickname(signUpRequestDto.email())) {
            throw new ApplicationException(USER_DUPLICATE_NICKNAME);
        }

        if (!signUpRequestDto.password().equals(signUpRequestDto.passwordConfirm())) {
            throw new ApplicationException(PASSWORD_PASSWORD_CONFIRM_MISMATCH);
        }

        LocalDateTime now = LocalDateTime.now();
        String encodedPassword = passwordEncoder.encode(signUpRequestDto.password());
        Users users = Users.builder()
                .email(signUpRequestDto.email())
                .nickname(signUpRequestDto.nickname())
                .password(encodedPassword)
                .createdAt(now)
                .updatedAt(now)
                .build();

        usersRepository.save(users);
    }

    // TODO : 해당 API는 비회원이 DB에 쉽게 접근할 수 있기 때문에 캐싱이 필요함.
    public ApiResponse<AvailabilityResponseDto> emailAvailability(@Valid EmailAvailabilityRequestDto emailAvailabilityRequestDto) {

        if (usersRepository.existsByEmail(emailAvailabilityRequestDto.email())) {
            return ApiResponse.of(EMAIL_NOT_AVAILABLE, AvailabilityResponseDto.of(false));
        }

        return ApiResponse.of(EMAIL_AVAILABLE, AvailabilityResponseDto.of(true));
    }

    // TODO : 해당 API는 비회원이 DB에 쉽게 접근할 수 있기 때문에 캐싱이 필요함.
    public ApiResponse<AvailabilityResponseDto> nicknameAvailability(@Valid NicknameAvailabilityRequestDto emailAvailabilityRequestDto) {

        if (usersRepository.existsByEmail(emailAvailabilityRequestDto.nickname())) {
            return ApiResponse.of(NICKNAME_NOT_AVAILABLE, AvailabilityResponseDto.of(false));
        }

        return ApiResponse.of(NICKNAME_AVAILABLE, AvailabilityResponseDto.of(true));
    }

}
