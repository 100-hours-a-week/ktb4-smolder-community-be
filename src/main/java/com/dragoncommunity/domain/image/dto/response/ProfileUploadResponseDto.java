package com.dragoncommunity.domain.image.dto.response;

import com.dragoncommunity.common.util.FileUtil;
import com.dragoncommunity.domain.image.model.Images;

public record ProfileUploadResponseDto (String profileImageUrl){
    public static ProfileUploadResponseDto of(String profileImageUrl){
        return new ProfileUploadResponseDto(profileImageUrl);
    }

    public static ProfileUploadResponseDto from(Images image){

        String url = FileUtil.toFullUrl(image.getImageUrl());

        return of(url);
    }
}
