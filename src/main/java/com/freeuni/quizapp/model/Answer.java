package com.freeuni.quizapp.model;

public class Answer {
    private int id;
    private int question_id;
    private String answer_text;
    private boolean is_correct;

    public Answer(int id, int questionId, String answerText, boolean isCorrect) {
        this.id = id;
        this.question_id = questionId;
        this.answer_text = answerText;
        this.is_correct = isCorrect;
    }

    public int getId() {
        return id;
    }

    public int getQuestionId() {
        return question_id;
    }

    public String getAnswerText() {
        return answer_text;
    }

    public boolean isCorrect() {
        return is_correct;
    }
}
