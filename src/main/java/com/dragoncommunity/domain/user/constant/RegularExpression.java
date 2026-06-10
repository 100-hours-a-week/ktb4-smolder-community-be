package com.dragoncommunity.domain.user.constant;

public final class RegularExpression {
    public static final String NICKNAME_REGEXP = "^[가-힣a-zA-Z]{2,10}$";
    public static final String PASSWORD_REGEXP = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$";

    private RegularExpression() {
    }
}
