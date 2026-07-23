package com.dragoncommunity.domain.auth.dto;

import com.dragoncommunity.domain.auth.dto.response.SignInResponseDto;

public record SignInResultDto(
        SignInResponseDto signInResponseDto,
        RefreshTokenInfoDto refreshTokenInfoDto
        ) {
        public static SignInResultDto of(SignInResponseDto signInResponseDto, RefreshTokenInfoDto refreshTokenInfoDto){
                return new SignInResultDto(signInResponseDto,refreshTokenInfoDto);
        }
}
