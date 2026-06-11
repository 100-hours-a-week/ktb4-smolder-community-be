package com.dragoncommunity.common.exception;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;

import static com.dragoncommunity.common.exception.enums.ApplicationErrorCode.*;
import static com.dragoncommunity.common.exception.enums.GlobalErrorCode.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {GlobalExceptionHandlerTest.TestController.class, GlobalExceptionHandler.class})
@Import(GlobalExceptionHandlerTest.TestController.class)
class GlobalExceptionHandlerTest {

    private static final String INVALID_MESSAGE= "Not Blank";

    @Autowired
    private MockMvc mockMvc;

    @RestController
    static class TestController {
        @GetMapping("/test/user-duplicate-email-exception")
        public void throwApplicationException() {
            throw new ApplicationException(USER_DUPLICATE_EMAIL);
        }

        @PostMapping("/test/validation")
        public void throwValidationException(@Valid @RequestBody TestDto dto) {}

        @GetMapping("/test/missing-param")
        public void throwMissingParamException(@RequestParam("name") String name) {}

        @GetMapping("/test/uncaught")
        public void throwUncaughtException() {
            throw new RuntimeException();
        }
    }

    record TestDto(
            @NotBlank(message=INVALID_MESSAGE) String value
    ) {}

    @Test
    @DisplayName("ApplicationException 발생 시, 해당 에러코드의 상태값과 메시지를 반환한다.")
    void applicationExceptionTest() throws Exception {
        mockMvc.perform(get("/test/user-duplicate-email-exception"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(USER_DUPLICATE_EMAIL.getMessage()));
    }

    @Test
    @DisplayName("@Valid 검증 실패(MethodArgumentNotValidException) 시, INVALID_INPUT_VALUE 에러를 반환한다.")
    void handleMethodArgumentNotValidTest() throws Exception {
        // 빈 객체를 보내 @NotBlank 검증을 실패하게 만듦
        String json = "{\"value\":\"\"}";

        mockMvc.perform(post("/test/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(INVALID_MESSAGE));
    }

    @Test
    @DisplayName("필수 쿼리 파라미터 누락 시, MISSING_INPUT_VALUE 에러를 반환한다.")
    void handleMissingServletRequestParameterTest() throws Exception {
        // ?name=xxx 파라미터를 누락하고 요청
        mockMvc.perform(get("/test/missing-param"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(MISSING_INPUT_VALUE.getMessage()));
    }

    @Test
    @DisplayName("지원하지 않는 Media Type 요청 시, UNSUPPORTED_MEDIA_TYPE 에러를 반환한다.")
    void handleHttpMediaTypeNotSupportedTest() throws Exception {
        // JSON을 기대하는 곳에 TEXT_PLAIN을 보냄
        mockMvc.perform(post("/test/validation")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("test"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.message").value(UNSUPPORTED_MEDIA_TYPE.getMessage()));
    }

    @Test
    @DisplayName("존재하지 않는 URL로 요청 시(NoResourceFoundException), RESOURCE_NOT_FOUND 에러를 반환한다.")
    void handleNoResourceFoundTest() throws Exception {
        // 존재하지 않는 경로로 요청
        mockMvc.perform(get("/not-exist-static-resource.html"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(RESOURCE_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("서버 내부에서 알 수 없는 예외(Exception) 발생 시, INTERNAL_SERVER_ERROR를 반환한다.")
    void handleAllUncaughtExceptionTest() throws Exception {
        mockMvc.perform(get("/test/uncaught"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(INTERNAL_SERVER_ERROR.getMessage()));
    }
}