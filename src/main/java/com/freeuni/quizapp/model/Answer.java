package com.freeuni.quizapp.model;

public class Answer {
    private int id;
    private int questionId;
    private String answerText;
    private boolean isCorrect;

    public Answer(int id, int questionId, String answerText, boolean isCorrect) {
        this.id = id;
        this.questionId = questionId;
        this.answerText = answerText;
        this.isCorrect = isCorrect;
    }

    public int getId() {
        return id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public String getAnswerText() {
        return answerText;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
}
