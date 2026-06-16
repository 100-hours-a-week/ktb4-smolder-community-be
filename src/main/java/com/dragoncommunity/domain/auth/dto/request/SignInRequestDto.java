package com.dragoncommunity.domain.auth.dto.request;

import com.dragoncommunity.domain.user.constant.RegularExpression;
import com.dragoncommunity.domain.user.constant.ValidationMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignInRequestDto(
        @NotBlank(message = ValidationMessage.EMAIL_NOT_BLANK)
        @Email(message = ValidationMessage.EMAIL_PATTERN_NOT_AVAILABILITY)
        String email,

        @NotBlank(message = ValidationMessage.PASSWORD_NOT_BLANK)
        @Pattern(
                regexp = RegularExpression.PASSWORD_REGEXP,
                message = ValidationMessage.PASSWORD_PATTERN_NOT_AVAILABILITY
        )
        String password
) {
}