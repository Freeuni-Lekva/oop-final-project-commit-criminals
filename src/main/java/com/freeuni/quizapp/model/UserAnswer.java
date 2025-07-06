package com.freeuni.quizapp.model;

public class UserAnswer {

    private int id;
    private int userId;
    private int quizId;
    private int questionId;
    private String givenAnswer;
    private boolean isCorrect;

    public UserAnswer(int id, int userId, int questionId, int quizId,String givenAnswer, boolean isCorrect) {
        this.id = id;
        this.userId = userId;
        this.quizId = quizId;
        this.questionId = questionId;
        this.givenAnswer = givenAnswer;
        this.isCorrect = isCorrect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getGivenAnswer() {
        return givenAnswer;
    }

    public void setGivenAnswer(String givenAnswer) {
        this.givenAnswer = givenAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
