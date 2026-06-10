package com.dragoncommunity.domain.user.constant;

public final class ValidationMessage {
    public static final String EMAIL_NOT_BLANK = "이메일은 필수 입력 항목입니다.";
    public static final String EMAIL_PATTERN_NOT_AVAILABILITY = "이메일 형식이 올바르지 않습니다.";

    public static final String NICKNAME_NOT_BLANK = "닉네임은 필수 입력 항목입니다.";
    public static final String NICKNAME_PATTERN_NOT_AVAILABILITY = "닉네임은 한글 또는 영문 2~10자여야 합니다. (특수문자, 공백, 숫자 불가)";

    public static final String PASSWORD_NOT_BLANK = "비밀번호는 필수 입력 항목입니다.";
    public static final String PASSWORD_PATTERN_NOT_AVAILABILITY = "비밀번호는 영문, 숫자, 특수문자를 포함하여 8~20자여야 합니다.";

    public static final String PASSWORD_CONFIRM_NOT_BLANK = "비밀번호 확인은 필수 입력 항목입니다.";

    private ValidationMessage() {
    }
}
