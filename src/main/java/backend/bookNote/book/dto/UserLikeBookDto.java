package backend.bookNote.book.dto;

import backend.bookNote.common.asepct.HasUserId;
import lombok.Data;

@Data
public class UserLikeBookDto implements HasUserId {
    private Long userId;
    private Long bookId;

    @Override
    public Long getUserId() {
        return userId;
    }
}
