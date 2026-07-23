package com.dragoncommunity.domain.user.model;

import com.dragoncommunity.domain.image.model.Images;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UsersImages {

    @EmbeddedId
    private UsersImagesId id;

    @MapsId("userId")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private Users user;

    @MapsId("imageId")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", unique = true, nullable = false)
    private Images image;

    private UsersImages(Users user, Images image) {
        this.user = user;
        this.image = image;
        this.id = new UsersImagesId(user.getUserId(), image.getImageId());
    }

    public static UsersImages createUsersImages(Users user, Images image) {
        return new UsersImages(user, image);
    }

    @Embeddable
    @EqualsAndHashCode
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UsersImagesId implements Serializable {
        private Long userId;
        private Long imageId;

        private UsersImagesId(Long userId, Long imageId) {
            this.userId = userId;
            this.imageId = imageId;
        }
    }
}