package com.freeuni.quizapp.dao.impl;

import com.freeuni.quizapp.dao.interfaces.AnswerDao;
import com.freeuni.quizapp.model.Answer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnswerDaoImpl implements AnswerDao {
    private final String table_name = "answers";
    private final Connection con;

    public AnswerDaoImpl(Connection con) {
        this.con = con;
    }

    @Override
    public void addAnswer(Answer answer) throws SQLException {
        String query = "INSERT INTO " + table_name + "(question_id, answer_text, is_correct) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1,answer.getQuestionId());
            ps.setString(2,answer.getAnswerText());
            ps.setBoolean(3,answer.isCorrect());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Answer> getAnswersByQuestionId(int questionId) throws SQLException {
        String query = "SELECT * FROM " + table_name + " WHERE question_id=?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1,questionId);
            ResultSet rs = ps.executeQuery();
            List<Answer> answers = new ArrayList<>();
            while(rs.next()) {
                Answer a = new Answer(rs.getInt("answer_id"),
                        rs.getInt("question_id"),
                        rs.getString("answer_text"),
                        rs.getBoolean("is_correct")
                );
                answers.add(a);
            }
            return answers;
        }
    }

    @Override
    public Answer getAnswerById(int answerId) throws SQLException {
        String query = "SELECT * FROM " + table_name + " WHERE answer_id=?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, answerId);
            ResultSet rs = ps.executeQuery();
            if(!rs.next()) return null;
            Answer a = new Answer(rs.getInt("answer_id"),
                    rs.getInt("question_id"),
                    rs.getString("answer_text"),
                    rs.getBoolean("is_correct")
            );
            return a;
        }
    }

    @Override
    public void updateAnswer(int answerId, Answer updatedAnswer) throws SQLException {
        String query = "UPDATE " + table_name + " SET answer_text=?, is_correct=? WHERE answer_id=?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, updatedAnswer.getAnswerText());
            ps.setBoolean(2, updatedAnswer.isCorrect());
            ps.setInt(3, answerId);
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteAnswer(int answerId) throws SQLException {
        String query = "DELETE FROM " + table_name + " WHERE answer_id=?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, answerId);
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteAnswersByQuestionId(int questionId) throws SQLException {
        String query = "DELETE FROM " + table_name + " WHERE question_id=?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, questionId);
            ps.executeUpdate();
        }
    }
}
