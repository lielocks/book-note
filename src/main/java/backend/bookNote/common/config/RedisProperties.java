package backend.bookNote.common.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties("spring.redis")
public class RedisProperties {
    private String host;
    private int port;
}
