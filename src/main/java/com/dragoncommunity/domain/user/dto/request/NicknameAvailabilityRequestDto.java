package com.dragoncommunity.domain.user.dto.request;


import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class NicknameAvailabilityRequestDto {
    @Column(unique = true, nullable = false)
    private String nickname;
}
