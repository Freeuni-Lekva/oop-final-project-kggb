package Models;

import java.util.HashSet;

public class Quiz {

    private int id;
    private String name;
    private HashSet<Question> questions;
    private String category;
    private String description;
    private String creator;
    private String creation_date;
    private boolean randomized;
    private boolean multi_page;
    private boolean immediate_score;

    public Quiz(int id, String name, String category, String description, String creator, String creation_date, boolean randomized, boolean multi_page, boolean immediate_score) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.creator = creator;
        this.creation_date = creation_date;
        this.randomized = randomized;
        this.multi_page = multi_page;
        this.immediate_score = immediate_score;
        this.questions = new HashSet<>();
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public HashSet<Question> getQuestions() {
        return questions;
    }
    public void setQuestions(HashSet<Question> questions) {
        this.questions = questions;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCreator() {
        return creator;
    }
    public void setCreator(String creator) {
        this.creator = creator;
    }
    public String getCreation_date() {
        return creation_date;
    }
    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }
    public boolean isRandomized() {
        return randomized;
    }
    public void setRandomized(boolean randomized) {
        this.randomized = randomized;
    }
    public boolean isMulti_page() {
        return multi_page;
    }
    public void setMulti_page(boolean multi_page) {
        this.multi_page = multi_page;
    }
    public boolean isImmediate_score() {
        return immediate_score;
    }
    public void setImmediate_score(boolean immediate_score) {
        this.immediate_score = immediate_score;
    }

}
