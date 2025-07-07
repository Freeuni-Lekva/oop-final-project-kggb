package DAOs;

public abstract class Question {
    protected long id;
    protected long quizId;
    protected String question;
    protected int questionOrder;
    protected int points;

    public Question(long id, long quizId, String question, int questionOrder, int points) {
        this.id = id;
        this.quizId = quizId;
        this.question = question;
        this.questionOrder = questionOrder;
        this.points = points;
    }

    public long getId() { return id; }
    public long getQuizId() { return quizId; }
    public String getQuestion() { return question; }
    public int getQuestionOrder() { return questionOrder; }
    public int getPoints() { return points; }
}
