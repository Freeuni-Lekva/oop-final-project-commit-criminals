package com.freeuni.quizapp.service.impl;

import com.freeuni.quizapp.dao.impl.QuizDaoImpl;
import com.freeuni.quizapp.dao.interfaces.QuizDao;
import com.freeuni.quizapp.model.Quiz;
import com.freeuni.quizapp.model.User;
import com.freeuni.quizapp.service.interfaces.QuizzesService;
import com.freeuni.quizapp.util.DBConnector;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

public class QuizzesServiceImpl implements QuizzesService {
    private final QuizDao quizzesDao;
    private final HttpServletRequest request;
    private final User user;

    public QuizzesServiceImpl(HttpServletRequest request) {
        try {
            quizzesDao = new QuizDaoImpl(DBConnector.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.request = request;
        user = (User) request.getSession().getAttribute("currentUser");
    }

    public void storeRecentQuizzes() throws SQLException {
        List<Quiz> recentQuizzes = quizzesDao.listRecentQuizzes(5);
        request.setAttribute("recentQuizzes", recentQuizzes);
    }

    public void storePopularQuizzes() throws SQLException {
        List<Quiz> popularQuizzes = quizzesDao.listRecentQuizzes(6);
        for (Quiz q : popularQuizzes) {
            System.out.println(q.getTitle());
        }
        request.setAttribute("popularQuizzes", popularQuizzes);
    }

    public void storeUsersCreatedQuizzes(User user) throws SQLException {
        if (user != null) {
            List<Quiz> usersCreatedQuizzes = quizzesDao.findUsersCreatedQuizzes(user.getId());
            request.setAttribute("recentlyCreatedQuizzes", usersCreatedQuizzes);
        }
    }

    public void storeRecentlyCreatedQuizzes(User user) throws SQLException {
    }

    public void storeRecentlyTakenQuizzes(User user) throws SQLException {
    }
}
