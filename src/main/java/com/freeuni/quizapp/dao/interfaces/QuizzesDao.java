package com.freeuni.quizapp.dao.interfaces;

import com.freeuni.quizapp.model.Quiz;
import com.freeuni.quizapp.model.User;

import java.sql.Timestamp;
import java.util.List;

public interface QuizzesDao {

    void addQuiz(Quiz quiz);

    void deleteQuiz(int quiz_id);

    User getQuizCreator(int quiz_id);

    Quiz getQuiz(int quiz_id);

    List<Quiz> findUsersCreatedQuizzes(int user_id);

    List<Quiz> listPopularQuizzes(int num);

    List<Quiz> listRecentQuizzes(int num);

    int countTimesTaken(int quiz_id);

    Timestamp getCreationTime(int quiz_id);

    double getAverageScore(int quizId);
}
