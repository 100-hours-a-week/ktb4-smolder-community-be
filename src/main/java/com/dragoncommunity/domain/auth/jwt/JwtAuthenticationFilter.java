package com.dragoncommunity.domain.auth.jwt;


import com.dragoncommunity.common.dto.ApiResponse;
import com.dragoncommunity.common.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.dragoncommunity.common.exception.enums.ApplicationErrorCode.AUTHORIZATION_HEADER_MISSING_OR_INVALID;
import static com.dragoncommunity.common.exception.enums.ApplicationErrorCode.INVALID_TOKEN;
import static com.dragoncommunity.domain.auth.constant.AuthConstant.WHITE_LIST;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendErrorResponse(response, AUTHORIZATION_HEADER_MISSING_OR_INVALID);
            return;
        }

        String token = authHeader.substring(7);

        try {
            jwtProvider.parse(token);

            if (!jwtProvider.isAccessToken(token)) {
                sendErrorResponse(response, INVALID_TOKEN);
                return;
            }

            Long userId = jwtProvider.getUserId(token);
            request.setAttribute("userId", userId);

            filterChain.doFilter(request, response);

        } catch (Exception exception) {
            sendErrorResponse(response, INVALID_TOKEN);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {

        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String responseBody = objectMapper.writeValueAsString(ApiResponse.of(errorCode.getMessage()));
        response.getWriter().write(responseBody);
    }
}