package com.dragoncommunity.domain.post.model;

import com.dragoncommunity.common.model.SoftDeleteEntity;
import com.dragoncommunity.domain.user.model.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Posts extends SoftDeleteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "title",nullable = false)
    private String title;

    @Column(name = "content",nullable = false)
    private String content;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    private Posts(String title, String content,Users user){
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public static Posts createPost(String title, String content,Users user){
        return new Posts(
                title,
                content,
                user
        );
    }
}
