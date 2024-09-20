package backend.bookNote.book.controller;

import backend.bookNote.auth.model.UserCustom;
import backend.bookNote.book.domain.Book;
import backend.bookNote.book.dto.BookListResponseDto;
import backend.bookNote.book.dto.UserBookListResponseDto;
import backend.bookNote.book.dto.UserLikeBookDto;
import backend.bookNote.book.service.BookService;
import backend.bookNote.book.service.NaverSearchService;
import backend.bookNote.book.vo.NaverResultVO;
import backend.bookNote.note.dto.NoteResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class BookController {

    private final NaverSearchService naverSearchService;
    private final BookService bookService;

    @GetMapping("/search")
    public NaverResultVO searchBook(@RequestParam String keyword) {
        return naverSearchService.list(keyword);
    }

    @MutationMapping(name = "fetchAndSaveBook")
    public Book fetchAndSaveBook(@AuthenticationPrincipal UserCustom userCustom, @Argument String isbn) {
        return bookService.selectBookByIsbn(isbn, userCustom.getId());
    }

    @MutationMapping(name = "userLikeBook")
    public String userLikeBook(@AuthenticationPrincipal UserCustom userCustom, @Argument(name = "UserLikeBookInput") UserLikeBookDto userLikeBookDto) {
        return bookService.userBookLike(userCustom.getId(), userLikeBookDto);
    }

    @PostMapping("/share")
    public String generateShareToken(@AuthenticationPrincipal UserCustom userCustom) {
        return bookService.generateUserShareToken(userCustom.getId());
    }

    @GetMapping("/valid")
    public List<BookListResponseDto> validateShareToken(@RequestHeader("token") String token) {
        return bookService.validateShareToken(token);
    }

    @QueryMapping(name = "getUserBookList")
    public List<UserBookListResponseDto> getUserBookList(@AuthenticationPrincipal UserCustom userCustom) {
        return bookService.getUserBookList(userCustom.getId());
    }
}
