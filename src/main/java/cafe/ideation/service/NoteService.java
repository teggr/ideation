package cafe.ideation.service;

import cafe.ideation.model.Note;
import cafe.ideation.service.AppDataDirectoryService.DataCollection;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.security.SecureRandom;

@Component
public class NoteService {

    private static DataCollection NOTES_DATA_COLLECTION = new DataCollection("notes"); 

    private final AppDataDirectoryService appDataDirectoryService;

    public NoteService(AppDataDirectoryService appDataDirectoryService) {
        this.appDataDirectoryService = appDataDirectoryService;
        this.appDataDirectoryService.registerDataCollection(NOTES_DATA_COLLECTION);
    }

    public List<Note> listNotes() {
        List<Note> loadedNotes = new ArrayList<>();
        Path notesDir = appDataDirectoryService.getDataCollectionPath(NOTES_DATA_COLLECTION);
        if (!Files.exists(notesDir)) {
            return Collections.emptyList();
        }
        File[] files = notesDir.toFile().listFiles((dir, name) -> name.endsWith(".properties"));
        if (files != null) {
            for (File metaFile : files) {
                Properties props = new Properties();
                try (FileInputStream in = new FileInputStream(metaFile)) {
                    props.load(in);
                    String title = props.getProperty("title", "");
                    String created = props.getProperty("created", "");
                    String updated = props.getProperty("updated", "");
                    String contentType = props.getProperty("contentType", "text/plain");
                    String baseName = metaFile.getName().replaceFirst("\\.properties$", "");
                    String ext = contentType.equals("text/plain") ? "txt" : "dat";
                    File contentFile = notesDir.resolve(baseName + "." + ext).toFile();
                    String content = "";
                    if (contentFile.exists()) {
                        content = new String(Files.readAllBytes(contentFile.toPath()));
                    }
                    Note note = new Note(title);
                    note.setContent(content);
                    note.setContentType(contentType);
                    // Parse dates if present
                    if (!created.isEmpty()) note.setDateCreated(java.time.LocalDateTime.parse(created));
                    if (!updated.isEmpty()) note.setDateUpdated(java.time.LocalDateTime.parse(updated));
                    loadedNotes.add(note);
                } catch (Exception e) {
                    // skip corrupted notes
                }
            }
        }
        return Collections.unmodifiableList(loadedNotes);
    }

    public int getNoteCount() {
        Path notesDir = appDataDirectoryService.getDataCollectionPath(NOTES_DATA_COLLECTION);
        if (!Files.exists(notesDir)) {
            return 0;
        }
        File[] files = notesDir.toFile().listFiles((dir, name) -> name.endsWith(".properties"));
        return files != null ? files.length : 0;
    }

    public void addNote(Note note) {
        Properties props = new Properties();
        props.setProperty("title", note.getTitle());
        props.setProperty("created", note.getDateCreated().toString());
        props.setProperty("updated", note.getDateUpdated().toString());
        props.setProperty("contentType", note.getContentType());
        File metaFile = getMetaFile(note);
        try (FileOutputStream out = new FileOutputStream(metaFile)) {
            props.store(out, "Note Metadata");
        } catch (IOException e) {
            throw new RuntimeException("Failed to write note metadata", e);
        }
        File contentFile = getContentFile(note);
        try (FileOutputStream out = new FileOutputStream(contentFile)) {
            out.write(note.getContent().getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write note content", e);
        }
    }

    private String generateRandomFilename() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public Note createNote() {
        Note note = new Note("");
        note.setContent("");
        note.setContentType("text/plain");
        note.setDateCreated(java.time.LocalDateTime.now());
        note.setDateUpdated(java.time.LocalDateTime.now());
        addNote(note);
        return note;
    }
    
    public void deleteNote(Note note) {
        File metaFile = getMetaFile(note);
        File contentFile = getContentFile(note);
        if (metaFile.exists()) metaFile.delete();
        if (contentFile.exists()) contentFile.delete();
    }

    public void editNote(Note note) {
        addNote(note);
    }

    public void saveNote(Note note, String newContent) {
        note.setContent(newContent);
        note.setDateUpdated(java.time.LocalDateTime.now());
        // Set title to first line of content if available
        String[] lines = newContent.split("\r?\n", 2);
        String newTitle = lines.length > 0 && !lines[0].isBlank() ? lines[0] : "Untitled";
        note.setTitle(newTitle);
        editNote(note);
    }

    private String getFileExtension(String contentType) {
        return contentType.equals("text/plain") ? "txt" : "dat";
    }


    private String escapeFilename(String input) {
        // Remove or replace characters not allowed in filenames (Windows, macOS, Linux safe)
        return input.replaceAll("[\\\\/:*?\"<>|]", "_").trim();
    }

    private File getMetaFile(Note note) {
        String title = (note.getTitle() == null || note.getTitle().isEmpty()) ? generateRandomFilename() : escapeFilename(note.getTitle());
        Path notesDir = appDataDirectoryService.getDataCollectionPath(NOTES_DATA_COLLECTION);
        return notesDir.resolve(title + ".properties").toFile();
    }

    private File getContentFile(Note note) {
        String title = (note.getTitle() == null || note.getTitle().isEmpty()) ? generateRandomFilename() : escapeFilename(note.getTitle());
        Path notesDir = appDataDirectoryService.getDataCollectionPath(NOTES_DATA_COLLECTION);
        String ext = getFileExtension(note.getContentType());
        return notesDir.resolve(title + "." + ext).toFile();
    }
}
