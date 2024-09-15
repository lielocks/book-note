package backend.bookNote.book.domain;

import backend.bookNote.book.dto.BookListResponseDto;
import backend.bookNote.note.domain.Note;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String link;
    private String author;
    private String discount;
    private String publisher;
    private String pubdate;
    private String isbn;

    @Column(length = 1000)
    private String description;

    public BookListResponseDto toResponseDto(List<Note> notes) {
        List<String> noteContents = notes.stream()
                .map(Note::getContent)
                .toList();

        return BookListResponseDto.builder()
                .id(id)
                .title(title)
                .author(author)
                .link(link)
                .description(description)
                .discount(discount)
                .publisher(publisher)
                .noteContents(noteContents)
                .build();
    }
}
