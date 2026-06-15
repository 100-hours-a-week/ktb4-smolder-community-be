package com.dragoncommunity.domain.auth.constant;

public final class AuthConstant {
    public static final String[] WHITE_LIST = {
            "/auth",
            "/users",
    };

    public static final Long DEVICE_ID_COOKIE_AGE = (long) (60 * 60 * 24 * 365);
    public static final Long REFRESH_TOKEN_COOKIE_AGE = (long) (60 * 60 * 24 * 365);

    private AuthConstant(){}
}
