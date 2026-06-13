package com.dragoncommunity.common.exception.enums;

import com.dragoncommunity.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApplicationErrorCode implements ErrorCode {
    USER_DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    USER_DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    PASSWORD_PASSWORD_CONFIRM_MISMATCH(HttpStatus.BAD_REQUEST,"비밀번호와 비밀번호 확인이 다릅니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"파일 업로드에 실패했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
