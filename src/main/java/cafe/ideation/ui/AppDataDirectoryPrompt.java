package cafe.ideation.ui;

import cafe.ideation.service.AppDataDirectoryService;
import javax.swing.*;
import java.awt.*;

public class AppDataDirectoryPrompt {
    private final AppDataDirectoryService appDataDirectoryService;

    public AppDataDirectoryPrompt(AppDataDirectoryService appDataDirectoryService) {
        this.appDataDirectoryService = appDataDirectoryService;
    }

    public void promptForDirectory(Component parent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose Application Data Directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setSelectedFile( appDataDirectoryService.getDefaultDirectory());
        int result = chooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            String selectedDir = chooser.getSelectedFile().getAbsolutePath();
            appDataDirectoryService.setAppDataDir(selectedDir);
            JOptionPane.showMessageDialog(parent, "App data directory set to: " + selectedDir);
        }
    }
}
