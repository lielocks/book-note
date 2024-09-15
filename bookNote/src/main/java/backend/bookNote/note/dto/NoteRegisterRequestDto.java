package backend.bookNote.note.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class NoteRegisterRequestDto {
    private Long bookId;
    private String content;
}

