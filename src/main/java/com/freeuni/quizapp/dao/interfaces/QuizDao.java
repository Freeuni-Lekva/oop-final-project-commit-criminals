package com.freeuni.quizapp.dao.interfaces;

import com.freeuni.quizapp.model.Quiz;
import com.freeuni.quizapp.model.User;

import java.sql.Timestamp;
import java.util.List;

public interface QuizDao {

    void addQuiz(String title, String  description, int creator_id);

    void deleteQuiz(String title);

    User getQuizCreator(String title);

    List<Quiz> findUsersCreatedQuizzes(int creator_id);

    List<Quiz> listRecentQuizzes(int num);

    Timestamp getCreationTime(String title);
}
