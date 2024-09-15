package backend.bookNote.book.repository;

import backend.bookNote.book.domain.UserBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserBookRepository extends JpaRepository<UserBook, Long> {
    @Query("SELECT ub FROM UserBook ub WHERE ub.book.id = :bookId AND ub.user.userId = :userId")
    Optional<UserBook> findByBookIdAndUserId(@Param("bookId") Long bookId, @Param("userId") Long userId);

    @Query("SELECT ub FROM UserBook ub WHERE ub.user.userId = :userId")
    List<UserBook> findUserBooksByUser(@Param("userId") Long userId);
}
