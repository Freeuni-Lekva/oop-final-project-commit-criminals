package com.freeuni.quizapp.dao.interfaces;

import com.freeuni.quizapp.model.Achievement;
import com.freeuni.quizapp.model.User;
import com.freeuni.quizapp.model.UserAnswer;

import java.util.List;

public interface UserAnswersDao {

    List<UserAnswer> getUserAnswers(int user_id);

    List<UserAnswer> getUsersQuizAnswers(int user_id, int quiz_id);

    void update(UserAnswer ua, int ua_id);

    void addUserAnswer(UserAnswer ua);

    void addAllAnswers(List<UserAnswer> lst);

    void removeUserAnswer(int ua_id);

    UserAnswer findByUserAndQuestion(int user_id, int question_id);
}
