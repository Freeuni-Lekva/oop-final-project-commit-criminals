package com.freeuni.quizapp.dao.interfaces;

import com.freeuni.quizapp.model.Answer;

import java.sql.SQLException;
import java.util.List;

public interface AnswerDao {

    void addAnswer(Answer answer) throws SQLException;

    List<Answer> getAnswersByQuestionId(int questionId) throws SQLException;

    Answer getAnswerById(int answerId) throws SQLException;

    void updateAnswer(int answerId, Answer updatedAnswer) throws SQLException;

    void deleteAnswer(int answerId) throws SQLException;

    void deleteAnswersByQuestionId(int questionId) throws SQLException;
}
