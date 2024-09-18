package backend.bookNote.note.repository;

import backend.bookNote.note.domain.Note;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static backend.bookNote.note.domain.QNote.note;


@Repository
@RequiredArgsConstructor
public class NoteRepositoryImpl implements NoteRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Note> getNoteList(Long userId) {
        return queryFactory.selectFrom(note)
                .where(note.userBook.user.userId.eq(userId))
                .orderBy(note.createdAt.desc())
                .fetch();
    }
}
