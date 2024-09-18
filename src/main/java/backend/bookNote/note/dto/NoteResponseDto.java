package backend.bookNote.note.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NoteResponseDto {
    private Long noteId;
    private String content;
    private boolean isDeleted;
    private String createdAt;
    private String updatedAt;
    private Long bookId;
}
