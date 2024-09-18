package backend.bookNote.auth.dto;

import backend.bookNote.user.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import static backend.bookNote.user.domain.User.Role.ROLE_USER;

@Getter
@Setter
public class SocialKakaoDto {

    /**
     * Access Token 요청시 사용
     */
    @Getter
    @Setter
    public static class TokenRequest {
        private String code;
        private String redirectUri;
    }

    /**
     * Access Token 결과 파싱
     */
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TokenResponse {
        private String access_token;
        private String refresh_token;
    }

    /**
     * Access Token 으로 카카오 유저 정보 조회시 사용
     */
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserInfo {
        private String id; //uid
        private Properties properties;

        @Getter
        public static class Properties {
            private String nickname; // kakao profile_nickname 정보 가져옴
        }

        //UserInfo -> User Entity
        public User createUser(){
            String nickname = (properties != null) ? properties.getNickname() : null;

            User user = User.builder().userUid(id).nickname(nickname).role(ROLE_USER).build();
            return user;
        }
    }
}
