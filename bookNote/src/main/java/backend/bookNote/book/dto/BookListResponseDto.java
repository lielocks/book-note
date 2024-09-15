package backend.bookNote.book.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookListResponseDto {
    private Long id;
    private String title;
    private String link;
    private String author;
    private String discount;
    private String publisher;
    private String description;
    private List<String> noteContents;

}
