package backend.bookNote.auth.model;

import backend.bookNote.auth.token.RefreshToken;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

/**
 * Redis 저장용 엔티티
 */
@Getter
@RedisHash(value = "activeUser", timeToLive = 14400) // 4시간 저장
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActiveUser {
    @Id
    private String refreshToken;

    private LocalDateTime expiredAt; // 만료 시각

    private Long id; // user pk

    private String nickname;

    private LocalDateTime createdAt;

    public ActiveUser(Long id, String nickname, RefreshToken refreshToken) {
        this.id = id;
        this.nickname = nickname;

        this.refreshToken = refreshToken.getToken();
        this.expiredAt = refreshToken.getExpiredAt();

        this.createdAt = LocalDateTime.now();
    }
}
