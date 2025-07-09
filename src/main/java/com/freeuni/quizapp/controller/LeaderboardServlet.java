package com.freeuni.quizapp.controller;

import com.freeuni.quizapp.dao.impl.QuizDaoImpl;
import com.freeuni.quizapp.dao.impl.QuizResultDaoImpl;
import com.freeuni.quizapp.dao.impl.UserDaoImpl;
import com.freeuni.quizapp.model.Quiz;
import com.freeuni.quizapp.model.QuizResult;
import com.freeuni.quizapp.model.User;
import com.freeuni.quizapp.util.DBConnector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/leaderboard")
public class LeaderboardServlet extends HttpServlet {

    public static class LeaderboardEntry {
        private String username;
        private int score;
        private int totalQuestions;
        private double percentage;
        private int timeTakenSeconds;
        private String completedAt;

        public LeaderboardEntry(String username, int score, int totalQuestions, int timeTakenSeconds, String completedAt) {
            this.username = username;
            this.score = score;
            this.totalQuestions = totalQuestions;
            this.percentage = totalQuestions > 0 ? (double) score / totalQuestions * 100 : 0;
            this.timeTakenSeconds = timeTakenSeconds;
            this.completedAt = completedAt;
        }

        public String getUsername() { return username; }
        public int getScore() { return score; }
        public int getTotalQuestions() { return totalQuestions; }
        public double getPercentage() { return percentage; }
        public int getTimeTakenSeconds() { return timeTakenSeconds; }
        public String getCompletedAt() { return completedAt; }
    }

    public static class QuizLeaderboard {
        private Quiz quiz;
        private List<LeaderboardEntry> entries;

        public QuizLeaderboard(Quiz quiz, List<LeaderboardEntry> entries) {
            this.quiz = quiz;
            this.entries = entries;
        }

        public Quiz getQuiz() { return quiz; }
        public List<LeaderboardEntry> getEntries() { return entries; }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try (Connection conn = DBConnector.getConnection()) {
            QuizDaoImpl quizDao = new QuizDaoImpl(conn);
            QuizResultDaoImpl quizResultDao = new QuizResultDaoImpl(conn);
            UserDaoImpl userDao = new UserDaoImpl(conn);
            
            List<Quiz> allQuizzes = quizDao.listRecentQuizzes(1000);
            List<QuizLeaderboard> quizLeaderboards = new ArrayList<>();
            
            for (Quiz quiz : allQuizzes) {
                List<QuizResult> quizResults = quizResultDao.getQuizResults(quiz.getId());
                
                if (quizResults.isEmpty()) {
                    continue;
                }
                
                Map<Integer, QuizResult> bestScoresByUser = new HashMap<>();
                
                for (QuizResult result : quizResults) {
                    if (!result.isPracticeMode()) { 
                        Integer userId = result.getUserId();
                        QuizResult existingBest = bestScoresByUser.get(userId);
                        
                        if (existingBest == null || 
                            result.getScore() > existingBest.getScore() ||
                            (result.getScore() == existingBest.getScore() && 
                             result.getTimeTakenSeconds() < existingBest.getTimeTakenSeconds())) {
                            bestScoresByUser.put(userId, result);
                        }
                    }
                }
                
                List<LeaderboardEntry> entries = new ArrayList<>();
                for (QuizResult result : bestScoresByUser.values()) {
                    try {
                        User user = userDao.getUser(result.getUserId());
                        if (user != null) {
                            String completedAt = result.getCompletedAt() != null ? 
                                result.getCompletedAt().toString().substring(0, 16) : "Unknown";
                            
                            entries.add(new LeaderboardEntry(
                                user.getUsername(),
                                result.getScore(),
                                result.getTotalQuestions(),
                                result.getTimeTakenSeconds(),
                                completedAt
                            ));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                
                entries.sort((a, b) -> {
                    int scoreCompare = Integer.compare(b.getScore(), a.getScore());
                    if (scoreCompare != 0) return scoreCompare;
                    return Integer.compare(a.getTimeTakenSeconds(), b.getTimeTakenSeconds());
                });
                
                if (entries.size() > 10) {
                    entries = entries.subList(0, 10);
                }
                
                if (!entries.isEmpty()) {
                    quizLeaderboards.add(new QuizLeaderboard(quiz, entries));
                }
            }
            
            quizLeaderboards.sort((a, b) -> Integer.compare(b.getEntries().size(), a.getEntries().size()));
            
            request.setAttribute("quizLeaderboards", quizLeaderboards);
            request.getRequestDispatcher("leaderboard.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                             "Database error while loading leaderboard");
        }
    }
} 