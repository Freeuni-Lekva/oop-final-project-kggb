package Models;

import java.sql.Time;
import java.sql.Timestamp;

public class Challenge {

    private long id;
    private String challenger;
    private String challenged;
    private long  quizID;
    private String challengeMessage;
    private Timestamp sentAt;
    private String status;
    private long challengerScore;
    private long challengedScore;
    private Timestamp createdAt;
    private int challengerBestScore;

    public Challenge(long id, String challenger, String challenged, long quizID, String challengeMessage,
                     Timestamp sentAt, String status,long challengerScore, long challengedScore, Timestamp createdAt) {
        this.id = id;
        this.challenger = challenger;
        this.challenged = challenged;
        this.quizID = quizID;
        this.challengeMessage = challengeMessage;
        this.sentAt = sentAt;
        this.status = status;
        this.challengerScore = challengerScore;
        this.challengedScore = challengedScore;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getChallenger() {
        return challenger;
    }
    public void setChallenger(String challenger) {
        this.challenger = challenger;
    }
    public String getChallenged() {
        return challenged;
    }
    public void setChallenged(String challenged) {
        this.challenged = challenged;
    }
    public long getQuizID() {
        return quizID;
    }
    public void setQuizID(long quizID) {
        this.quizID = quizID;
    }
    public String getChallengeMessage() {
        return challengeMessage;
    }
    public void setChallengeMessage(String challengeMessage) {
        this.challengeMessage = challengeMessage;
    }
    public Timestamp getSentAt() {
        return sentAt;
    }
    public void setSentAt(Timestamp sentAt) {
        this.sentAt = sentAt;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public long getChallengerScore() {
        return challengerScore;
    }
    public void setChallengerScore(long challengerScore) {
        this.challengerScore = challengerScore;
    }
    public long getChallengedScore() {
        return challengedScore;
    }
    public void setChallengedScore(long challengedScore) {
        this.challengedScore = challengedScore;
    }
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    public int getChallengerBestScore() { return challengerBestScore; }
    public void setChallengerBestScore(int challengerBestScore) { this.challengerBestScore = challengerBestScore; }

}
