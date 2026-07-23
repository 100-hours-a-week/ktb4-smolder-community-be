package com.dragoncommunity.domain.auth.dto;

public record ReissueTokenResult(
        AccessTokenInfoDto accessTokenInfoDto,
        RefreshTokenInfoDto refreshTokenInfoDto
) {
    public static ReissueTokenResult of(AccessTokenInfoDto accessTokenInfoDto, RefreshTokenInfoDto refreshTokenInfoDto){
        return new ReissueTokenResult(accessTokenInfoDto,refreshTokenInfoDto);
    }
}
