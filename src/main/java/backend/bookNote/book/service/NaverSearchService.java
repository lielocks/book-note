package backend.bookNote.book.service;

import backend.bookNote.book.vo.NaverResultVO;

public interface NaverSearchService {
    NaverResultVO list(String keyword);
}
