package Models;

public abstract class Question {
    protected long id;
    protected long quizId;
    protected String question;
    protected int questionOrder;
    protected int points;

    // ✅ Default constructor (required by child classes using empty constructor)
    public Question() {}

    // ✅ Full constructor
    public Question(long id, long quizId, String question, int questionOrder, int points) {
        this.id = id;
        this.quizId = quizId;
        this.question = question;
        this.questionOrder = questionOrder;
        this.points = points;
    }

    // ✅ Getters
    public long getId() { return id; }
    public long getQuizId() { return quizId; }
    public String getQuestion() { return question; }
    public int getQuestionOrder() { return questionOrder; }
    public int getPoints() { return points; }

    // ✅ Setters (required for servlet construction)
    public void setId(long id) { this.id = id; }
    public void setQuizId(long quizId) { this.quizId = quizId; }
    public void setQuestion(String question) { this.question = question; }
    public void setQuestionOrder(int questionOrder) { this.questionOrder = questionOrder; }
    public void setPoints(int points) { this.points = points; }
}
