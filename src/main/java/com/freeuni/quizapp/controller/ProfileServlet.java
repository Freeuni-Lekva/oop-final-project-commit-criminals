package com.freeuni.quizapp.controller;

import com.freeuni.quizapp.dao.impl.QuizDaoImpl;
import com.freeuni.quizapp.dao.impl.QuizResultDaoImpl;
import com.freeuni.quizapp.dao.impl.QuestionDaoImpl;
import com.freeuni.quizapp.model.Quiz;
import com.freeuni.quizapp.model.QuizResult;
import com.freeuni.quizapp.model.User;
import com.freeuni.quizapp.util.DBConnector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
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
        


        try (Connection conn = DBConnector.getConnection()) {

            conn.setAutoCommit(true);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            
            QuizDaoImpl quizDao = new QuizDaoImpl(conn);
            QuizResultDaoImpl quizResultDao = new QuizResultDaoImpl(conn);
            QuestionDaoImpl questionDao = new QuestionDaoImpl(conn);


            List<Quiz> createdQuizzes = quizDao.findUsersCreatedQuizzes(currentUser.getId());
            int quizzesCreated = createdQuizzes != null ? createdQuizzes.size() : 0;
            

            Map<Integer, Integer> questionCounts = new HashMap<>();
            if (createdQuizzes != null) {
                for (Quiz quiz : createdQuizzes) {
                    int questionCount = questionDao.getQuizAllQuestions(quiz.getId()).size();
                    questionCounts.put(quiz.getId(), questionCount);
                }
            }

            List<QuizResult> takenResults = quizResultDao.getUsersQuizResults(currentUser.getId());
            int quizzesTaken = takenResults != null ? takenResults.size() : 0;


            List<String> history = new ArrayList<>();
            if (takenResults != null) {
                takenResults.sort(Comparator.comparing(QuizResult::getCompletedAt).reversed());
                for (int i = 0; i < Math.min(5, takenResults.size()); i++) {
                    QuizResult qr = takenResults.get(i);
                    Quiz quiz = quizDao.getQuizById(qr.getQuizId());
                    if (quiz != null) {
                        String activity = String.format(
                            "Took quiz: %s (Score: %d/%d, Time: %ds, %s, Date: %s)",
                            quiz.getTitle(),
                            qr.getScore(),
                            qr.getTotalQuestions(),
                            qr.getTimeTakenSeconds(),
                            qr.isPracticeMode() ? "Practice" : "Normal",
                            qr.getCompletedAt() != null ? qr.getCompletedAt().toString() : "Unknown"
                        );
                        history.add(activity);
                    }
                }
            }

            request.setAttribute("quizzesCreated", quizzesCreated);
            request.setAttribute("quizzesTaken", quizzesTaken);
            request.setAttribute("createdQuizzes", createdQuizzes);
            request.setAttribute("questionCounts", questionCounts);
            request.setAttribute("history", history);
            request.setAttribute("editMode", editMode);

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }
} 