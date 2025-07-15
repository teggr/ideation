package cafe.ideation.ui;


import cafe.ideation.model.Note;
import cafe.ideation.service.NoteService;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.function.Consumer;


public class NotePanel extends JPanel {
    private final Note note;
    private final JTextArea contentArea;
    private final NoteService noteService;
    private boolean dirty = false;
    private final JLabel titleLabel;
    private final JButton saveButton;
    private Consumer<String> onTitleChanged;
    private Runnable onNoteSaved;

    public NotePanel(Note note, NoteService noteService) {
        this.note = note;
        this.noteService = noteService;
        setLayout(new BorderLayout());
        contentArea = new JTextArea(note.getContent());
        add(new JScrollPane(contentArea), BorderLayout.CENTER);

        // Top panel with title and Save button
        JPanel topPanel = new JPanel(new BorderLayout());
        titleLabel = new JLabel(getTabTitle());
        topPanel.add(titleLabel, BorderLayout.CENTER);
        saveButton = new JButton("Save");
        saveButton.setEnabled(false);
        saveButton.addActionListener(e -> saveNote());
        topPanel.add(saveButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Track changes for dirty state
        contentArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { setDirty(true); }
            @Override
            public void removeUpdate(DocumentEvent e) { setDirty(true); }
            @Override
            public void changedUpdate(DocumentEvent e) { setDirty(true); }
        });
    }


    public String getTabTitle() {
        return note.getTitle().isEmpty() ? "Untitled" : note.getTitle();
    }


    public JTextArea getContentArea() {
        return contentArea;
    }


    public Note getNote() {
        return note;
    }


    public void setOnTitleChanged(Consumer<String> onTitleChanged) {
        this.onTitleChanged = onTitleChanged;
    }

    public void setOnNoteSaved(Runnable onNoteSaved) {
        this.onNoteSaved = onNoteSaved;
    }

    private void setDirty(boolean dirty) {
        this.dirty = dirty;
        saveButton.setEnabled(dirty);
    }

    private void saveNote() {
        String newContent = contentArea.getText();
        String oldTitle = note.getTitle();
        noteService.saveNote(note, newContent);
        setDirty(false);
        // Update title label if changed
        String newTitle = getTabTitle();
        titleLabel.setText(newTitle);
        if (onTitleChanged != null && !oldTitle.equals(newTitle)) {
            onTitleChanged.accept(newTitle);
        }
        if (onNoteSaved != null) {
            onNoteSaved.run();
        }
    }
}
