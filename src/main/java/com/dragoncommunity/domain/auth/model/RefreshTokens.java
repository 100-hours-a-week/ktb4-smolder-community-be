package com.dragoncommunity.domain.auth.model;

import com.dragoncommunity.common.model.CreatedEntity;
import com.dragoncommunity.domain.user.model.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokens extends CreatedEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long refreshTokenId;

    @Column(name = "refresh_token", nullable = false, unique = true)
    private String refreshToken;

    @Column(name = "device_id", nullable = false, unique = true)
    private String deviceId;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    private RefreshTokens(String refreshToken, String deviceId, LocalDateTime expiredAt,Users user) {
        this.refreshToken = refreshToken;
        this.deviceId = deviceId;
        this.expiredAt = expiredAt;
        this.user = user;
    }

    public static RefreshTokens createRefreshToken(String refreshToken, String deviceId, LocalDateTime expiredAt,Users user) {
        return new RefreshTokens(
                refreshToken,
                deviceId,
                expiredAt,
                user
        );
    }

}
