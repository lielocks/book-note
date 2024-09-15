package backend.bookNote.auth.service;

import backend.bookNote.auth.token.AccessToken;
import backend.bookNote.auth.token.RefreshToken;
import backend.bookNote.auth.token.ShareToken;

public interface TokenService {
    AccessToken convertAccessToken(String token);
    void setAuthentication(Long userId, String username);
    AccessToken generateAccessToken(Long userId, String username);
    RefreshToken generateRefreshToken(Long userId, String username);
    AccessToken refreshAccessToken(String refreshToken);
    void deleteRefreshToken(String refreshToken);
    ShareToken generateShareToken(Long userId);
    Long validateShareToken(String token);
}