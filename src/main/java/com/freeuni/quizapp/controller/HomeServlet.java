package com.freeuni.quizapp.controller;

import com.freeuni.quizapp.model.Announcement;
import com.freeuni.quizapp.model.Quiz;
import com.freeuni.quizapp.model.User;
import com.freeuni.quizapp.service.impl.QuizResultsServiceImpl;
import com.freeuni.quizapp.service.impl.QuizzesServiceImpl;
import com.freeuni.quizapp.service.interfaces.AnnouncementService;
import com.freeuni.quizapp.service.impl.AnnouncementServiceImpl;
import com.freeuni.quizapp.service.interfaces.QuizResultsService;
import com.freeuni.quizapp.service.interfaces.QuizzesService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private final AnnouncementService announcementService = new AnnouncementServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        QuizzesService quizzesService = new QuizzesServiceImpl(request);
        QuizResultsService quizResultsService = new QuizResultsServiceImpl(request);

        try {
            List<Announcement> announcements = announcementService.getAllAnnouncements();
            request.setAttribute("announcements", announcements);
        } catch (Exception e) {
            request.setAttribute("error", "Could not load announcements.");
            e.printStackTrace();
        }

        try {
            quizzesService.storeRecentQuizzes();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            quizResultsService.storePopularQuizzes();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        User user = (User) request.getSession().getAttribute("currentUser");

        try {
            quizzesService.storeUsersCreatedQuizzes(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            quizzesService.storeRecentlyCreatedQuizzes(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            quizzesService.storeRecentlyTakenQuizzes(user);
        }  catch (SQLException e) {
            throw new RuntimeException(e);
        }

        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
