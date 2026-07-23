package com.dragoncommunity.domain.user.controller;

import com.dragoncommunity.common.dto.ApiResponse;
import com.dragoncommunity.common.exception.ApplicationException;
import com.dragoncommunity.domain.user.dto.request.EmailAvailabilityRequestDto;
import com.dragoncommunity.domain.user.dto.request.NicknameAvailabilityRequestDto;
import com.dragoncommunity.domain.user.dto.request.SignUpRequestDto;
import com.dragoncommunity.domain.user.dto.response.AvailabilityResponseDto;
import com.dragoncommunity.domain.user.service.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.dragoncommunity.common.exception.enums.ApplicationErrorCode.PASSWORD_PASSWORD_CONFIRM_MISMATCH;
import static com.dragoncommunity.domain.user.constant.SuccessMessage.USER_SIGNUP_SUCCESS;
import static com.dragoncommunity.domain.user.constant.UsersServiceMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UsersController {
    private final UsersService usersService;

    @GetMapping("/nickname/availability")
    public ResponseEntity<ApiResponse<AvailabilityResponseDto>> getNickNameAvailability(
            @Valid @ModelAttribute NicknameAvailabilityRequestDto nicknameAvailabilityRequestDto) {
        Boolean availability = usersService.nicknameAvailability(nicknameAvailabilityRequestDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(
                        availability ? NICKNAME_AVAILABLE : NICKNAME_NOT_AVAILABLE,
                        AvailabilityResponseDto.of(availability)
                ));
    }

    @GetMapping("/email/availability")
    public ResponseEntity<ApiResponse<AvailabilityResponseDto>> getEmailAvailability(
            @Valid @ModelAttribute EmailAvailabilityRequestDto emailAvailabilityRequestDto) {
        Boolean availability = usersService.emailAvailability(emailAvailabilityRequestDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.of(
                                availability ? EMAIL_AVAILABLE : EMAIL_NOT_AVAILABLE,
                                AvailabilityResponseDto.of(availability)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createUser(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        passwordAndPasswordConfirmMatchCheck(signUpRequestDto);

        usersService.signUp(signUpRequestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of(USER_SIGNUP_SUCCESS));
    }

    /**
     * 비밀번호와 비밀번호 확인이 일치하는지 확인한다.
     */
    private void passwordAndPasswordConfirmMatchCheck(SignUpRequestDto signUpRequestDto){
        if (!signUpRequestDto.password().equals(signUpRequestDto.passwordConfirm())) {
            throw new ApplicationException(PASSWORD_PASSWORD_CONFIRM_MISMATCH);
        }
    }


}
