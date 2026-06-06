package com.dragoncommunity.common.exception.enums;

import com.dragoncommunity.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApplicationErrorCode implements ErrorCode {
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
