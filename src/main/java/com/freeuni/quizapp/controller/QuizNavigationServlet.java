package com.freeuni.quizapp.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "QuizNavigationServlet", urlPatterns = "/quizNavigate")
public class QuizNavigationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Get or create answers map from session
        Map<String, String> userAnswers = (Map<String, String>) session.getAttribute("quizAnswers");
        if (userAnswers == null) {
            userAnswers = new HashMap<>();
        }

        // Save all current form answers
        String currentQuestionId = request.getParameter("currentQuestionId");
        if (currentQuestionId != null) {
            // Save text input answer
            String textAnswer = request.getParameter("question_" + currentQuestionId);
            if (textAnswer != null && !textAnswer.trim().isEmpty()) {
                userAnswers.put("question_" + currentQuestionId, textAnswer.trim());
            }
            
            // Save radio button answer  
            String radioAnswer = request.getParameter("question_" + currentQuestionId);
            if (radioAnswer != null && !radioAnswer.trim().isEmpty()) {
                userAnswers.put("question_" + currentQuestionId, radioAnswer.trim());
            }
        }

        // Update session
        session.setAttribute("quizAnswers", userAnswers);

        // Get navigation direction
        String direction = request.getParameter("direction");
        String currentIndexParam = request.getParameter("currentIndex");
        
        int currentIndex = 0;
        if (currentIndexParam != null) {
            try {
                currentIndex = Integer.parseInt(currentIndexParam);
            } catch (NumberFormatException e) {
                currentIndex = 0;
            }
        }

        // Handle different directions
        if ("submit".equals(direction)) {
            // Forward to submit servlet
            request.getRequestDispatcher("/submitQuiz").forward(request, response);
            return;
        }

        int newIndex = currentIndex;
        if ("next".equals(direction)) {
            newIndex = currentIndex + 1;
        } else if ("previous".equals(direction)) {
            newIndex = currentIndex - 1;
        }

        // Redirect to quiz page with new question index
        response.sendRedirect("takeQuiz.jsp?questionIndex=" + newIndex);
    }
} 