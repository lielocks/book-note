package backend.bookNote.note.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class NoteUpdateRequestDto {
    private Long noteId;
    private String content;
}
