package cafe.ideation.ui;

import cafe.ideation.service.NoteService;
import javax.swing.*;
import java.awt.*;

public class WelcomePanel extends JPanel {
    public WelcomePanel(NoteService noteService, Runnable onCreateNote) {
        setLayout(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome! Create your first note.", SwingConstants.CENTER);
        JButton createNoteButton = new JButton("Create Note");
        createNoteButton.addActionListener(e -> {
            noteService.createNote();
            onCreateNote.run();
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createNoteButton);
        add(welcomeLabel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
