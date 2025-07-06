package com.freeuni.quizapp.dao.interfaces;

import com.freeuni.quizapp.model.Announcement;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface AnnouncementDao {

    void addAnnouncement(Announcement a) throws SQLException;

    void deleteAnnouncement(int an_id)  throws SQLException;

    Announcement getAnnouncement(int an_id) throws SQLException;

    List<Announcement> getAllAnnouncements()  throws SQLException;

    List<Announcement> getUsersAnnouncements(int user_id)  throws SQLException;

    boolean contains(int a_id)  throws SQLException;

    public void updateAnnouncement(Announcement a) throws SQLException;
}
