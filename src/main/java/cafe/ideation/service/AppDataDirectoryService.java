package cafe.ideation.service;

import dev.rebelcraft.cli.App;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@Service
public class AppDataDirectoryService {

    private static final String APPLICATION_DATA_DIRECTORY = "application.data.directory";
    private static final String NOTES_SUBDIR = "notes";
    private final App app;

    public AppDataDirectoryService(App app) {
        this.app = app;
    }

    public boolean isInitialised() {
        Properties homeProperties = app.loadHomeProperties();
        return homeProperties.containsKey(APPLICATION_DATA_DIRECTORY);
    }

    public void setAppDataDir(String dir) {
        if (dir == null || dir.isEmpty()) {
            throw new IllegalArgumentException("Directory cannot be null or empty");
        }
        initFolders(dir);
        Properties homeProperties = app.loadHomeProperties();
        homeProperties.setProperty(APPLICATION_DATA_DIRECTORY, dir);
        app.saveHomeProperties(homeProperties);
    }

    private void initFolders(String dir) {
        try {
            Files.createDirectories(Paths.get(dir));
            Files.createDirectories(Paths.get(dir, NOTES_SUBDIR));
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize app data directory", e);
        }
    }

    public Path getNotesDirectory() {
        Properties homeProperties = app.loadHomeProperties();
        String applicationDataDir = homeProperties.getProperty(APPLICATION_DATA_DIRECTORY);
        return Paths.get(applicationDataDir, NOTES_SUBDIR);
    }

    public File getDefaultDirectory() {
        return  new File(System.getProperty("user.home") + File.separator + "ideation");
    }

}
