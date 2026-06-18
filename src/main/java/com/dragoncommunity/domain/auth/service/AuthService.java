package com.dragoncommunity.domain.auth.service;

import com.dragoncommunity.common.exception.ApplicationException;
import com.dragoncommunity.common.security.util.PasswordEncoder;
import com.dragoncommunity.domain.auth.dto.*;
import com.dragoncommunity.domain.auth.dto.request.DeleteAuthTokenRequestDto;
import com.dragoncommunity.domain.auth.dto.request.ReissueTokenRequestDto;
import com.dragoncommunity.domain.auth.dto.request.SignInRequestDto;
import com.dragoncommunity.domain.auth.dto.response.SignInResponseDto;
import com.dragoncommunity.domain.auth.jwt.JwtProvider;
import com.dragoncommunity.domain.auth.model.RefreshTokens;
import com.dragoncommunity.domain.auth.repository.RefreshTokensRepository;
import com.dragoncommunity.domain.user.model.Users;
import com.dragoncommunity.domain.user.repository.UsersImagesRepository;
import com.dragoncommunity.domain.user.repository.UsersRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.dragoncommunity.common.exception.enums.ApplicationErrorCode.*;

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
     * 인증 토큰 생성(로그인) 기능 구현
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
    public SignInResultDto createAuthToken(@Valid SignInRequestDto signInRequestDto) {
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

        clearOldestSessionIfExceeded(user);

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

    /**
     * 토큰 재발행
     * 1. 리프레시 토큰 검증
     * 2. 리프레시 토큰을 DB에서 가져온다.
     *  2-1. 없다면, 해당 토큰은 탈취 가능성이 있으므로 해당 토큰으로 발행된(device id로 조회)리프레시 토큰 삭제 후 에러 처리
     *  2-2. 클라이언트의 device id 쿠키의 값과 DB의 device id 값이 다른 경우 비정상이므로 삭제 후 에러 처리
     * 3. 새 엑세스 토큰 생성
     * 4. 새 리프레시 토큰 생성
     * 5. DB에 있는 기존 리프레시 토큰 삭제
     * 6. 새 리프레시 토큰 DB에 저장
     */
    @Transactional
    public ReissueTokenResult reissueAuthToken(@Valid ReissueTokenRequestDto reissueTokenRequestDto) {
        jwtProvider.parse(reissueTokenRequestDto.refreshToken());

        RefreshTokens savedRefreshToken = refreshTokensRepository
                .findByRefreshToken(reissueTokenRequestDto.refreshToken());

        if (savedRefreshToken == null){
            refreshTokensRepository.deleteByDeviceId(reissueTokenRequestDto.deviceId());
            throw new ApplicationException(AUTHORIZATION_HEADER_MISSING_OR_INVALID);
        }

        if(!savedRefreshToken.getDeviceId().equals(reissueTokenRequestDto.deviceId())) {
            refreshTokensRepository.deleteByRefreshToken(reissueTokenRequestDto.refreshToken());
            throw new ApplicationException(AUTHORIZATION_HEADER_MISSING_OR_INVALID);
        }

        Users user= savedRefreshToken.getUser();


        String newAccessToken = jwtProvider.createAccessToken(
                user.getUserId(),
                user.getEmail(),
                user.getNickname()
        );

        String newRefreshToken = jwtProvider.createRefreshToken(user.getUserId());

        LocalDateTime refreshTokenExpiredAt = jwtProvider.getExpiration(newRefreshToken);

        refreshTokensRepository.delete(savedRefreshToken);
        refreshTokensRepository.save(
                RefreshTokens.createRefreshToken(
                        newRefreshToken,
                        reissueTokenRequestDto.deviceId(),
                        refreshTokenExpiredAt,
                        user
                )
        );

        return ReissueTokenResult.of(
                AccessTokenInfoDto.of(
                        newAccessToken,jwtProvider.getAccessTokenValidityInMilliseconds()
                ),
                RefreshTokenInfoDto.of(
                        newRefreshToken, reissueTokenRequestDto.deviceId()
                )
        );
    }

    /**
     * 로그아웃
     * 1. DB 에서 RefreshToken 삭제
     */
    public void deleteAuthToken(@Valid DeleteAuthTokenRequestDto deleteAuthTokenRequestDto){
        RefreshTokens savedRefreshToken = refreshTokensRepository
                .findByRefreshToken(deleteAuthTokenRequestDto.refreshToken());

        refreshTokensRepository.delete(savedRefreshToken);
    }


    private String generateDeviceId() {
        return UUID.randomUUID().toString();
    }

    private void clearOldestSessionIfExceeded(Users user){
        List<RefreshTokens> tokens = refreshTokensRepository.findByUserOrderByCreatedAtAsc(user);

        if (tokens.size() >= 5) {
            RefreshTokens oldest = tokens.get(0);
            refreshTokensRepository.delete(oldest);
        }
    }

}
