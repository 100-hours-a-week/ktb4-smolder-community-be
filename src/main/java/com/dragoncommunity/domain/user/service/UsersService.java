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
        if (usersRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new ApplicationException(USER_DUPLICATE_EMAIL);
        }

        if (usersRepository.existsByNickname(signUpRequestDto.getEmail())) {
            throw new ApplicationException(USER_DUPLICATE_NICKNAME);
        }

        if (!signUpRequestDto.getPassword().equals(signUpRequestDto.getPasswordConfirm())) {
            throw new ApplicationException(PASSWORD_PASSWORD_CONFIRM_MISMATCH);
        }

        LocalDateTime now = LocalDateTime.now();
        String encodedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());
        Users users = Users.builder()
                .email(signUpRequestDto.getEmail())
                .nickname(signUpRequestDto.getNickname())
                .password(encodedPassword)
                .createdAt(now)
                .updatedAt(now)
                .build();

        usersRepository.save(users);
    }

    // TODO : 해당 API는 비회원이 DB에 쉽게 접근할 수 있기 때문에 캐싱이 필요함.
    public ApiResponse<AvailabilityResponseDto> emailAvailability(@Valid EmailAvailabilityRequestDto emailAvailabilityRequestDto) {

        if (usersRepository.existsByEmail(emailAvailabilityRequestDto.getEmail())) {

            AvailabilityResponseDto emailAvailabilityResponseDto = AvailabilityResponseDto.builder()
                    .availability(false)
                    .build();

            return ApiResponse.of(EMAIL_NOT_AVAILABLE, emailAvailabilityResponseDto);
        }

        AvailabilityResponseDto emailAvailabilityResponseDto = AvailabilityResponseDto.builder()
                .availability(true)
                .build();

        return ApiResponse.of(EMAIL_AVAILABLE, emailAvailabilityResponseDto);
    }

    // TODO : 해당 API는 비회원이 DB에 쉽게 접근할 수 있기 때문에 캐싱이 필요함.
    public ApiResponse<AvailabilityResponseDto> nicknameAvailability(@Valid NicknameAvailabilityRequestDto emailAvailabilityRequestDto) {

        if (usersRepository.existsByEmail(emailAvailabilityRequestDto.getNickname())) {

            AvailabilityResponseDto nicknameAvailabilityResponseDto = AvailabilityResponseDto.builder()
                    .availability(false)
                    .build();

            return ApiResponse.of(NICKNAME_NOT_AVAILABLE, nicknameAvailabilityResponseDto);
        }

        AvailabilityResponseDto nicknameAvailabilityResponseDto = AvailabilityResponseDto.builder()
                .availability(true)
                .build();

        return ApiResponse.of(NICKNAME_AVAILABLE, nicknameAvailabilityResponseDto);
    }

}
