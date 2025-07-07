package Models;

import java.sql.Timestamp;

public class Message {

    private long id;
    private String sentFrom;
    private String sentTo;
    private String message;
    private String title;
    private Timestamp dateSent;

    public Message(long id, String sentFrom, String sentTo, String message, String title, Timestamp dateSent) {
        this.id = id;
        this.sentFrom = sentFrom;
        this.sentTo = sentTo;
        this.message = message;
        this.title = title;
        this.dateSent = dateSent;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getSentFrom() {
        return sentFrom;
    }
    public void setSentFrom(String sentFrom) {
        this.sentFrom = sentFrom;
    }
    public String getSentTo() {
        return sentTo;
    }
    public void setSentTo(String sentTo) {
        this.sentTo = sentTo;
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
    public Timestamp getDateSent() {
        return dateSent;
    }
    public void setDateSent(Timestamp dateSent) {
        this.dateSent = dateSent;
    }

}