package com.dragoncommunity.common.util;

import com.dragoncommunity.common.exception.ApplicationException;
import jakarta.servlet.http.HttpServletRequest;

import static com.dragoncommunity.common.exception.enums.ApplicationErrorCode.AUTHORIZATION_HEADER_MISSING_OR_INVALID;
import static com.dragoncommunity.common.exception.enums.GlobalErrorCode.INTERNAL_SERVER_ERROR;

public class SecurityContextUtil {
    private SecurityContextUtil(){
        throw new ApplicationException(INTERNAL_SERVER_ERROR);
    }

    public static Long extractUserId(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if( userId== null){
            throw new ApplicationException(AUTHORIZATION_HEADER_MISSING_OR_INVALID);
        }
        return userId;
    }
}
