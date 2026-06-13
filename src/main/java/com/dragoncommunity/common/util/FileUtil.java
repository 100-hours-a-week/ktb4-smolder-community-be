package com.dragoncommunity.common.util;

import com.dragoncommunity.common.exception.ApplicationException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static com.dragoncommunity.common.exception.enums.GlobalErrorCode.INTERNAL_SERVER_ERROR;

public class FileUtil {

    /**
     * 인스턴스 생성 방지
     */
    private FileUtil(){
        throw new ApplicationException(INTERNAL_SERVER_ERROR);
    }

    /**
     * 상대 경로에 서버 도메인을 붙여 전체 URL 생성
     */
    public static String toFullUrl(String relativePath) {
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .build()
                .toUriString();

        return baseUrl + relativePath;
    }
}
