package backend.bookNote.auth.controller;

import backend.bookNote.auth.dto.LoginDto;
import backend.bookNote.auth.dto.SocialKakaoDto;
import backend.bookNote.auth.service.AuthService;
import backend.bookNote.auth.service.SocialKakaoService;
import backend.bookNote.common.constant.Constants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final SocialKakaoService socialKakaoService;

    /**
     * Kakao Social Login
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PostMapping("/login/kakao")
    public LoginDto loginKakao(@RequestBody SocialKakaoDto.TokenRequest request, HttpServletResponse response) throws Exception {

        LoginDto dto = socialKakaoService.login(request);
        log.info("User Logged In :: {}", dto.getUserId());

        ResponseCookie cookie = ResponseCookie.from(Constants.REFRESH_TOKEN_COOKIE_NAME, dto.getRefreshToken())
                .maxAge(Constants.REFRESH_TOKEN_TTL_SECONDS)
                .path("/")
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return dto;
    }

    /**
     * Token 재발급
     * @param refreshTokenCookie
     * @param response
     * @return
     */
    @PostMapping("/refresh")
    public LoginDto tokenRefresh(@CookieValue(Constants.REFRESH_TOKEN_COOKIE_NAME) Cookie refreshTokenCookie, HttpServletResponse response) {

        var token = authService.tokenRefresh(refreshTokenCookie.getValue());

        ResponseCookie cookie = ResponseCookie.from(Constants.REFRESH_TOKEN_COOKIE_NAME, token.getRefreshToken())
                .maxAge(Constants.REFRESH_TOKEN_TTL_SECONDS)
                .path("/")
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return token;
    }
}
