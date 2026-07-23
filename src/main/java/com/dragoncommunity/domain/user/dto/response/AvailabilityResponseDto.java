package com.dragoncommunity.domain.user.dto.response;

public record AvailabilityResponseDto(Boolean availability) {
    public static AvailabilityResponseDto of(Boolean availability){
        return new AvailabilityResponseDto(availability);
    }
}
