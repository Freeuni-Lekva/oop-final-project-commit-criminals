package com.freeuni.quizapp.service.impl;

import com.freeuni.quizapp.dao.impl.QuizResultDaoImpl;
import com.freeuni.quizapp.dao.interfaces.QuizResultDao;
import com.freeuni.quizapp.model.Quiz;
import com.freeuni.quizapp.model.User;
import com.freeuni.quizapp.service.interfaces.QuizResultsService;
import com.freeuni.quizapp.util.DBConnector;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

public class QuizResultsServiceImpl implements QuizResultsService {
    private final QuizResultDao quizResultDao;
    private final HttpServletRequest request;
    private final User user;

    public QuizResultsServiceImpl(HttpServletRequest request) {
        try {
            quizResultDao = new QuizResultDaoImpl(DBConnector.getConnection());
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
}
