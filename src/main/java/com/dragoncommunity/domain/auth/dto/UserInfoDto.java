package com.dragoncommunity.domain.auth.dto;

import com.dragoncommunity.common.util.FileUtil;

public record UserInfoDto(
        String email,
        String nickname,
        String profileImageUrl
) {
    public static UserInfoDto of(String email, String nickname, String profileImageUrl){

        if(profileImageUrl != null){
            profileImageUrl = FileUtil.toFullUrl(profileImageUrl);
        }

        return new UserInfoDto(email,nickname,profileImageUrl);
    }
}
