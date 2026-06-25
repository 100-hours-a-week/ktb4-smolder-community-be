package com.dragoncommunity.domain.auth.constant;

public final class AuthConstant {
    public static final String[] WHITE_LIST = {
            "/auth",
            "/user/**",
            "/user",
            "/image",
            "/public/profile/**",
    };

    public static final Long DEVICE_ID_COOKIE_AGE = (long) (604800);
    public static final Long REFRESH_TOKEN_COOKIE_AGE = (long) (604800);

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    public static final String DEVICE_ID_COOKIE_NAME = "deviceId";

    public static final String AUTHORIZATION_MISSING = "인증 정보가 유효하지 않습니다.";

    private AuthConstant(){}
}
