package com.freeuni.quizapp.dao.impl;

import com.freeuni.quizapp.dao.interfaces.UserAnswerDao;
import com.freeuni.quizapp.model.UserAnswer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//db needs quiz_id
public class UserAnswerDaoImpl implements UserAnswerDao {
    private final Connection con;
    private final String table_name = "user_answer";

    public UserAnswerDaoImpl(Connection con) {
        this.con = con;
    }

    @Override
    public List<UserAnswer> getUserAnswers(int user_id) throws SQLException {
        String query = "SELECT * FROM " + table_name + " WHERE user_id = ?";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setInt(1, user_id);
            ResultSet rs = ps.executeQuery();
            return getUserAnswersFromRs(rs);
        }
    }

    @Override
    public List<UserAnswer> getUsersQuizAnswers(int user_id, int quiz_id) throws SQLException {
        String query = "SELECT * FROM " + table_name + " WHERE user_id = ? AND quiz_id = ?";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setInt(1, user_id);
            ps.setInt(2, quiz_id);
            ResultSet rs = ps.executeQuery();
            return getUserAnswersFromRs(rs);
        }
    }

    @Override
    public void update(UserAnswer ua, int ua_id) throws SQLException {
        String query = "UPDATE " + table_name + " SET given_answer = ?, is_correct = ? WHERE user_answer_id = ?";
        try(PreparedStatement ps = con.prepareStatement(query)){
            //            ps.setInt(1, ua.getQuizId());
            ps.setString(1, ua.getGivenAnswer());
            ps.setBoolean(2, ua.isCorrect());
            ps.setInt(3, ua_id);
            ps.executeUpdate();
        }
    }

    @Override
    public void addUserAnswer(UserAnswer ua) throws SQLException {
        String query = "INSERT INTO " + table_name + " (user_id, question_id, given_answer, is_correct) VALUES (?, ?, ?, ?)";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setInt(1, ua.getUserId());
            ps.setInt(2, ua.getQuestionId());
//            ps.setInt(3, ua.getQuizId());
            ps.setString(3, ua.getGivenAnswer());
            ps.setBoolean(4, ua.isCorrect());
            ps.executeUpdate();
        }
    }

    @Override
    public void addAllAnswers(List<UserAnswer> lst) throws SQLException {
        for(UserAnswer ua : lst) addUserAnswer(ua);
    }

    @Override
    public void removeUserAnswer(int ua_id) throws SQLException {
        String query = "DELETE FROM " + table_name + " WHERE user_answer_id = ?";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setInt(1, ua_id);
            ps.executeUpdate();
        }
    }

    @Override
    public UserAnswer findByUserAndQuestion(int user_id, int question_id) throws SQLException {
        String  query = "SELECT * FROM " + table_name + " WHERE user_id = ? AND question_id = ?";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setInt(1, user_id);
            ps.setInt(2, question_id);
            ResultSet rs = ps.executeQuery();
            if(!rs.next()) return null;
            return new UserAnswer(rs.getInt("user_answer_id"),
                    rs.getInt("user_id"),
                    rs.getInt("question_id"),
                    rs.getInt("quiz_id"),
                    rs.getString("given_answer"),
                    rs.getBoolean("is_correct")
            );
        }
    }


    private List<UserAnswer> getUserAnswersFromRs(ResultSet rs) throws SQLException {
        List<UserAnswer> res = new ArrayList<>();
        while(rs.next()){
            UserAnswer ua = new UserAnswer(rs.getInt("user_answer_id"),
                    rs.getInt("user_id"),
                    rs.getInt("question_id"),
                    rs.getInt("quiz_id"),
                    rs.getString("given_answer"),
                    rs.getBoolean("is_correct")
            );
            res.add(ua);
        }
        return res;
    }

}
