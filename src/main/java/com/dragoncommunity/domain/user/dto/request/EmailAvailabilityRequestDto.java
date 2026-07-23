package com.dragoncommunity.domain.user.dto.request;


import com.dragoncommunity.domain.user.constant.ValidationMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public record EmailAvailabilityRequestDto(
        @NotBlank(message = ValidationMessage.EMAIL_NOT_BLANK)
        @Email(message = ValidationMessage.EMAIL_PATTERN_NOT_AVAILABILITY)
        String email
) {
}