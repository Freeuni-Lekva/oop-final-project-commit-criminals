package com.freeuni.quizapp.dao.interfaces;

import com.freeuni.quizapp.model.Question;

import java.sql.SQLException;
import java.util.List;

public interface QuestionDao {

    Question getQuestionById(int id) throws SQLException;

    void addQuestion(Question q) throws SQLException;

    void removeQuestion(int q_id) throws SQLException;

    List<Question> getAllQuestions() throws SQLException;

    List<Question> getQuizAllQuestions(int quiz_id) throws SQLException;

    void updateQuestion(int update_q_id, Question q) throws SQLException;
}
