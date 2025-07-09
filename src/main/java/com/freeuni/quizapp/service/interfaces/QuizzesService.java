package com.freeuni.quizapp.service.interfaces;

import com.freeuni.quizapp.model.User;

import java.sql.SQLException;

public interface QuizzesService {

    void storeRecentQuizzes() throws SQLException;

    void storePopularQuizzes() throws SQLException;

    void storeUsersCreatedQuizzes(User user) throws SQLException;

    void storeRecentlyTakenQuizzes(User user) throws SQLException;

    void storeRecentlyCreatedQuizzes(User user) throws SQLException;
}
