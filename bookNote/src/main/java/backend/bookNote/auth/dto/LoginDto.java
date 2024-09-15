package backend.bookNote.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginDto {
    private String accessToken;

    @JsonIgnore
    private String refreshToken; // Cookie 로 set

    private Long userId;
    private String nickname;
}
