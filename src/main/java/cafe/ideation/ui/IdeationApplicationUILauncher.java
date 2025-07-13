package cafe.ideation.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import dev.rebelcraft.cli.App;

import javax.swing.*;
import java.awt.*;

@Component
public class IdeationApplicationUILauncher implements CommandLineRunner {
    private final App app;

    @Autowired
    public IdeationApplicationUILauncher(App app) {
        this.app = app;
    }

    @Override
    public void run(String... args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Hello World");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 200);
            frame.setLayout(new BorderLayout());
            frame.add(new JLabel("Hello World", SwingConstants.CENTER), BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
