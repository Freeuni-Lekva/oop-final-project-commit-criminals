package com.freeuni.quizapp.dao.interfaces;

import com.freeuni.quizapp.model.QuizResult;

import java.util.List;

public interface QuizResultsDao {

    List<QuizResult> getUsersQuizResults(int user_id);

    QuizResult getUserQuizResult(int user_id, int quiz_id);

    List<QuizResult> getQuizResults(int quiz_id);

    boolean containsQuizResult(QuizResult r);

    void  addQuizResult(QuizResult r);

    void updateQuizResult(int qr_id, QuizResult r);

    void removeQuizResult(int qr_id);
}
