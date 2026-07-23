package com.dragoncommunity.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

import static com.dragoncommunity.domain.auth.constant.AuthConstant.AUTHORIZATION_MISSING;

public record ReissueTokenRequestDto(
        @NotBlank(message = AUTHORIZATION_MISSING)
        String refreshToken,

        @NotBlank(message = AUTHORIZATION_MISSING)
        String deviceId
) {
    public static ReissueTokenRequestDto of(String refreshToken, String deviceId){
        return new ReissueTokenRequestDto(refreshToken, deviceId);
    }
}
