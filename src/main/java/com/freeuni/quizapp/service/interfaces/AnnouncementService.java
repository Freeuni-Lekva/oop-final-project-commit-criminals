package com.freeuni.quizapp.service.interfaces;

import com.freeuni.quizapp.model.Announcement;

import java.sql.SQLException;
import java.util.List;

public interface AnnouncementService {
    public List<Announcement> getAllAnnouncements() throws SQLException;
}
