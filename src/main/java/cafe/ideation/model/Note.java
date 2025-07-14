package cafe.ideation.model;

import java.time.LocalDateTime;

public class Note {
    private String title;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;

    public Note(String title) {
        this.title = title;
        this.dateCreated = LocalDateTime.now();
        this.dateUpdated = LocalDateTime.now();
    }

    public String getTitle() { return title; }
    public LocalDateTime getDateCreated() { return dateCreated; }
    public LocalDateTime getDateUpdated() { return dateUpdated; }
    public void setTitle(String title) { this.title = title; }
    public void setDateUpdated(LocalDateTime dateUpdated) { this.dateUpdated = dateUpdated; }
}
