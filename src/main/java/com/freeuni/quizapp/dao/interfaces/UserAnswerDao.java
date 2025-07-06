package com.freeuni.quizapp.dao.interfaces;

import com.freeuni.quizapp.model.Achievement;
import com.freeuni.quizapp.model.User;
import com.freeuni.quizapp.model.UserAnswer;

import java.sql.SQLException;
import java.util.List;

public interface UserAnswerDao {

    List<UserAnswer> getUserAnswers(int user_id) throws SQLException;

    List<UserAnswer> getUsersQuizAnswers(int user_id, int quiz_id) throws SQLException;

    void update(UserAnswer ua, int ua_id) throws SQLException;

    void addUserAnswer(UserAnswer ua) throws SQLException;

    void addAllAnswers(List<UserAnswer> lst) throws SQLException;

    void removeUserAnswer(int ua_id) throws SQLException;

    UserAnswer findByUserAndQuestion(int user_id, int question_id) throws SQLException;
}
