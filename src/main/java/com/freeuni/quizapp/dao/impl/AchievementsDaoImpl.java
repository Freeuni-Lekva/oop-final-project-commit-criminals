package com.freeuni.quizapp.dao.impl;

import com.freeuni.quizapp.dao.interfaces.AchievementDao;
import com.freeuni.quizapp.enums.AchievementType;
import com.freeuni.quizapp.model.Achievement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AchievementsDaoImpl implements AchievementDao {
    private final String table_name = "achievements";
    private Connection con;

    public AchievementsDaoImpl(Connection con){
        this.con = con;
    }

    @Override
    public void addAchievement(Achievement a) throws SQLException {
        String query = "INSERT INTO " + table_name + "(user_id, achievement_name, quiz_id, achieved_at) VALUES (?, ?, ?, ?)";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setInt(1, a.getUserId());
            ps.setString(2, a.getType().toString());
            ps.setInt(3, a.getQuiz_id());
            ps.setTimestamp(4, a.getAchievedAt());
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteAchievement(int user_id, int ach_id) throws SQLException {
        String query = "DELETE FROM " + table_name +
                " WHERE (user_id = ? AND achievement_id = ?)";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setInt(1, user_id);
            ps.setInt(2, ach_id);
            ps.executeUpdate();
        }
    }

    @Override
    public boolean hasUserAchievement(int user_id, int ach_id) throws SQLException {
        Achievement a = getAchievement(user_id, ach_id);
        if(a == null) return false;
        return true;
    }

    @Override
    public Achievement getAchievement(int user_id, int ach_id) throws SQLException {
        String query = "SELECT 1 FROM " + table_name +
                " WHERE (user_id = ? AND achievement_id = ?)";
        try(PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, user_id);
            ps.setInt(2, ach_id);
            try(ResultSet rs = ps.executeQuery()){
                if(!rs.next()) return null;
                Achievement curr = new Achievement(rs.getInt("achievement_id"),
                        rs.getInt("user_id"),
                        AchievementType.valueOf(rs.getString("achievement_name")),
                        rs.getInt("quiz_id"),
                        rs.getTimestamp("achieved_at")
                );
                return curr;
            }
        }
    }

    @Override
    public List<Achievement> getAchievements(int user_id) throws SQLException {
        List<Achievement> res = new ArrayList<>();
        String query = "SELECT * FROM " + table_name + " WHERE user_id = ?";
        try(PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, user_id);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    Achievement curr = new Achievement(rs.getInt("achievement_id"),
                            rs.getInt("user_id"),
                            AchievementType.valueOf(rs.getString("achievement_name")),
                            rs.getInt("quiz_id"),
                            rs.getTimestamp("achieved_at")
                    );
                    res.add(curr);
                }
            }
        }
        return res;
    }

    @Override
    public List<Achievement> getAllAchievements() throws SQLException {
        List<Achievement> res = new ArrayList<>();
        String query = "SELECT * FROM " + table_name;
        try(PreparedStatement ps = con.prepareStatement(query)) {
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    Achievement curr = new Achievement(rs.getInt("achievement_id"),
                            rs.getInt("user_id"),
                            AchievementType.valueOf(rs.getString("achievement_name")),
                            rs.getInt("quiz_id"),
                            rs.getTimestamp("achieved_at")
                    );
                    res.add(curr);
                }
            }
        }
        return res;
    }

    @Override
    public Timestamp getAchievedTime(int user_id, int ach_id) throws SQLException {
        String query = "SELECT achieved_at FROM " + table_name + " WHERE user_id = ? AND achievement_id = ?";
        try(PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, user_id);
            ps.setInt(2, ach_id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) return rs.getTimestamp("achieved_at");
            }
        }
        return null;
    }

    @Override
    public int getQuizId(int user_id, int ach_id) throws SQLException {
        String query = "SELECT quiz_id FROM " + table_name + " WHERE user_id = ? AND achievement_id = ?";
        try(PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, user_id);
            ps.setInt(2, ach_id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) return rs.getInt("quiz_id");
            }
        }
        return -1;
    }
}
