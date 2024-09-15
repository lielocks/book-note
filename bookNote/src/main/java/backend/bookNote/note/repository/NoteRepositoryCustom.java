package backend.bookNote.note.repository;

import backend.bookNote.note.domain.Note;

import java.util.List;

public interface NoteRepositoryCustom {
    List<Note> getNoteList(Long userId);
}
