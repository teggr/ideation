package cafe.ideation.ui;

import cafe.ideation.service.NoteApplicationService;
import javax.swing.*;
import java.awt.*;

public class WelcomePanel extends JPanel {
    public WelcomePanel(NoteApplicationService noteApplicationService, Runnable onCreateNote) {
        setLayout(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome! Create your first note.", SwingConstants.CENTER);
        JButton createNoteButton = new JButton("Create Note");
        createNoteButton.addActionListener(e -> {
            noteApplicationService.createNote();
            onCreateNote.run();
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createNoteButton);
        add(welcomeLabel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
