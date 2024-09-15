package backend.bookNote.auth.service;

import backend.bookNote.auth.model.ActiveUser;
import backend.bookNote.auth.repository.ActiveUserRepository;
import backend.bookNote.auth.token.AccessToken;
import backend.bookNote.auth.model.UserCustom;
import backend.bookNote.auth.token.RefreshToken;
import backend.bookNote.auth.token.ShareToken;
import backend.bookNote.common.exception.CustomError;
import backend.bookNote.common.exception.CustomException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService{

    @Value("${jwt.accessToken.secretKey}")
    private String accessTokenKey;

    @Value("${jwt.refreshToken.secretKey}")
    private String refreshTokenKey;

    private final ActiveUserRepository activeUserRepository;

    private Key accessKey;

    @PostConstruct
    public void init() {
        this.accessKey = new SecretKeySpec(accessTokenKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    public AccessToken convertAccessToken(String token) {
        return new AccessToken(token, accessKey);
    }

    public void setAuthentication(Long userId, String nickname) {
        UserCustom userCustom = new UserCustom(userId, nickname);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userCustom, null, Collections.singleton(new SimpleGrantedAuthority("USER")));
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    public AccessToken generateAccessToken(Long userId, String username) {
        return new AccessToken(userId, username, accessKey);
    }

    /**
     * Refresh Token 생성
     * @return
     */
    public RefreshToken generateRefreshToken(Long userId, String username) {
        RefreshToken refreshToken = new RefreshToken();

        // Refresh token Redis에 저장
        ActiveUser activeUser = new ActiveUser(userId, username, refreshToken);
        activeUserRepository.save(activeUser);

        return refreshToken;
    }

    public AccessToken refreshAccessToken(String refreshToken) {
        ActiveUser activeUser = activeUserRepository.findById(refreshToken)
                .orElseThrow(() -> new CustomException(CustomError.REFRESH_TOKEN_INVALID));

        if (activeUser.getExpiredAt().isBefore(LocalDateTime.now())) {
            // refresh token 만료
            activeUserRepository.deleteById(refreshToken); // Redis에서 삭제
            throw new CustomException(CustomError.REFRESH_TOKEN_EXPIRED);
        }

        return generateAccessToken(activeUser.getId(), activeUser.getNickname());
    }

    public void deleteRefreshToken(String refreshToken) {
        activeUserRepository.deleteById(refreshToken);
    }

    @Override
    public ShareToken generateShareToken(Long userId) {
        return new ShareToken(userId, accessKey);
    }

    @Override
    public Long validateShareToken(String token) {
        ShareToken shareToken = new ShareToken(token, accessKey);

        // 토큰 만료 여부 확인
        if (shareToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new CustomException(CustomError.SHARE_TOKEN_EXPIRED);
        }

        return Long.valueOf(shareToken.getData().getSubject());
    }

}
