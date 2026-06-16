package com.dragoncommunity.domain.auth.service;

import com.dragoncommunity.common.exception.ApplicationException;
import com.dragoncommunity.common.security.util.PasswordEncoder;
import com.dragoncommunity.domain.auth.dto.RefreshTokenInfoDto;
import com.dragoncommunity.domain.auth.dto.SignInResultDto;
import com.dragoncommunity.domain.auth.dto.AccessTokenInfoDto;
import com.dragoncommunity.domain.auth.dto.UserInfoDto;
import com.dragoncommunity.domain.auth.dto.request.SignInRequestDto;
import com.dragoncommunity.domain.auth.dto.response.SignInResponseDto;
import com.dragoncommunity.domain.auth.jwt.JwtProvider;
import com.dragoncommunity.domain.auth.model.RefreshTokens;
import com.dragoncommunity.domain.auth.repository.RefreshTokensRepository;
import com.dragoncommunity.domain.user.model.Users;
import com.dragoncommunity.domain.user.repository.UsersImagesRepository;
import com.dragoncommunity.domain.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.dragoncommunity.common.exception.enums.ApplicationErrorCode.AUTH_INVALID_CREDENTIALS;

@Service
@RequiredArgsConstructor
@Validated
public class AuthService {
    private final RefreshTokensRepository refreshTokensRepository;
    private final UsersRepository usersRepository;
    private final UsersImagesRepository usersImagesRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    /**
     * 로그인 기능 구현
     * 1. 이메일 존재 확인
     * 2. 비밀번호 확인
     * 3. 프로필 이미지 존재 확인
     * 4. 엑세스 토큰 생성
     * 5. 리프레쉬 토큰 생성
     *  5-1. 해당 유저가 발급한 리프래시 토큰의 개수가 5개 이상이면 삭제
     * 6. device id 생성 (장치 구분용)
     * 7. DB에 저장
     */
    @Transactional
    public SignInResultDto signIn(SignInRequestDto signInRequestDto) {
        Users user = usersRepository.findByEmail(signInRequestDto.email())
                .orElseThrow(() -> new ApplicationException(AUTH_INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(signInRequestDto.password(),user.getPassword())) {
            throw new ApplicationException(AUTH_INVALID_CREDENTIALS);
        }

        String profileImageUrl =  usersImagesRepository.findByUser(user).
                map(ui -> ui.getImage().getImageUrl())
                .orElse(null);

        String accessToken = jwtProvider.createAccessToken(
                user.getUserId(),
                user.getEmail(),
                user.getNickname()
        );

        String refreshToken = jwtProvider.createRefreshToken(user.getUserId());

        LocalDateTime refreshTokenExpiredAt = jwtProvider.getExpiration(refreshToken);

        List<RefreshTokens> tokens = refreshTokensRepository.findByUserOrderByCreatedAtAsc(user);

        if (tokens.size() >= 5) {
            RefreshTokens oldest = tokens.get(0);
            refreshTokensRepository.delete(oldest);
        }

        String deviceId = generateDeviceId();

        refreshTokensRepository.save(
                RefreshTokens.createRefreshToken(
                        refreshToken,
                        deviceId,
                        refreshTokenExpiredAt,
                        user
                )
        );

        return SignInResultDto.of(
                SignInResponseDto.of(
                        AccessTokenInfoDto.of(
                                accessToken,jwtProvider.getAccessTokenValidityInMilliseconds()
                        ),
                        UserInfoDto.of(
                                user.getEmail(),
                                user.getNickname(),
                                profileImageUrl
                        )
                ),
                RefreshTokenInfoDto.of(refreshToken,deviceId)
        );
    }

    private String generateDeviceId() {
        return UUID.randomUUID().toString();
    }

}
