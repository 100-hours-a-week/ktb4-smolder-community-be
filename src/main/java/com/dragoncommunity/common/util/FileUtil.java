package com.dragoncommunity.common.util;

import com.dragoncommunity.common.exception.ApplicationException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;

import static com.dragoncommunity.common.exception.enums.ApplicationErrorCode.FILE_URL_PARSE_FAILED;
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

    /**
     * 전체 URL 에서 도메인을 제외한 상대 경로 추출
     */
    public static String extractPathFromUrl(String url) {
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new ApplicationException(FILE_URL_PARSE_FAILED);
        }
        return uri.getPath();
    }

    /**
     * 클라이언트 접근 URL에서 실제 리소스가 있는 PATH로 변환
     */
    public static String extractFileNameFromPath(String path,String prefix) {

        if(!path.startsWith(prefix)){
            throw new ApplicationException(FILE_URL_PARSE_FAILED);
        }

        return path.substring(prefix.length());

    }
}
