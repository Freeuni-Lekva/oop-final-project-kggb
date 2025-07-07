package Models;
import java.sql.Time;
import java.sql.Timestamp;

public class QuizTakesHistory {
//    id BIGINT AUTO_INCREMENT PRIMARY KEY,
//    username VARCHAR(64) NOT NULL,
//    quiz_id BIGINT NOT NULL,
//    score BIGINT,
//    max_score BIGINT,
//    time_taken DATETIME DEFAULT CURRENT_TIMESTAMP,
//    time_spent TIME,
//    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE,
//    FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
    private long id;
    private String username;
    private long quizId;
    private long score;
    private long maxScore;
    private Timestamp timeTaken;
    private Time timeSpent;

    public QuizTakesHistory(long id, String username, long quizId, long score, long maxScore, Timestamp timeTaken, Time timeSpent) {
        this.id = id;
        this.username = username;
        this.quizId = quizId;
        this.score = score;
        this.maxScore = maxScore;
        this.timeTaken = timeTaken;
        this.timeSpent = timeSpent;
    }

    public long getId() { return id; }
    public String getUsername() { return username; }
    public long getQuizId() { return quizId; }
    public long getScore() { return score; }
    public long getMaxScore() { return maxScore; }
    public Timestamp getTimeTaken() { return timeTaken; }
    public Time getTimeSpent() { return timeSpent; }
}
