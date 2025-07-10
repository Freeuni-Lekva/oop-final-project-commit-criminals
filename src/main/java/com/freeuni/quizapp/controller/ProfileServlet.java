package com.freeuni.quizapp.controller;

import com.freeuni.quizapp.dao.impl.QuizDaoImpl;
import com.freeuni.quizapp.model.Achievement;
import com.freeuni.quizapp.model.Quiz;
import com.freeuni.quizapp.model.QuizResult;
import com.freeuni.quizapp.model.User;
import com.freeuni.quizapp.service.impl.ProfileServiceImpl;
import com.freeuni.quizapp.service.interfaces.ProfileService;
import com.freeuni.quizapp.util.DBConnector;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private final ProfileService profileService = new ProfileServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (User) (session != null ? session.getAttribute("currentUser") : null);

        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        boolean editMode = "true".equals(request.getParameter("edit"));

        try {
            List<Quiz> createdQuizzes = profileService.getUserCreatedQuizzes(currentUser.getId());
            Map<Integer, Integer> questionCounts = profileService.getQuestionCounts(createdQuizzes);
            List<QuizResult> quizResults = profileService.getUserQuizResults(currentUser.getId());
            List<String> activityHistory = profileService.buildActivityHistory(quizResults, 5);
            List<Achievement> achievements = profileService.getUserAchievements(currentUser.getId());


            List<String> greatestQuizNames = new ArrayList<>();
            if (achievements != null) {
                try (Connection conn = DBConnector.getConnection()) {
                    QuizDaoImpl quizDao = new QuizDaoImpl(conn);
                    for (Achievement achievement : achievements) {
                        if (achievement.getType() == com.freeuni.quizapp.enums.AchievementType.I_am_the_Greatest 
                            && achievement.getQuiz_id() > 0) {
                            Quiz quiz = quizDao.getQuizById(achievement.getQuiz_id());
                            if (quiz != null) {
                                greatestQuizNames.add(quiz.getTitle());
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            int quizzesCreated = createdQuizzes != null ? createdQuizzes.size() : 0;
            int quizzesTaken = quizResults != null ? quizResults.size() : 0;

            request.setAttribute("quizzesCreated", quizzesCreated);
            request.setAttribute("quizzesTaken", quizzesTaken);
            request.setAttribute("createdQuizzes", createdQuizzes);
            request.setAttribute("questionCounts", questionCounts);
            request.setAttribute("history", activityHistory);
            request.setAttribute("achievements", achievements);
            request.setAttribute("greatestQuizNames", greatestQuizNames);
            request.setAttribute("editMode", editMode);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("quizzesCreated", 0);
            request.setAttribute("quizzesTaken", 0);
            request.setAttribute("createdQuizzes", null);
            request.setAttribute("questionCounts", null);
            request.setAttribute("history", null);
            request.setAttribute("achievements", null);
            request.setAttribute("greatestQuizNames", new ArrayList<>());
            request.setAttribute("editMode", editMode);
        }
        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }
} 