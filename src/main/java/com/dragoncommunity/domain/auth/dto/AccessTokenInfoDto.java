package com.dragoncommunity.domain.auth.dto;

public record AccessTokenInfoDto(
        String accessToken,
        Long expiresIn
) {
    public static AccessTokenInfoDto of(String accessToken, Long expiresIn){
        return new AccessTokenInfoDto(accessToken,expiresIn);
    }
}
