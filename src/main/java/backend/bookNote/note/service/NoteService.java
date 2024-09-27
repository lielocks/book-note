package backend.bookNote.note.service;

import backend.bookNote.book.domain.UserBook;
import backend.bookNote.book.repository.UserBookRepository;
import backend.bookNote.common.annotation.ValidateUserAccess;
import backend.bookNote.common.exception.CustomError;
import backend.bookNote.common.exception.CustomException;
import backend.bookNote.note.domain.Note;
import backend.bookNote.note.dto.NoteRegisterRequestDto;
import backend.bookNote.note.dto.NoteResponseDto;
import backend.bookNote.note.dto.NoteSoftDeleteDto;
import backend.bookNote.note.dto.NoteUpdateRequestDto;
import backend.bookNote.note.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final UserBookRepository userBookRepository;
    private final NoteRepository noteRepository;

    @Transactional
    public NoteResponseDto registerNote(Long userId, NoteRegisterRequestDto requestDto) {
        UserBook userBook = userBookRepository.findByBookIdAndUserId(requestDto.getBookId(), userId)
                .orElseThrow(() -> new CustomException(CustomError.USERBOOK_NOT_FOUND));

        Note note = Note.builder()
                .userBook(userBook)
                .content(requestDto.getContent())
                .isDeleted(false)
                .build();

        noteRepository.save(note);

        return note.toResponseDto();
    }

    @Transactional
    public NoteResponseDto updateNoteContent(Long userId, NoteUpdateRequestDto requestDto) {
        Note note = findNoteAndValidateUser(requestDto.getNoteId(), userId);
        note.updateContent(requestDto.getContent());

        noteRepository.save(note);

        return note.toResponseDto();
    }

    @ValidateUserAccess
    @Transactional
    public NoteResponseDto softDeleteNote(NoteSoftDeleteDto softDeleteDto) {
        Note note = noteRepository.findByIdIncludingDeleted(softDeleteDto.getNoteId())
                .orElseThrow(() -> new CustomException(CustomError.NOTE_NOT_FOUND));

        note.setIsDeleted(softDeleteDto.isDeleted());
        noteRepository.save(note);

        return note.toResponseDto();
    }


    @Transactional
    public String hardDeleteNote(Long userId, Long noteId) {
        Note note = noteRepository.findByIdIncludingDeleted(noteId)
                .orElseThrow(() -> new CustomException(CustomError.NOTE_NOT_FOUND));
        validateUserAccess(note, userId);

        // Soft delete된 데이터만 삭제
        if (Boolean.FALSE.equals(note.getIsDeleted())) {
            throw new CustomException(CustomError.CANNOT_DELETE_ACTIVE_NOTE);
        }

        noteRepository.hardDeleteNoteById(noteId);
        return "delete success : " + noteId;
    }

    @Transactional(readOnly = true)
    public List<NoteResponseDto> getNoteListByUserId(Long userId) {
        List<Note> notes = noteRepository.getNoteList(userId);
        return notes.stream()
                .map(Note::toResponseDto)
                .toList();
    }

    private Note findNoteAndValidateUser(Long noteId, Long userId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new CustomException(CustomError.NOTE_NOT_FOUND));
        validateUserAccess(note, userId);
        return note;
    }

    /**
     * 사용자 권한 검증 체크
     * @param note
     * @param userId
     */
    private void validateUserAccess(Note note, Long userId) {
        if (!userId.equals(note.getUserBook().getUser().getUserId())) {
            throw new CustomException(CustomError.ACCESS_TOKEN_INVALID);
        }
    }
}
