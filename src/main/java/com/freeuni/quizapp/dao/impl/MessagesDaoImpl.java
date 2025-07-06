package com.freeuni.quizapp.dao.impl;

import com.freeuni.quizapp.dao.interfaces.MessageDao;
import com.freeuni.quizapp.dao.interfaces.UserDao;
import com.freeuni.quizapp.model.Message;
import com.freeuni.quizapp.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MessagesDaoImpl implements MessageDao {
    private final String table_name = "messages";
    private Connection con;

    public MessagesDaoImpl(Connection con) {
        this.con = con;
    }

    @Override
    public void addMessage(Message m) throws SQLException {
        String query = "INSERT INTO " + table_name + "(from_user_id, to_user_id, text, timestamp) VALUES (?, ?, ?, ?)";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setInt(1, m.getSenderId());
            ps.setInt(2, m.getReceiverId());
            ps.setString(3, m.getContent());
            ps.setTimestamp(4, m.getSentAt());
            ps.executeUpdate();
        }
    }

    @Override
    public void removeMessage(int m_id) throws SQLException {
        String query = "DELETE FROM " + table_name +
                " WHERE message_id = ?";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setInt(1, m_id);
            ps.executeUpdate();
        }
    }

    @Override
    public Message getLastMessage(int from_id, int to_id) throws SQLException {
        String query = "SELECT * FROM " + table_name + " WHERE from_user_id = ? AND to_user_id = ? ORDER BY timestamp DESC LIMIT 1";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setInt(1, from_id);
            ps.setInt(2, to_id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    Message m = new Message(rs.getInt("message_id"),
                            rs.getInt("from_user_id"),
                            rs.getInt("to_user_id"),
                            null, //enum after its added to database
                            rs.getString("text"),
                            rs.getTimestamp("timestamp")
                    );
                    return m;
                }
            }
        }
        return null;
    }

    @Override
    public List<Message> getMessages(int from_id, int to_id) throws SQLException {
        List<Message> res = new ArrayList<>();
        String query = "SELECT * FROM " + table_name + " WHERE from_user_id = ? AND to_user_id = ? ORDER BY timestamp DESC";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setInt(1, from_id);
            ps.setInt(2, to_id);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    Message m = new Message(rs.getInt("message_id"),
                            rs.getInt("from_user_id"),
                            rs.getInt("to_user_id"),
                            null, //enum after its added to database
                            rs.getString("text"),
                            rs.getTimestamp("timestamp")
                    );
                    res.add(m);
                }
            }
        }
        return res;
    }

    @Override
    public List<User> getInboxPeopleList(int user_id) throws SQLException {
        UserDao userDao = new UserDaoImpl(con);
        List<User> res = new ArrayList<>();
        Set<Integer> seen_users = new HashSet<>();
        String query = "SELECT * FROM " + table_name + " WHERE from_user_id = ? OR to_user_id = ?";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setInt(1, user_id);
            ps.setInt(2, user_id);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    User user;
                    int from = rs.getInt("from_user_id");
                    int to = rs.getInt("to_user_id");
                    int otherUserId = (from == user_id) ? to : from;
                    if(seen_users.contains(otherUserId)) continue;
                    user = userDao.getUser(otherUserId);
                    if(user != null){
                        res.add(user);
                        seen_users.add(otherUserId);
                    }
                }
            }
        }
        return res;
    }


    @Override
    public List<Message> getAllMessages() throws SQLException {
        List<Message> res = new ArrayList<>();
        String query = "SELECT * FROM " + table_name + " ORDER BY timestamp DESC";
        try(PreparedStatement ps = con.prepareStatement(query)){
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    Message m = new Message(rs.getInt("message_id"),
                            rs.getInt("from_user_id"),
                            rs.getInt("to_user_id"),
                            null, //enum after its added to database
                            rs.getString("text"),
                            rs.getTimestamp("timestamp")
                    );
                    res.add(m);
                }
            }
        }
        return res;
    }

    @Override
    public boolean messageExists(int m_id) throws SQLException {
        String query = "SELECT 1 FROM " + table_name + " WHERE message_id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, m_id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public Message getMessage(int m_id) throws SQLException {
        String query = "SELECT * FROM " + table_name + " WHERE message_id = ?";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setInt(1, m_id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    Message m = new Message(rs.getInt("message_id"),
                            rs.getInt("from_user_id"),
                            rs.getInt("to_user_id"),
                            null, //enum after its added to database
                            rs.getString("text"),
                            rs.getTimestamp("timestamp")
                    );
                    return m;
                }
            }
        }
        return null;
    }

    @Override
    public Timestamp getSentMessageTime(int m_id) throws SQLException {
        Message m = getMessage(m_id);
        if(m == null) return null;
        return m.getSentAt();
    }
}
