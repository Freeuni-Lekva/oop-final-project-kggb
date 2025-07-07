package DAOs;

import java.util.ArrayList;
import java.util.List;

public class MultipleChoiceQuestion extends Question {
    private String correctAnswer;
    private List<String> choices;

    public MultipleChoiceQuestion(long id, long quizId, String question, String correctAnswer, List<String> choices, int questionOrder, int points) {
        super(id, quizId, question, questionOrder, points);
        this.correctAnswer = correctAnswer;
        this.choices = choices;
    }

    public String getCorrectAnswer() { return correctAnswer; }
    public List<String> getChoices() { return choices; }
}
