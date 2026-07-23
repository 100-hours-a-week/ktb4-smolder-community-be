package com.dragoncommunity.domain.user.model;

import com.dragoncommunity.common.model.SoftDeleteEntity;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends SoftDeleteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    private Users(String email, String nickname, String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    public static Users createUser(String email, String nickname, String password) {
        return new Users(
                email,
                nickname,
                password
        );
    }

}