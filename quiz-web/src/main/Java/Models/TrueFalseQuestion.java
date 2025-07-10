package Models;

public class TrueFalseQuestion extends Question {
    private boolean correctAnswer;
    public TrueFalseQuestion(long id, long quizId, String question, boolean correctAnswer, int questionOrder, int points) {
        super(id, quizId, question, questionOrder, points);
        this.correctAnswer = correctAnswer;
    }
    public boolean isCorrectAnswer() {
        return correctAnswer;
    }

    @Override
    public boolean isUsersAnswerCorrect(String userAnswer) {
        if (userAnswer == null) return false;
        return userAnswer.equalsIgnoreCase(String.valueOf(correctAnswer));
    }
}
