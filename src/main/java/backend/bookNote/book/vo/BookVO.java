package backend.bookNote.book.vo;

import backend.bookNote.book.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
 * items 에 들어갈 VO
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BookVO {
	private String title;
	private String link;
	private String image;
	private String author;
	private String discount;
	private String publisher;
	private String pubdate;
	private String isbn;
	private String description;

	public Book toBookEntity() {
		return Book.builder()
				.title(title)
				.link(link)
				.author(author)
				.discount(discount)
				.publisher(publisher)
				.pubdate(pubdate)
				.isbn(isbn)
				.description(description).build();
	}
}