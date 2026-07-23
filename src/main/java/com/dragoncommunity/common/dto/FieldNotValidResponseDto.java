package com.dragoncommunity.common.dto;

public record FieldNotValidResponseDto (String field){
    public static FieldNotValidResponseDto of(String field){
        return new FieldNotValidResponseDto(field);
    }
}
