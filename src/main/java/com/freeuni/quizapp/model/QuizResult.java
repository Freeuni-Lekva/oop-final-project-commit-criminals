package com.freeuni.quizapp.model;

import java.security.Timestamp;

public class QuizResult {
    private int id;
    private int userId;
    private int quizId;
    private int score;
    private int totalQuestions;
    private long timeTakenSeconds;
    private boolean isPracticeMode;
    private Timestamp completedAt;

    public QuizResult(int id, int userId, int quizId, int score,
                      int totalQuestions, long timeTakenSeconds, boolean isPracticeMode,
                      Timestamp completedAt) {
        this.id = id;
        this.userId = userId;
        this.quizId = quizId;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.timeTakenSeconds = timeTakenSeconds;
        this.isPracticeMode = isPracticeMode;
        this.completedAt = completedAt;
    }

    public QuizResult(int userId, int quizId) {
        this.userId = userId;
        this.quizId = quizId;
    }

}
