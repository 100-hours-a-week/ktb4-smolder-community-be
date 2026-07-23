package com.dragoncommunity.domain.auth.dto.response;

import com.dragoncommunity.domain.auth.dto.AccessTokenInfoDto;
import com.dragoncommunity.domain.auth.dto.UserInfoDto;

public record SignInResponseDto(
        AccessTokenInfoDto accessTokenInfoDto,
        UserInfoDto userInfoDto
) {
    public static SignInResponseDto of(AccessTokenInfoDto accessTokenInfoDto, UserInfoDto userInfoDto){
        return new SignInResponseDto(accessTokenInfoDto, userInfoDto);
    }
}
