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

@Getter
@ToString
public class ShareToken {

    public static final int EXPIRED_AFTER = 10; // 10분 후 만료

    private final String token;
    private final Key key;
    private final LocalDateTime expiredAt;

    public ShareToken(String token, Key key) {
        this.token = token;
        this.key = key;
        this.expiredAt = extractExpirationDateFromToken(token); // 토큰에서 만료 일자 추출
    }

    public ShareToken(Long userId, Key key) {
        this.key = key;
        this.expiredAt = LocalDateTime.now().plusMinutes(EXPIRED_AFTER);
        this.token = createJwtShareToken(userId);
    }

    private String createJwtShareToken(Long userId) {
        Date expiredDate = Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("iss", "skeleton")
                .setIssuedAt(new Date())
                .setExpiration(expiredDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT 디코딩
    public Claims getData() {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private LocalDateTime extractExpirationDateFromToken(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Date expirationDate = claims.getExpiration();
        return expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}

