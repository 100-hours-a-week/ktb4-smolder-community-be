package com.dragoncommunity.domain.post.model;

import com.dragoncommunity.domain.image.model.Images;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostsImages {
    @EmbeddedId
    private PostsImagesId id;

    @MapsId("postId")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", unique = true, nullable = false)
    private Posts post;

    @MapsId("imageId")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id",unique = true, nullable = false)
    private Images image;

    private PostsImages(Posts post, Images image) {
        this.post = post;
        this.image = image;
        this.id = new PostsImagesId(post.getPostId(), image.getImageId());
    }

    public static PostsImages createPostsImages(Posts post, Images image) {
        return new PostsImages(post, image);
    }

    @Embeddable
    @EqualsAndHashCode
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PostsImagesId implements Serializable {
        private Long postId;
        private Long imageId;

        private PostsImagesId(Long postId, Long imageId) {
            this.postId = postId;
            this.imageId = imageId;
        }
    }
}
