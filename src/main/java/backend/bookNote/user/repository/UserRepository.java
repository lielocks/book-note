package backend.bookNote.user.repository;

import backend.bookNote.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserUid(String userUid);
    User findByUserId(Long id);
}
