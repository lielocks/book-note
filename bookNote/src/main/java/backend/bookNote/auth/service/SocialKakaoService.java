package backend.bookNote.auth.service;

import backend.bookNote.auth.dto.LoginDto;
import backend.bookNote.auth.dto.SocialKakaoDto;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface SocialKakaoService {

    LoginDto login(SocialKakaoDto.TokenRequest requestDto) throws Exception;

    SocialKakaoDto.TokenResponse getAccessToken(SocialKakaoDto.TokenRequest requestDto) throws Exception;

    SocialKakaoDto.UserInfo getUserInfo(String token) throws Exception;
}
