package com.dragoncommunity.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

import static com.dragoncommunity.domain.auth.constant.AuthConstant.AUTHORIZATION_MISSING;

public record DeleteAuthTokenRequestDto(
        @NotBlank(message = AUTHORIZATION_MISSING)
        String refreshToken
) {
    public static DeleteAuthTokenRequestDto of(String refreshToken){
        return new DeleteAuthTokenRequestDto(refreshToken);
    }
}
