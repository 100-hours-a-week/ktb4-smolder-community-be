package com.dragoncommunity.domain.image.model;

import com.dragoncommunity.common.model.SoftDeleteEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Images extends SoftDeleteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "image_url",unique = true, nullable = false)
    private String imageUrl;

    private Images(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public static Images createImage(String imageUrl){
        return new Images(imageUrl);
    }
}
