package Models;

import java.sql.Timestamp;

public class ReportedQuiz {

    private long quizId;
    private String username;
    private String message;
    private String reportType;
    private Timestamp reportedAt;
    private String status;

    public ReportedQuiz(long quizId, String username, String message, String reportType, Timestamp reportedAt, String status) {
        this.quizId = quizId;
        this.username = username;
        this.message = message;
        this.reportType = reportType;
        this.reportedAt = reportedAt;
        this.status = status;
    }
    public long getQuizId() { return quizId; }
    public String getUsername() { return username; }
    public String getMessage() { return message; }
    public String getReportType() { return reportType; }
    public Timestamp getReportedAt() { return reportedAt; }
    public String getStatus() { return status; }

}
