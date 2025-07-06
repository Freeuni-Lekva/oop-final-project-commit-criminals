package com.freeuni.quizapp.dao.impl;

import com.freeuni.quizapp.dao.interfaces.QuizResultDao;
import com.freeuni.quizapp.model.Quiz;
import com.freeuni.quizapp.model.QuizResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuizResultDaoImpl implements QuizResultDao {
    private final String table_name = "quiz_results";
    private Connection con;

    public QuizResultDaoImpl(Connection connection) {
        con = connection;
    }


    @Override
    public List<QuizResult> getUsersQuizResults(int user_id) throws SQLException {
        String query = "SELECT * FROM " + table_name + " WHERE user_id = ?";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setInt(1, user_id);
            ResultSet rs = ps.executeQuery();
            List<QuizResult> res = getQuizResultsFromRs(rs);
            return res;
        }
    }

    @Override
    public QuizResult getUserQuizResult(int user_id, int quiz_id) throws SQLException {
        String query = "SELECT * FROM " + table_name + " WHERE user_id = ? AND quiz_id = ?";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setInt(1, user_id);
            ps.setInt(2, quiz_id);
            ResultSet rs = ps.executeQuery();
            if(!rs.next()) return null;
            QuizResult qr =  new QuizResult(rs.getInt("quiz_result_id"),
                    rs.getInt("user_id"),
                    rs.getInt("quiz_id"),
                    rs.getInt("total_score"),
                    rs.getInt("total_questions"),
                    rs.getInt("time_taken"),
                    rs.getBoolean("is_practice"),
                    rs.getTimestamp("completed_at")
            );
            return qr;
        }
    }

    @Override
    public List<QuizResult> getQuizResults(int quiz_id) throws SQLException {
        String query = "SELECT * FROM " + table_name + " WHERE quiz_id = ?";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setInt(1, quiz_id);
            ResultSet rs = ps.executeQuery();
            List<QuizResult> res = getQuizResultsFromRs(rs);
            return res;
        }
    }

    @Override
    public boolean containsQuizResult(int qr_id) throws SQLException {
        String query = "SELECT 1 FROM " + table_name + " WHERE quiz_result_id = ?";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setInt(1, qr_id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) return false;
            return true;
        }
    }

    @Override
    public void addQuizResult(QuizResult r) throws SQLException {
        String query = "INSERT INTO " + table_name + " (user_id, quiz_id, total_score, total_questions, time_taken, is_practice, completed_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setInt(1, r.getUserId());
            ps.setInt(2, r.getQuizId());
            ps.setInt(3, r.getScore());
            ps.setInt(4, r.getTotalQuestions());
            ps.setInt(5, r.getTimeTakenSeconds());
            ps.setBoolean(6, r.isPracticeMode());
            ps.setTimestamp(7, r.getCompletedAt());
            ps.executeUpdate();
        }
    }

    @Override
    public void updateQuizResult(int qr_id, QuizResult r) throws SQLException{
        String updateQuery = "UPDATE " + table_name + " SET user_id = ?, quiz_id = ?, total_score = ?, total_questions = ?, time_taken = ?, is_practice = ?, completed_at = ? WHERE quiz_result_id = ?";
        try (PreparedStatement ps = con.prepareStatement(updateQuery)) {
            ps.setInt(1, r.getUserId());
            ps.setInt(2, r.getQuizId());
            ps.setInt(3, r.getScore());
            ps.setInt(4, r.getTotalQuestions());
            ps.setInt(5, r.getTimeTakenSeconds());
            ps.setBoolean(6, r.isPracticeMode());
            ps.setTimestamp(7, r.getCompletedAt());
            ps.setInt(8, qr_id);
            ps.executeUpdate();
        }
    }

    @Override
    public void removeQuizResult(int qr_id) throws SQLException {
        String query = "DELETE FROM " + table_name + " WHERE quiz_result_id = ?";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setInt(1, qr_id);
            ps.executeUpdate();
        }
    }

    private List<QuizResult> getQuizResultsFromRs(ResultSet rs) throws SQLException {
        List<QuizResult> res = new ArrayList<>();
        while(rs.next()){
            QuizResult curr = new QuizResult(rs.getInt("quiz_result_id"),
                    rs.getInt("user_id"),
                    rs.getInt("quiz_id"),
                    rs.getInt("total_score"),
                    rs.getInt("total_questions"),
                    rs.getInt("time_taken"),
                    rs.getBoolean("is_practice"),
                    rs.getTimestamp("completed_at")
            );
            res.add(curr);
        }
        return res;
    }

}
