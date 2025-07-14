package cafe.ideation.ui;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import cafe.ideation.service.NoteService;
import dev.rebelcraft.cli.App;

import javax.swing.*;
import java.awt.*;
import cafe.ideation.service.AppDataDirectoryService;
import com.formdev.flatlaf.FlatLightLaf;

@Component
public class IdeationApplicationUILauncher implements CommandLineRunner {

    private final NoteService noteService;
    private final App app;
    private final AppDataDirectoryService appDataDirectoryService;

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
            frame.setSize(600, 400);
            frame.setLayout(new BorderLayout());
            if (noteService.getNoteCount() == 0) {
                WelcomePanel welcomePanel = new WelcomePanel(noteService, () -> {
                    frame.getContentPane().removeAll();
                    NoteListPanel noteListPanel = new NoteListPanel(noteService);
                    frame.add(noteListPanel, BorderLayout.CENTER);
                    frame.revalidate();
                    frame.repaint();
                });
                frame.add(welcomePanel, BorderLayout.CENTER);
            } else {
                NoteListPanel noteListPanel = new NoteListPanel(noteService);
                frame.add(noteListPanel, BorderLayout.CENTER);
            }
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
