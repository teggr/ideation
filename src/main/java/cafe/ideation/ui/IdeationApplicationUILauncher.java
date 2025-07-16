package cafe.ideation.ui;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import cafe.ideation.service.NoteService;
import dev.rebelcraft.cli.App;

import javax.swing.*;
import java.awt.*;

import cafe.ideation.model.Note;
import cafe.ideation.service.AppDataDirectoryService;
import com.formdev.flatlaf.FlatLightLaf;

@Component
public class IdeationApplicationUILauncher implements CommandLineRunner {

    private final NoteService noteService;
    private final App app;
    private final AppDataDirectoryService appDataDirectoryService;

    private JFrame mainFrame;

    public IdeationApplicationUILauncher(App app,
            AppDataDirectoryService appDataDirectoryService, NoteService noteService) {
        this.app = app;
        this.appDataDirectoryService = appDataDirectoryService;
        this.noteService = noteService;
    }

    @Override
    public void run(String... args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
            } catch (Exception e) {
                System.err.println("Failed to initialize FlatLaf");
            }
            if (!appDataDirectoryService.isInitialised()) {
                JFrame promptFrame = new JFrame("Setup Application Directory");
                promptFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                promptFrame.setSize(400, 200);
                promptFrame.setLocationRelativeTo(null);
                AppDataDirectoryPrompt appDataDirectoryPrompt = new AppDataDirectoryPrompt(appDataDirectoryService);
                appDataDirectoryPrompt.promptForDirectory(promptFrame);
                if (!appDataDirectoryService.isInitialised()) {
                    JOptionPane.showMessageDialog(promptFrame,
                            "You must choose an application data directory to continue.");
                    promptFrame.dispose();
                    return;
                }
                promptFrame.dispose();
            }
            JFrame frame = new JFrame("Notes");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setLayout(new BorderLayout());

            JMenuBar menuBar = new JMenuBar();
            JMenu fileMenu = new JMenu("File");

            menuBar.add(fileMenu);
            frame.setJMenuBar(menuBar);

            if (noteService.getNoteCount() == 0) {
                WelcomePanel welcomePanel = new WelcomePanel(noteService, () -> {
                    frame.getContentPane().removeAll();
                    NotesMainPanel notesMainPanel = new NotesMainPanel(noteService);
                    frame.add(notesMainPanel, BorderLayout.CENTER);
                    frame.revalidate();
                    frame.repaint();
                });
                frame.add(welcomePanel, BorderLayout.CENTER);
            } else {
                NotesMainPanel notesMainPanel = new NotesMainPanel(noteService);
                frame.add(notesMainPanel, BorderLayout.CENTER);

                JMenuItem createNoteItem = new JMenuItem("Create Note");
                createNoteItem.addActionListener(e -> {
                    Note newNote = noteService.createNote();
                    notesMainPanel.showNote(newNote);
                });
                fileMenu.add(createNoteItem);
            }
            this.mainFrame = frame;
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    @EventListener
    public void onShutdown(ContextClosedEvent event) {
        // Dispose the main frame on Spring context shutdown
        if (mainFrame != null) {
            SwingUtilities.invokeLater(() -> {
                mainFrame.dispose();
            });
        }
    }
}
