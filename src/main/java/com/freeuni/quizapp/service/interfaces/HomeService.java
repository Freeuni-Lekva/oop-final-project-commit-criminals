package com.freeuni.quizapp.service.interfaces;

import com.freeuni.quizapp.model.User;

import java.sql.SQLException;

public interface HomeService {

    void storePopularQuizzes() throws SQLException;

    void storeRecentlyTakenQuizzes() throws SQLException;

    void storeRecentQuizzes() throws SQLException;

    void storeUsersCreatedQuizzes(User user) throws SQLException;

    void storeRecentlyTakenQuizzes(User user) throws SQLException;

    void storeRecentlyCreatedQuizzes(User user) throws SQLException;

}
