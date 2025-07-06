package com.freeuni.quizapp.dao.interfaces;

import com.freeuni.quizapp.model.QuizResult;

import java.sql.SQLException;
import java.util.List;

public interface QuizResultDao {

    List<QuizResult> getUsersQuizResults(int user_id) throws SQLException;

    QuizResult getUserQuizResult(int user_id, int quiz_id) throws SQLException;

    List<QuizResult> getQuizResults(int quiz_id) throws SQLException;

    boolean containsQuizResult(int qr_id) throws SQLException;

    void  addQuizResult(QuizResult r) throws SQLException;

    void updateQuizResult(int qr_id, QuizResult r) throws SQLException;

    void removeQuizResult(int qr_id) throws SQLException;
}
