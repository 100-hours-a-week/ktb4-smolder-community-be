package com.dragoncommunity.domain.user.dto.request;


import com.dragoncommunity.domain.user.constant.RegularExpression;
import com.dragoncommunity.domain.user.constant.ValidationMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class NicknameAvailabilityRequestDto {
    @NotBlank(message = ValidationMessage.NICKNAME_NOT_BLANK)
    @Pattern(regexp = RegularExpression.NICKNAME_REGEXP,message = ValidationMessage.NICKNAME_PATTERN_NOT_AVAILABILITY)
    private String nickname;
}
