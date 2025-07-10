package Models;

public class PictureResponseQuestion extends Question {
    private String pictureUrl;
    private String correctAnswer;

    public PictureResponseQuestion(long id, long quizId, String pictureUrl, String question, String correctAnswer, int questionOrder, int points) {
        super(id, quizId, question, questionOrder, points);
        this.pictureUrl = pictureUrl;
        this.correctAnswer = correctAnswer;
    }

    public String getPictureUrl() { return pictureUrl; }
    public String getCorrectAnswer() { return correctAnswer; }

    @Override
    public boolean isUsersAnswerCorrect(String userAnswer) {
        if (userAnswer == null) return false;
        return userAnswer.trim().equalsIgnoreCase(correctAnswer.trim());
    }
}
