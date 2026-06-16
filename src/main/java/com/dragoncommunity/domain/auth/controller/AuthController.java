package com.dragoncommunity.domain.auth.controller;

import com.dragoncommunity.common.dto.ApiResponse;
import com.dragoncommunity.domain.auth.dto.SignInResultDto;
import com.dragoncommunity.domain.auth.dto.request.SignInRequestDto;
import com.dragoncommunity.domain.auth.dto.response.SignInResponseDto;
import com.dragoncommunity.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.dragoncommunity.domain.auth.constant.AuthConstant.DEVICE_ID_COOKIE_AGE;
import static com.dragoncommunity.domain.auth.constant.AuthConstant.REFRESH_TOKEN_COOKIE_AGE;
import static com.dragoncommunity.domain.auth.constant.SuccessMessage.SIGN_IN_SUCCESS;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * 로그인 정보 생성
     * 1. 로그인 처리
     * 2. device id, refresh token id를 쿠키에 저장
     */
    @PostMapping
    public ApiResponse<SignInResponseDto> createAuth(
            @Valid @RequestBody
            SignInRequestDto signInRequestDto,
            HttpServletResponse httpServletResponse){
        SignInResultDto signInResultDto = authService.signIn(signInRequestDto);

        addHttpOnlyCookie(httpServletResponse, "refresh_token",
                signInResultDto.refreshTokenInfoDto().refreshToken(), REFRESH_TOKEN_COOKIE_AGE);
        addHttpOnlyCookie(httpServletResponse, "device_id",
                signInResultDto.refreshTokenInfoDto().deviceId(), DEVICE_ID_COOKIE_AGE);

        return ApiResponse.of(SIGN_IN_SUCCESS, signInResultDto.signInResponseDto());
    }

    private void addHttpOnlyCookie(HttpServletResponse response, String name, String value, long maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(false) // TODO : HTTPS 배포 후 재설정
                .path("/")
                .maxAge(maxAge)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }


}
