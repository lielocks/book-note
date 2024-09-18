package backend.bookNote.book.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class NaverResultVO {
	private String lastBuildDate;
	private int total;
	private int start;
	private int display;
	private List<BookVO> items;
}