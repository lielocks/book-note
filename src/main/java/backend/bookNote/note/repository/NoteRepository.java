package backend.bookNote.note.repository;

import backend.bookNote.note.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long>, NoteRepositoryCustom {
    @Query(value = "SELECT * FROM note WHERE note_id = :noteId", nativeQuery = true)
    Optional<Note> findByIdIncludingDeleted(@Param("noteId") Long noteId);

    @Modifying
    @Query(value = "DELETE FROM note WHERE note_id = :noteId", nativeQuery = true)
    void hardDeleteNoteById(@Param("noteId") Long noteId);

}
