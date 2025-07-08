package com.freeuni.quizapp.controller;

import com.freeuni.quizapp.dao.impl.QuizDaoImpl;
import com.freeuni.quizapp.dao.impl.QuestionDaoImpl;
import com.freeuni.quizapp.dao.impl.AnswerDaoImpl;
import com.freeuni.quizapp.model.Quiz;
import com.freeuni.quizapp.model.Question;
import com.freeuni.quizapp.model.Answer;
import com.freeuni.quizapp.model.User;
import com.freeuni.quizapp.util.DBConnector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Collections;

@WebServlet(name = "QuizServlet", urlPatterns = "/startQuiz")
public class QuizServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String quizIdParam = request.getParameter("quizId");
        if (quizIdParam == null) {
            response.sendRedirect("quizzes.jsp");
            return;
        }

        try {
            int quizId = Integer.parseInt(quizIdParam);
        
            boolean isRandom = "true".equals(request.getParameter("random"));
            boolean isOnePage = "true".equals(request.getParameter("onePage"));
            boolean isImmediateCorrection = "true".equals(request.getParameter("immediate"));
            boolean isPracticeMode = "true".equals(request.getParameter("practice"));

            try (Connection con = DBConnector.getConnection()) {
                QuizDaoImpl quizDao = new QuizDaoImpl(con);
                Quiz quiz = quizDao.getQuizById(quizId);
                
                if (quiz == null) {
                    response.sendRedirect("quizzes.jsp");
                    return;
                }

                QuestionDaoImpl questionDao = new QuestionDaoImpl(con);
                List<Question> questions = questionDao.getQuizAllQuestions(quizId);
                
                AnswerDaoImpl answerDao = new AnswerDaoImpl(con);
                for (Question question : questions) {
                    List<Answer> answers = answerDao.getAnswersByQuestionId(question.getId());
                    question.setAnswers(answers);
                }

                if (isRandom) {
                    Collections.shuffle(questions);
                }
                
                quiz.setRandom(isRandom);
                quiz.setOnePage(isOnePage);
                quiz.setImmediateCorrection(isImmediateCorrection);
                quiz.setPracticeModeEnabled(isPracticeMode);
                quiz.setQuestions(questions);
                
                // Always clear stored answers when starting any quiz (fresh start)
                session.removeAttribute("quizAnswers");
                session.removeAttribute("isActiveQuizSession");
                session.removeAttribute("lastQuizId");
                
                session.setAttribute("currentQuiz", quiz);
                session.setAttribute("quizStartTime", System.currentTimeMillis());
                
                request.getRequestDispatcher("takeQuiz.jsp").forward(request, response);
                
            } catch (SQLException e) {
                throw new ServletException("Database error while loading quiz", e);
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("quizzes.jsp");
        }
    }
} 