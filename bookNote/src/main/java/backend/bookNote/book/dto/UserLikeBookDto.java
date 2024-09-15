package backend.bookNote.book.dto;

import lombok.Data;

@Data
public class UserLikeBookDto {
    private Long userId;
    private Long bookId;
}
