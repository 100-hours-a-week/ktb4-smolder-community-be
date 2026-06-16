package com.dragoncommunity.domain.auth.dto;

public record RefreshTokenInfoDto (
        String refreshToken,
        String deviceId
){
    public static RefreshTokenInfoDto of(String refreshToken, String deviceId){
        return new RefreshTokenInfoDto(refreshToken,deviceId);
    }
}
