package com.dragoncommunity.common.config;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import static com.dragoncommunity.common.config.constant.FileConfigConstant.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 정적 리소스 접근 설정
     * 1. 정적 리소스 접근 URL
     * 2. 정적 리소스 실제 위치
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String uploadPath = UPLOAD_FOLDER.toUri().toString();

        registry.addResourceHandler(RESOURCE_ALLOW_URL)
                .addResourceLocations(uploadPath);
    }
}
