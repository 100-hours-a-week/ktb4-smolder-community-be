package com.dragoncommunity.domain.auth.controller;

import com.dragoncommunity.common.dto.ApiResponse;
import com.dragoncommunity.domain.auth.dto.AccessTokenInfoDto;
import com.dragoncommunity.domain.auth.dto.ReissueTokenResult;
import com.dragoncommunity.domain.auth.dto.SignInResultDto;
import com.dragoncommunity.domain.auth.dto.request.ReissueTokenRequestDto;
import com.dragoncommunity.domain.auth.dto.request.SignInRequestDto;
import com.dragoncommunity.domain.auth.dto.response.SignInResponseDto;
import com.dragoncommunity.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import static com.dragoncommunity.domain.auth.constant.AuthConstant.*;
import static com.dragoncommunity.domain.auth.constant.SuccessMessage.SIGN_IN_SUCCESS;
import static com.dragoncommunity.domain.auth.constant.SuccessMessage.TOKEN_REISSUE_SUCCESS;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * 로그인 정보 생성
     * 1. 로그인 처리
     * 2. device id, refresh token을 쿠키에 저장
     */
    @PostMapping
    public ApiResponse<SignInResponseDto> createAuth(
            @Valid @RequestBody
            SignInRequestDto signInRequestDto,
            HttpServletResponse httpServletResponse){
        SignInResultDto signInResultDto = authService.createAuthToken(signInRequestDto);

        addHttpOnlyCookie(httpServletResponse, REFRESH_TOKEN_COOKIE_NAME,
                signInResultDto.refreshTokenInfoDto().refreshToken(), REFRESH_TOKEN_COOKIE_AGE);
        addHttpOnlyCookie(httpServletResponse, DEVICE_ID_COOKIE_NAME,
                signInResultDto.refreshTokenInfoDto().deviceId(), DEVICE_ID_COOKIE_AGE);

        return ApiResponse.of(SIGN_IN_SUCCESS, signInResultDto.signInResponseDto());
    }

    /**
     * 리프레시 토큰 사용하여 엑세스 토큰 재발행
     * 1. 토큰 재발행(Access Token, Refresh Token)
     * 2. 기존 device id, 새로운 refresh token을 덮어씌움
     */
    @PutMapping
    public ApiResponse<AccessTokenInfoDto> reissueAuth(
            @CookieValue(name = REFRESH_TOKEN_COOKIE_NAME, required = false)
            String refreshToken,
            @CookieValue(name = DEVICE_ID_COOKIE_NAME, required = false)
            String deviceId,
            HttpServletResponse httpServletResponse){
        ReissueTokenResult reissueTokenResult = authService.reissueAuthToken(ReissueTokenRequestDto.of(refreshToken,deviceId));

        addHttpOnlyCookie(httpServletResponse, REFRESH_TOKEN_COOKIE_NAME,
                reissueTokenResult.refreshTokenInfoDto().refreshToken(), REFRESH_TOKEN_COOKIE_AGE);
        addHttpOnlyCookie(httpServletResponse, DEVICE_ID_COOKIE_NAME,
                reissueTokenResult.refreshTokenInfoDto().deviceId(), DEVICE_ID_COOKIE_AGE);

        return ApiResponse.of(TOKEN_REISSUE_SUCCESS, reissueTokenResult.accessTokenInfoDto());
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
