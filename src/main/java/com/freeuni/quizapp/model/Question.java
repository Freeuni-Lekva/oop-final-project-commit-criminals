package com.freeuni.quizapp.model;

import com.freeuni.quizapp.enums.QuestionType;

import java.util.List;

public class Question {

    private int id;
    private int quizId;
    private String text;
    private QuestionType type;
    private String imageUrl;
    private int order;
    private List<Answer> answers;

    public Question(int id, int quizId, String text, QuestionType type,  String imageUrl, int order, List<Answer> answers) {
        this.id = id;
        this.quizId = quizId;
        this.text = text;
        this.type = type;
        this.imageUrl = imageUrl;
        this.order = order;
        this.answers = answers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
