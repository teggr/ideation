package cafe.ideation.service;

import cafe.ideation.model.Note;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class NoteService {
    private final List<Note> notes = new ArrayList<>();

    public List<Note> listNotes() {
        return Collections.unmodifiableList(notes);
    }

    public void addNote(Note note) {
        notes.add(note);
    }

    public int getNoteCount() {
        return notes.size();
    }
}
