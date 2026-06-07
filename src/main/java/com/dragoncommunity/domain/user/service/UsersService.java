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

import static com.dragoncommunity.common.exception.enums.ApplicationErrorCode.PASSWORD_PASSWORD_CONFIRM_MISMATCH;
import static com.dragoncommunity.common.exception.enums.ApplicationErrorCode.USER_DUPLICATE_EMAIL;

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

    public ApiResponse<AvailabilityResponseDto> emailAvailability(@Valid EmailAvailabilityRequestDto emailAvailabilityRequestDto) {

        if (usersRepository.existsByEmail(emailAvailabilityRequestDto.getEmail())) {

            AvailabilityResponseDto emailAvailabilityResponseDto = AvailabilityResponseDto.builder()
                    .availability(false)
                    .build();

            return ApiResponse.of("이미 사용중인 이메일입니다.", emailAvailabilityResponseDto);
        }

        AvailabilityResponseDto emailAvailabilityResponseDto = AvailabilityResponseDto.builder()
                .availability(true)
                .build();

        return ApiResponse.of("사용 가능한 이메일입니다.", emailAvailabilityResponseDto);
    }

    public ApiResponse<AvailabilityResponseDto> nicknameAvailability(@Valid NicknameAvailabilityRequestDto emailAvailabilityRequestDto) {

        if (usersRepository.existsByEmail(emailAvailabilityRequestDto.getNickname())) {

            AvailabilityResponseDto nicknameAvailabilityResponseDto = AvailabilityResponseDto.builder()
                    .availability(false)
                    .build();

            return ApiResponse.of("이미 사용중인 닉네임입니다.", nicknameAvailabilityResponseDto);
        }

        AvailabilityResponseDto nicknameAvailabilityResponseDto = AvailabilityResponseDto.builder()
                .availability(true)
                .build();

        return ApiResponse.of("사용 가능한 닉네임입니다.", nicknameAvailabilityResponseDto);
    }

}
