package cafe.ideation.ui;

import cafe.ideation.model.Note;
import javax.swing.*;
import java.awt.*;

public class NotePanel extends JPanel {
    private final Note note;
    private final JTextArea contentArea;

    public NotePanel(Note note) {
        this.note = note;
        setLayout(new BorderLayout());
        contentArea = new JTextArea(note.getContent());
        add(new JScrollPane(contentArea), BorderLayout.CENTER);
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel(getTabTitle()), BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
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
}
