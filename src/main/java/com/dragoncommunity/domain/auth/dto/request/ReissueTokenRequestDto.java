package com.dragoncommunity.domain.auth.dto.request;

public record ReissueTokenRequestDto(
        String refreshToken,
        String deviceId
) {
    public static ReissueTokenRequestDto of(String refreshToken, String deviceId){
        return new ReissueTokenRequestDto(refreshToken, deviceId);
    }
}
