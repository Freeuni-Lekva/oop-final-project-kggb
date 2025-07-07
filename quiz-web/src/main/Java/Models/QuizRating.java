package Models;

public class QuizRating {
    private long id;
    private String player;
    private long quizId;
    private int rating;
    private String review;
    private java.sql.Timestamp createdAt;
    public QuizRating() {}

    public QuizRating(long id, String player, long quizId, int rating, String review, java.sql.Timestamp createdAt) {
        this.id = id;
        this.player = player;
        this.quizId = quizId;
        this.rating = rating;
        this.review = review;
        this.createdAt = createdAt;
    }


    public String getPlayer() {return player;}
    public long getQuizId() {return quizId;}
    public int getRating() {return rating;}
    public String getReview() {return review;}
    public java.sql.Timestamp getCreatedAt() {return createdAt;}
    public long getId() {return id;}

}
