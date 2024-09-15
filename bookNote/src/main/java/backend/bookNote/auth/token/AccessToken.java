package backend.bookNote.auth.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.ToString;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
public class AccessToken {
    // 만료 시간(분)
    public static final int EXPIRED_AFTER = 15; // 15분

    // 암호화된 token
    private final String token;

    // key
    private final Key key;

    // 만료 일자
    private LocalDateTime expiredAt;

    // AccessToken 생성자 (발급 시)
    // User 가 아니라 User Principal 로 만들도록 수정
    public AccessToken(Long id, String nickname, Key key) {
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(EXPIRED_AFTER);
        Date expiredDate = Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant());

        // claims 만들기
        Map<String, Object> claims = new HashMap<>();

        claims.put("iss", "skeleton"); // 발행인
        claims.put("aud", id); // 토큰 대상자(User PK)
        claims.put("exp", LocalDateTime.now().toString()); // 발행 시간

        this.key = key;
        this.expiredAt = expiredAt;
        this.token = createJwtAuthToken(nickname, claims, expiredDate);
    }

    // JWT로 만드는 메소드
    public String createJwtAuthToken(String nickname, Map<String, Object> claimMap, Date expiredDate) {
        return Jwts.builder()
                .setSubject(nickname)
                .addClaims(claimMap)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiredDate)
                .compact();
    }

    // 이하는 AccessToken 유효성 검증에 사용
    public AccessToken(String token, Key key) {
        this.token = token;
        this.key = key;
    }

    // JWT 디코딩
    public Claims getData() {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}