package com.freeuni.quizapp.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return id == answer.id && question_id == answer.question_id && is_correct == answer.is_correct && Objects.equals(answer_text, answer.answer_text);
    }

}
