package com.freeuni.quizapp.controller;

import com.freeuni.quizapp.dao.impl.AnswerDaoImpl;
import com.freeuni.quizapp.dao.impl.QuizResultDaoImpl;
import com.freeuni.quizapp.dao.impl.UserAnswerDaoImpl;
import com.freeuni.quizapp.dao.impl.UserDaoImpl;
import com.freeuni.quizapp.model.Answer;
import com.freeuni.quizapp.model.Quiz;
import com.freeuni.quizapp.model.User;
import com.freeuni.quizapp.util.DBConnector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@WebServlet("/submitQuiz")
public class SubmitQuizServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        User currentUser = (User) (session != null ? session.getAttribute("currentUser") : null);
        
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Quiz currentQuiz = (Quiz) session.getAttribute("currentQuiz");
        @SuppressWarnings("unchecked")
        Map<String, String> userAnswers = (Map<String, String>) session.getAttribute("quizAnswers");
        Object startTimeObj = session.getAttribute("quizStartTime");
        
        if (currentQuiz == null) {
            response.sendRedirect("quizzes.jsp");
            return;
        }
        
        try (Connection conn = DBConnector.getConnection()) {
            conn.setAutoCommit(true);
            
            AnswerDaoImpl answerDao = new AnswerDaoImpl(conn);
            UserAnswerDaoImpl userAnswerDao = new UserAnswerDaoImpl(conn);
            QuizResultDaoImpl quizResultDao = new QuizResultDaoImpl(conn);
            UserDaoImpl userDao = new UserDaoImpl(conn);
            
            int score = 0;
            int totalQuestions = currentQuiz.getQuestions().size();

            for (var question : currentQuiz.getQuestions()) {
                String userAnswerKey = "question_" + question.getId();
                String userAnswer = userAnswers != null ? userAnswers.get(userAnswerKey) : "";
                
                if ((userAnswer == null || userAnswer.trim().isEmpty()) && request.getParameter(userAnswerKey) != null) {
                    userAnswer = request.getParameter(userAnswerKey);
                }
                
                boolean isCorrect = false;
                if (userAnswer != null && !userAnswer.trim().isEmpty()) {

                    List<Answer> correctAnswers = answerDao.getAnswersByQuestionId(question.getId());
                    

                    for (Answer answer : correctAnswers) {
                        if (answer.isCorrect() && 
                            answer.getAnswerText().trim().equalsIgnoreCase(userAnswer.trim())) {
                            isCorrect = true;
                            break;
                        }
                    }
                    
                    if (isCorrect) {
                        score++;
                    }

                    userAnswerDao.addUserAnswer(
                        currentUser.getId(),
                        question.getId(),
                        userAnswer.trim(),
                        isCorrect
                    );
                }
            }
            

            int timeTakenSeconds = 0;
            if (startTimeObj != null) {
                long startTimeMillis = 0;
                if (startTimeObj instanceof Timestamp) {
                    startTimeMillis = ((Timestamp) startTimeObj).getTime();
                } else if (startTimeObj instanceof Long) {
                    startTimeMillis = (Long) startTimeObj;
                }
                if (startTimeMillis > 0) {
                    long timeDiff = System.currentTimeMillis() - startTimeMillis;
                    timeTakenSeconds = (int) (timeDiff / 1000);
                }
            }
            

            quizResultDao.addQuizResult(
                currentUser.getId(),
                currentQuiz.getId(),
                score,
                totalQuestions,
                timeTakenSeconds,
                currentQuiz.isPracticeModeEnabled()
            );

            
            // Set result attributes for results page
            request.setAttribute("quiz", currentQuiz);
            request.setAttribute("score", score);
            request.setAttribute("totalQuestions", totalQuestions);
            request.setAttribute("timeTakenSeconds", timeTakenSeconds);
            request.setAttribute("percentage", (double) score / totalQuestions * 100);
            
            // Clear quiz session data
            session.removeAttribute("currentQuiz");
            session.removeAttribute("quizAnswers");
            session.removeAttribute("quizStartTime");
            session.removeAttribute("isActiveQuizSession");
            
            // Forward to results page
            request.getRequestDispatcher("quizResults.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "Error processing quiz submission: " + e.getMessage());
        }
    }
} 