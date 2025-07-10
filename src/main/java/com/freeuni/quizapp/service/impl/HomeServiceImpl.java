package com.freeuni.quizapp.service.impl;

import com.freeuni.quizapp.dao.impl.QuizDaoImpl;
import com.freeuni.quizapp.dao.impl.QuizResultDaoImpl;
import com.freeuni.quizapp.dao.interfaces.QuizDao;
import com.freeuni.quizapp.dao.interfaces.QuizResultDao;
import com.freeuni.quizapp.model.Quiz;
import com.freeuni.quizapp.model.QuizResult;
import com.freeuni.quizapp.model.User;
import com.freeuni.quizapp.service.interfaces.HomeService;
import com.freeuni.quizapp.util.DBConnector;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HomeServiceImpl implements HomeService {
    private final QuizResultDao quizResultDao;
    private final QuizDao quizDao;
    private final HttpServletRequest request;
    private final User user;

    public HomeServiceImpl(HttpServletRequest request) {
        try {
            quizResultDao = new QuizResultDaoImpl(DBConnector.getConnection());
            quizDao = new QuizDaoImpl(DBConnector.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.request = request;
        user = (User) request.getSession().getAttribute("currentUser");
    }

    public void storePopularQuizzes() throws SQLException {
        List<Quiz> popularQuizzes = quizResultDao.listPopularQuizzes(5);
        request.setAttribute("popularQuizzes", popularQuizzes);
    }

    public void storeRecentlyTakenQuizzes() throws SQLException {
        if (user == null) return;
        List<QuizResult> quizResults = quizResultDao.getUsersQuizResults(user.getId());
        quizResults.sort((r1, r2) -> r2.getCompletedAt().compareTo(r1.getCompletedAt()));
        int n = 5;
        List<Quiz> res = null;
        if (!quizResults.isEmpty())
            res = new ArrayList<>();
        for (QuizResult quizResult : quizResults) {
            if (n-- < 1) break;
            res.add(quizDao.getQuizById(quizResult.getQuizId()));
        }
        request.setAttribute("recentlyTakenQuizzes", res);
    }

    public void storeRecentQuizzes() throws SQLException {
        List<Quiz> recentQuizzes = quizDao.listRecentQuizzes(5);
        request.setAttribute("recentQuizzes", recentQuizzes);
    }

    public void storeUsersCreatedQuizzes(User user) throws SQLException {
        if (user != null) {
            List<Quiz> usersCreatedQuizzes = quizDao.findUsersCreatedQuizzes(user.getId());
            request.setAttribute("recentlyCreatedQuizzes", usersCreatedQuizzes);
        }
    }

    public void storeRecentlyCreatedQuizzes(User user) throws SQLException {
    }

    public void storeRecentlyTakenQuizzes(User user) throws SQLException {
    }
}
