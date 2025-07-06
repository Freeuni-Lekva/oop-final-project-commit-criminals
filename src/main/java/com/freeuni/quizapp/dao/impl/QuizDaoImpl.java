package com.freeuni.quizapp.dao.impl;

import com.freeuni.quizapp.dao.interfaces.QuizDao;
import com.freeuni.quizapp.model.Quiz;
import com.freeuni.quizapp.model.User;

import java.sql.Timestamp;
import java.util.List;

public class QuizDaoImpl implements QuizDao {
    @Override
    public void addQuiz(String title, String description, int creator_id) {

    }

    @Override
    public void deleteQuiz(String title) {

    }

    @Override
    public User getQuizCreator(String title) {
        return null;
    }

    @Override
    public List<Quiz> findUsersCreatedQuizzes(int creator_id) {
        return List.of();
    }

    @Override
    public List<Quiz> listRecentQuizzes(int num) {
        return List.of();
    }

    @Override
    public Timestamp getCreationTime(String title) {
        return null;
    }
}
