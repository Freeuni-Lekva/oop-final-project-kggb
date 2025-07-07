package Models;

import java.sql.Timestamp;

public class Announcement {

    private long id;
    private String type;
    private String message;
    private String title;
    private Timestamp createdAt;
    private String createdBy;
    private boolean active;

    public Announcement(long id, String type, String message, String title, Timestamp createdAt, String createdBy, boolean active) {
        this.id = id;
        this.type = type;
        this.message = message;
        this.title = title;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.active = active;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

}

