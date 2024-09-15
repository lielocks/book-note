package backend.bookNote.auth.service;

import backend.bookNote.auth.dto.LoginDto;
import backend.bookNote.auth.dto.SocialKakaoDto;
import backend.bookNote.auth.token.AccessToken;
import backend.bookNote.auth.token.RefreshToken;
import backend.bookNote.common.exception.CustomError;
import backend.bookNote.common.exception.CustomException;
import backend.bookNote.user.domain.User;
import backend.bookNote.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocialKakaoServiceImpl implements SocialKakaoService {

    private final UserService userService;
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;

    @Value("${kakao.url.token}")
    private String tokenUrl;

    @Value("${kakao.url.profile}")
    private String profileUrl;

    @Value("${kakao.url.clientId}")
    private String clientId;

    @Value("${kakao.url.clientSecret}")
    private String clientSecret;

    @Override
    public LoginDto login(SocialKakaoDto.TokenRequest requestDto) throws Exception {

        // Access Token 발급
        SocialKakaoDto.TokenResponse tokenResponseDto = getAccessToken(requestDto);

        // Access Token 으로 user 정보 조회
        SocialKakaoDto.UserInfo userInfo = getUserInfo(tokenResponseDto.getAccess_token());

        // User 정보 없으면 User create 먼저 진행
        User user = userService.findByUserUid(userInfo.getId());
        if (user == null) {
            user = userService.createUser(userInfo);
        }

        // Jwt Token 발급
        // Access Token user id 로 발급
        AccessToken accessToken = tokenService.generateAccessToken(user.getUserId(), user.getNickname());
        RefreshToken refreshToken = tokenService.generateRefreshToken(user.getUserId(), user.getNickname());

        return LoginDto.builder()
                .userId(user.getUserId())
                .accessToken(accessToken.getToken())
                .refreshToken(refreshToken.getToken())
                .nickname(user.getNickname())
                .build();
    }

    /**
     * Kakao Token Url 에 요청하여 Access Token 발급
     * @param requestDto
     * @return
     */
//    @Override
//    public SocialKakaoDto.TokenResponse getAccessToken(SocialKakaoDto.TokenRequest requestDto) throws Exception {
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
//
//        // header 설정
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.add("Accept", "application/json");
//
//        // Kakao Uri param 설정
//        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
//
//        paramMap.add("client_id", clientId);
//        paramMap.add("redirect_uri", requestDto.getRedirectUri());
//        paramMap.add("client_secret", clientSecret);
//        paramMap.add("code", requestDto.getCode());
//        paramMap.add("grant_type", "authorization_code");
//
//        // Http 요청
//        ResponseEntity<String> responseEntity;
//
//        try {
//            responseEntity = restTemplate.postForEntity(tokenUrl, new HttpEntity<>(paramMap, headers), String.class);
//        } catch (HttpClientErrorException e) {
//            log.error("Kakao Token Url Error :: ", e);
//            throw new CustomException(CustomError.AUTH_TOKEN_CREATE_FAIL);
//        }
//
//        return objectMapper.readValue(responseEntity.getBody(), SocialKakaoDto.TokenResponse.class);
//    }

    @Override
    public SocialKakaoDto.TokenResponse getAccessToken(SocialKakaoDto.TokenRequest requestDto) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        // header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Accept", "application/json");

        // Kakao Uri param 설정
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("client_id", clientId);
        paramMap.add("redirect_uri", requestDto.getRedirectUri());
        paramMap.add("client_secret", clientSecret);
        paramMap.add("code", requestDto.getCode());
        paramMap.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(paramMap, headers);

        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange(
                    tokenUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );
        } catch (HttpClientErrorException e) {
            log.error("Kakao Token Url Error :: ", e);
            throw new CustomException(CustomError.AUTH_TOKEN_CREATE_FAIL);
        }

        return objectMapper.readValue(responseEntity.getBody(), SocialKakaoDto.TokenResponse.class);
    }


    /**
     * Kakao Profile Url 에 Access Token 으로 유저 정보 조회
     * @param accessToken
     * @return
     */
//    @Override
//    public SocialKakaoDto.UserInfo getUserInfo(String accessToken) throws Exception {
//
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
//
//        // header 설정
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + accessToken);
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.add("Accept", "application/json");
//
//        // Kakao Profile Uri param 설정
//        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
//
//        // Http 요청
//        ResponseEntity<String> responseEntity;
//
//        try {
//            responseEntity = restTemplate.postForEntity(profileUrl, new HttpEntity<>(paramMap, headers), String.class);
//        } catch (HttpClientErrorException e) {
//            log.error("",e);
//            throw new CustomException(CustomError.AUTH_TOKEN_CREATE_FAIL);
//        }
//
//        return objectMapper.readValue(responseEntity.getBody(), SocialKakaoDto.UserInfo.class);
//    }
    @Override
    public SocialKakaoDto.UserInfo getUserInfo(String accessToken) throws Exception {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Accept", "application/json");

        // Create HttpEntity with headers (no body needed)
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(headers);

        // Http 요청 using RestTemplate.exchange
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange(
                    profileUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );
        } catch (HttpClientErrorException e) {
            log.error("Kakao Profile Url Error :: ", e);
            throw new CustomException(CustomError.AUTH_TOKEN_CREATE_FAIL);
        }

        return objectMapper.readValue(responseEntity.getBody(), SocialKakaoDto.UserInfo.class);
    }

}
