package Models;

public class FillInTheBlankQuestion extends Question {
    private String correctAnswer;
    private boolean caseSensitive;

    public FillInTheBlankQuestion(long id, long quizId, String question, String correctAnswer, boolean caseSensitive, int questionOrder, int points) {
        super(id, quizId, question, questionOrder, points);
        this.correctAnswer = correctAnswer;
        this.caseSensitive = caseSensitive;
    }

    public FillInTheBlankQuestion() {
        super();
    }

    public String getCorrectAnswer() { return correctAnswer; }
    public boolean isCaseSensitive() { return caseSensitive; }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }
}
