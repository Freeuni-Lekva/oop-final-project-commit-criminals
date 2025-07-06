package com.freeuni.quizapp.dao.interfaces;

import com.freeuni.quizapp.model.Achievement;
import com.freeuni.quizapp.model.User;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface AchievementDao {

    void addAchievement(Achievement a) throws SQLException;

    void deleteAchievement(int user_id, int ach_id)  throws SQLException;

    boolean hasUserAchievement(int user_id, int ach_id)  throws SQLException;

    Achievement getAchievement(int user_id, int ach_id)  throws SQLException;

    List<Achievement> getAchievements(int user_id)  throws SQLException;

    List<Achievement> getAllAchievements()  throws SQLException;

    Timestamp getAchievedTime(int user_id, int ach_id)  throws SQLException;

    int getQuizId(int user_id, int ach_id)  throws SQLException;
}
