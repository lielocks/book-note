package backend.bookNote.book.service;

import backend.bookNote.auth.service.TokenService;
import backend.bookNote.auth.token.ShareToken;
import backend.bookNote.book.domain.Book;
import backend.bookNote.book.domain.UserBook;
import backend.bookNote.book.dto.BookListResponseDto;
import backend.bookNote.book.dto.UserBookListResponseDto;
import backend.bookNote.book.dto.UserLikeBookDto;
import backend.bookNote.book.repository.BookRepository;
import backend.bookNote.book.repository.UserBookRepository;
import backend.bookNote.book.vo.BookVO;
import backend.bookNote.book.vo.NaverResultVO;
import backend.bookNote.common.annotation.ValidateUserAccess;
import backend.bookNote.common.exception.CustomError;
import backend.bookNote.common.exception.CustomException;
import backend.bookNote.note.domain.Note;
import backend.bookNote.user.domain.User;
import backend.bookNote.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final UserBookRepository userBookRepository;
    private final TokenService tokenService;

    @Value("${naver.clientId}")
    private String clientId;

    @Value("${naver.secret}")
    private String clientSecret;

    @Transactional
    public Book saveBook(Book book, Long userId) {
        Book savedBook = bookRepository.findByIsbn(book.getIsbn()).orElse(bookRepository.save(book));
        User user = userRepository.findByUserId(userId);

        userBookRepository.save(UserBook.builder()
                .user(user)
                .book(savedBook)
                .build());
        return savedBook;
    }

    public Book selectBookByIsbn(String isbn, Long userId) {
        // isbn 은 책의 고유 번호이기 때문에 1개의 Book 만 가져온다
        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/book.json")
                .queryParam("query", isbn)
                .encode()
                .build()
                .toUri();

        RequestEntity<Void> request = RequestEntity
                .get(uri)
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .build();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(request, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        NaverResultVO resultVO = null;

        try {
            resultVO = objectMapper.readValue(responseEntity.getBody(), NaverResultVO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (resultVO != null && !resultVO.getItems().isEmpty()) {
            BookVO selectedBook = resultVO.getItems().get(0);
            System.out.println("selected Book" + selectedBook.getDescription());

            // BookVO를 Book 엔티티로 변환하여 반환
            Book book = Book.builder()
                    .title(selectedBook.getTitle())
                    .link(selectedBook.getLink())
                    .author(selectedBook.getAuthor())
                    .discount(selectedBook.getDiscount())
                    .publisher(selectedBook.getPublisher())
                    .pubdate(selectedBook.getPubdate())
                    .isbn(selectedBook.getIsbn())
                    .description(selectedBook.getDescription())
                    .build();

            return saveBook(book, userId);
        }

        else throw new CustomException(CustomError.BOOK_NOT_FOUND);
    }

    @ValidateUserAccess
    @Transactional
    public String userBookLike(UserLikeBookDto userLikeBookDto) {
        UserBook userBook = userBookRepository.findByBookIdAndUserId(userLikeBookDto.getBookId(), userLikeBookDto.getUserId())
                .orElseThrow(() -> new CustomException(CustomError.USERBOOK_NOT_FOUND));

        userBook.isLiked();
        return "Liked Success";
    }

    @Transactional(readOnly = true)
    public List<BookListResponseDto> validateShareToken(String token) {
        Long userId = tokenService.validateShareToken(token);
        List<UserBook> userBookList = userBookRepository.findUserBooksByUser(userId);

        return userBookList.stream()
                .map(userBook -> {
                    Book book = userBook.getBook();
                    List<Note> notes = userBook.getNotes();
                    return book.toResponseDto(notes);
                })
                .toList();
    }


    public String generateUserShareToken(Long userId) {
        ShareToken shareToken = tokenService.generateShareToken(userId);
        return shareToken.getToken();
    }

    @Transactional(readOnly = true)
    public List<UserBookListResponseDto> getUserBookList(Long userId) {
        List<UserBook> userBookList = userBookRepository.findUserBooksByUser(userId);

        return userBookList.stream()
                .map(userBook -> {
                    Book book = userBook.getBook();
                    return book.toUserBookListDto();
                })
                .toList();
    }
}
