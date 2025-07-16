package cafe.ideation.service;

import cafe.ideation.model.Note;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteApplicationService {
    private final NoteService noteService;
    private final ApplicationEventPublisher eventPublisher;

    public NoteApplicationService(NoteService noteService, ApplicationEventPublisher eventPublisher) {
        this.noteService = noteService;
        this.eventPublisher = eventPublisher;
    }

    public List<Note> listNotes() {
        List<Note> notes = noteService.listNotes();
        eventPublisher.publishEvent(new NotesListedEvent(this, notes));
        return notes;
    }

    public Note createNote() {
        Note note = noteService.createNote();
        eventPublisher.publishEvent(new NoteCreatedEvent(this, note));
        return note;
    }

    public void deleteNote(Note note) {
        noteService.deleteNote(note);
        eventPublisher.publishEvent(new NoteDeletedEvent(this, note));
    }

    public void editNote(Note note) {
        noteService.editNote(note);
        eventPublisher.publishEvent(new NoteEditedEvent(this, note));
    }

    public void saveNote(Note note, String newContent) {
        noteService.saveNote(note, newContent);
        eventPublisher.publishEvent(new NoteSavedEvent(this, note));
    }

    public int getNoteCount() {
        return noteService.getNoteCount();
    }

    // Event classes
    public static class NoteCreatedEvent {
        public final Object source;
        public final Note note;
        public NoteCreatedEvent(Object source, Note note) { this.source = source; this.note = note; }
    }
    public static class NoteDeletedEvent {
        public final Object source;
        public final Note note;
        public NoteDeletedEvent(Object source, Note note) { this.source = source; this.note = note; }
    }
    public static class NoteEditedEvent {
        public final Object source;
        public final Note note;
        public NoteEditedEvent(Object source, Note note) { this.source = source; this.note = note; }
    }
    public static class NoteSavedEvent {
        public final Object source;
        public final Note note;
        public NoteSavedEvent(Object source, Note note) { this.source = source; this.note = note; }
    }
    public static class NotesListedEvent {
        public final Object source;
        public final List<Note> notes;
        public NotesListedEvent(Object source, List<Note> notes) { this.source = source; this.notes = notes; }
    }
}
