package com.freeuni.quizapp.service.interfaces;

import com.freeuni.quizapp.enums.AchievementType;
import com.freeuni.quizapp.model.Achievement;

import java.sql.SQLException;
import java.util.List;

public interface AchievementService {

    void checkQuizCompletionAchievements(int userId, int quizId, int score, int totalQuestions, boolean isPracticeMode) throws SQLException;

    void checkAndAwardQuizCreationAchievements(int userId, int quizId) throws SQLException;

    List<Achievement> getUserAchievements(int userId) throws SQLException;

    boolean hasAchievement(int userId, com.freeuni.quizapp.enums.AchievementType achievementType) throws SQLException;
    

    String getAchievementDescription(AchievementType achievementType);
} 