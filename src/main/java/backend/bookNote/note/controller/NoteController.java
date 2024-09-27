package backend.bookNote.note.controller;

import backend.bookNote.auth.model.UserCustom;
import backend.bookNote.note.dto.NoteRegisterRequestDto;
import backend.bookNote.note.dto.NoteResponseDto;
import backend.bookNote.note.dto.NoteSoftDeleteDto;
import backend.bookNote.note.dto.NoteUpdateRequestDto;
import backend.bookNote.note.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/note")
public class NoteController {

    private final NoteService noteService;

    @MutationMapping(name = "registerNote")
    public NoteResponseDto registerNote(@AuthenticationPrincipal UserCustom userCustom, @Argument("NoteRegisterInput") NoteRegisterRequestDto requestDto) {
        return noteService.registerNote(userCustom.getId(), requestDto);
    }

    @MutationMapping(name = "updateNote")
    public NoteResponseDto updateNote(@AuthenticationPrincipal UserCustom userCustom, @Argument("NoteUpdateInput") NoteUpdateRequestDto requestDto) {
        return noteService.updateNoteContent(userCustom.getId(), requestDto);
    }

    @MutationMapping(name = "softDeleteNote")
    public NoteResponseDto softDeleteNote(@Argument("NoteSoftDeleteDto") NoteSoftDeleteDto requestDto) {
        return noteService.softDeleteNote(requestDto);
    }

    @MutationMapping(name = "hardDeleteNote")
    public String hardDeleteNote(@AuthenticationPrincipal UserCustom userCustom, @Argument Long noteId) {
        return noteService.hardDeleteNote(userCustom.getId(), noteId);
    }

    @QueryMapping(name = "getNoteListByUserId")
    public List<NoteResponseDto> getNoteListByUserId(@AuthenticationPrincipal UserCustom userCustom) {
        return noteService.getNoteListByUserId(userCustom.getId());
    }

}
