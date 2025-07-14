package cafe.ideation.model;

import java.time.LocalDateTime;

public class Note {
    private String title;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
    private String content = "";
    private String contentType = "text/plain";

    public Note(String title) {
        this.title = title;
        this.dateCreated = LocalDateTime.now();
        this.dateUpdated = LocalDateTime.now();
    }

    public String getTitle() { return title; }
    public LocalDateTime getDateCreated() { return dateCreated; }
    public LocalDateTime getDateUpdated() { return dateUpdated; }
    public String getContent() { return content; }
    public String getContentType() { return contentType; }
    
    public void setTitle(String title) { this.title = title; }
    public void setDateUpdated(LocalDateTime dateUpdated) { this.dateUpdated = dateUpdated; }
    public void setContent(String content) { this.content = content; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public void setDateCreated(LocalDateTime dateCreated) { this.dateCreated = dateCreated; }
}
