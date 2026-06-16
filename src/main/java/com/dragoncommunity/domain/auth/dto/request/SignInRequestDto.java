package com.dragoncommunity.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import static com.dragoncommunity.domain.user.constant.RegularExpression.PASSWORD_REGEXP;
import static com.dragoncommunity.domain.user.constant.ValidationMessage.*;

public record SignInRequestDto(
        @NotBlank(message = EMAIL_NOT_BLANK)
        @Email(message = EMAIL_PATTERN_NOT_AVAILABILITY)
        String email,

        @NotBlank(message = PASSWORD_NOT_BLANK)
        @Pattern(
                regexp = PASSWORD_REGEXP,
                message = PASSWORD_PATTERN_NOT_AVAILABILITY
        )
        String password
) {
}