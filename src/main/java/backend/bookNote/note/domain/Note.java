package backend.bookNote.note.domain;

import backend.bookNote.book.domain.UserBook;
import backend.bookNote.common.model.BaseTimeEntity;
import backend.bookNote.note.dto.NoteResponseDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.format.DateTimeFormatter;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE note SET is_deleted = true WHERE note_id = ?")
@Where(clause = "is_deleted = false")
public class Note extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_book_id")
    private UserBook userBook;

    @Column(length = 1000)
    private String content;

    private Boolean isDeleted = false; // Soft delete 플래그

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public NoteResponseDto toResponseDto() {
        return NoteResponseDto.builder()
                .noteId(noteId)
                .content(content)
                .isDeleted(isDeleted)
                .createdAt(getCreatedAt().format(formatter))
                .updatedAt(getUpdatedAt().format(formatter))
                .bookId(userBook.getBook().getId())
                .build();
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void setIsDeleted() {
        this.isDeleted = true;
    }
}
