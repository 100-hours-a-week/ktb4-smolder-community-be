package com.dragoncommunity.common.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {
    private String allowedPattern;
    private String[] allowedOrigins;
    private String[] allowedMethods;
    private boolean allowCredentials;
}