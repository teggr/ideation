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

    public record DataCollection(String name) {}

    private static final String APPLICATION_DATA_DIRECTORY = "application.data.directory";

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
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize app data directory", e);
        }
    }

    public void registerDataCollection(DataCollection dataCollection) {
        try {
            Files.createDirectories((getApplicationDataDirectory().resolve(dataCollection.name())));
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize " + dataCollection.name() + " data collection", e);
        }
    }

    public Path getDataCollectionPath(DataCollection dataCollection) {
        return getApplicationDataDirectory().resolve(dataCollection.name());
    }

    private Path getApplicationDataDirectory() {
        Properties homeProperties = app.loadHomeProperties();
        String applicationDataDir = homeProperties.getProperty(APPLICATION_DATA_DIRECTORY);
        return Paths.get(applicationDataDir);
    }

    public File getDefaultDirectory() {
        return  new File(System.getProperty("user.home") + File.separator + "ideation");
    }

}
