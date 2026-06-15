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
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"파일 업로드에 실패했습니다."),
    FILE_URL_PARSE_FAILED(HttpStatus.BAD_REQUEST,"파일 경로가 유효하지 않습니다."),
    FILE_NOT_EXISTS(HttpStatus.BAD_REQUEST,"파일이 존재하지 않습니다."),
    PROFILE_IMAGE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR,"업로드된 프로필 이미지가 존재하지 않습니다."),
    AUTHORIZATION_HEADER_MISSING_OR_INVALID(HttpStatus.UNAUTHORIZED,"인증 정보가 없거나 형식이 올바르지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"인증 정보가 올바르지 않습니다."),
    AUTH_INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED,"아이디 또는 비밀번호가 올바르지 않습니다.");
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
