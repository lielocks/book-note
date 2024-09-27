package backend.bookNote.note.dto;

import backend.bookNote.common.asepct.HasUserId;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NoteSoftDeleteDto implements HasUserId {
    private Long userId;
    private Long noteId;
    private boolean deleted;

    @Override
    public Long getUserId() {
        return userId;
    }
}
