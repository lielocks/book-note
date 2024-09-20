package backend.bookNote.note.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NoteSoftDeleteDto {
    private Long userId;
    private Long noteId;
    private boolean deleted;
}
