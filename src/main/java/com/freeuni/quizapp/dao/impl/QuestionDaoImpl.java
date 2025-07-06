package com.freeuni.quizapp.dao.impl;

import com.freeuni.quizapp.dao.interfaces.QuestionDao;
import com.freeuni.quizapp.enums.QuestionType;
import com.freeuni.quizapp.model.Question;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionDaoImpl implements QuestionDao {
    private Connection con;
    private final String table_name = "questions";

    public QuestionDaoImpl(Connection con) {
        this.con = con;
    }

    @Override
    public Question getQuestionById(int id) throws SQLException {
        String query = "SELECT * FROM " + table_name + " WHERE question_id = ?";
        try(PreparedStatement pr = con.prepareStatement(query)){
            pr.setInt(1, id);
            ResultSet rs = pr.executeQuery();
            if(rs.next()){
                String q_type = rs.getString("type");
                QuestionType qt = QuestionType.valueOf(q_type);
                Question q = new Question(rs.getInt("question_id"),
                       rs.getInt("quiz_id"),
                       rs.getString("text"),
                        qt,
                        rs.getString("image_url"),
                        rs.getInt("q_order")
                );
                return q;
            }
        }
        return null;
    }

    @Override
    public void addQuestion(Question q) throws SQLException {
        String query = "INSERT INTO " + table_name + " (quiz_id, text, type, image_url, q_order) VALUES (?, ?, ?, ?, ?)";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setInt(1, q.getQuizId());
            ps.setString(2, q.getText());
            ps.setString(3, q.getType().toString());
            ps.setString(4, q.getImageUrl());
            ps.setInt(5, q.getOrder());
            ps.executeUpdate();
        }
    }

    @Override
    public void removeQuestion(int q_id) throws SQLException {
        String query = "DELETE FROM " + table_name + " WHERE question_id = ?";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setInt(1, q_id);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Question> getAllQuestions() throws SQLException {
        String query = "SELECT * FROM " + table_name + " ORDER BY q_order";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ResultSet rs = ps.executeQuery();
            List<Question> lst = getQuestionsFromRs(rs);
            return lst;
        }
    }

    @Override
    public List<Question> getQuizAllQuestions(int quiz_id) throws SQLException {
        String query = "SELECT * FROM " + table_name + " WHERE quiz_id = ? ORDER BY q_order";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setInt(1, quiz_id);
            ResultSet rs = ps.executeQuery();
            List<Question> res = getQuestionsFromRs(rs);
            return res;
        }
    }

    @Override
    public void updateQuestion(int update_q_id, Question q) throws SQLException {
        String updateQuery = "UPDATE questions SET quiz_id = ?, text = ?, type = ?, image_url = ?, q_order = ? WHERE question_id = ?";

        try (PreparedStatement ps = con.prepareStatement(updateQuery)) {
            ps.setInt(1, q.getQuizId());
            ps.setString(2, q.getText());
            ps.setString(3, q.getType().name());
            ps.setString(4, q.getImageUrl());
            ps.setInt(5, q.getOrder());
            ps.setInt(6, update_q_id);

            ps.executeUpdate();
        }
    }

    private List<Question> getQuestionsFromRs(ResultSet rs) throws SQLException {
        List<Question> lst = new ArrayList<>();
        while(rs.next()){
            String q_type = rs.getString("type");
            QuestionType qt = QuestionType.valueOf(q_type);
            Question q = new Question(rs.getInt("question_id"),
                    rs.getInt("quiz_id"),
                    rs.getString("text"),
                    qt,
                    rs.getString("image_url"),
                    rs.getInt("q_order")
            );
            lst.add(q);
        }
        return lst;
    }
}
