package com.dragoncommunity.domain.image.dto.response;

import com.dragoncommunity.common.util.FileUtil;
import com.dragoncommunity.domain.image.model.Images;

public record PostImageUploadResponseDto(String imageUrl){
    public static PostImageUploadResponseDto of(String imageUrl){
        return new PostImageUploadResponseDto(imageUrl);
    }

    public static PostImageUploadResponseDto from(Images image){

        String url = FileUtil.toFullUrl(image.getImageUrl());

        return of(url);
    }
}
