package com.dragoncommunity.domain.user.dto.request;

import com.dragoncommunity.domain.user.constant.RegularExpression;
import com.dragoncommunity.domain.user.constant.ValidationMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignUpRequestDto {

    @NotBlank(message = ValidationMessage.EMAIL_NOT_BLANK)
    @Email(message = ValidationMessage.EMAIL_PATTERN_NOT_AVAILABILITY)
    private String email;

    @NotBlank(message = ValidationMessage.PASSWORD_NOT_BLANK)
    @Pattern(regexp = RegularExpression.PASSWORD_REGEXP, message = ValidationMessage.PASSWORD_PATTERN_NOT_AVAILABILITY)
    private String password;

    @NotBlank(message = ValidationMessage.PASSWORD_CONFIRM_NOT_BLANK)
    @Pattern(regexp = RegularExpression.PASSWORD_REGEXP, message = ValidationMessage.PASSWORD_PATTERN_NOT_AVAILABILITY)
    private String passwordConfirm;

    @NotBlank(message = ValidationMessage.NICKNAME_NOT_BLANK)
    @Pattern(regexp = RegularExpression.NICKNAME_REGEXP,message = ValidationMessage.NICKNAME_PATTERN_NOT_AVAILABILITY)
    private String nickname;
}
