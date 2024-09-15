package backend.bookNote.auth.service;

import backend.bookNote.auth.dto.AuthTokens;
import backend.bookNote.auth.token.AccessToken;
import backend.bookNote.auth.token.RefreshToken;
import backend.bookNote.common.exception.CustomError;
import backend.bookNote.common.exception.CustomException;
import backend.bookNote.auth.dto.LoginDto;
import backend.bookNote.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void logOut(String refreshToken) {
        if(!StringUtils.hasText(refreshToken)) {
            throw new CustomException(CustomError.REFRESH_TOKEN_INVALID);
        }

        tokenService.deleteRefreshToken(refreshToken);
    }

    /**
     * Refresh Token 으로 Access Token 재발급 Reissue
     * refresh token 유효성 체크 ->
     * new Access, Refresh Token 생성 ->
     * Redis 에서 기존 Refresh Token 삭제 후 new Refresh Token 저장
     *
     * @param refreshToken
     * @return
     */
    @Override
    @Transactional
    public LoginDto tokenRefresh(String refreshToken) {

        AccessToken newAccessToken = tokenService.refreshAccessToken(refreshToken);
        Long userId = Long.valueOf(newAccessToken.getData().get("aud").toString());
        String nickname = newAccessToken.getData().getSubject();

        RefreshToken newRefreshToken =tokenService.generateRefreshToken(userId, nickname);

        return LoginDto.builder()
                .userId(userId)
                .accessToken(newAccessToken.getToken())
                .refreshToken(newRefreshToken.getToken())
                .nickname(nickname)
                .build();
    }



}