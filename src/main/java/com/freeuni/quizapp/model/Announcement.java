package com.freeuni.quizapp.model;

import java.sql.Timestamp;

public class Announcement {
    private int id;
    private int user_id;
    private String title;
    private String announcement_text;
    private String url;
    private Timestamp createdAt;

    public Announcement(int id, int user_id, String title, String text, String url, Timestamp createdAt) {
        this.id = id;
        this.user_id = user_id;
        this.title = title;
        this.url = url;
        this.createdAt = createdAt;
        this.announcement_text = text;
    }

    public String getText() {
        return announcement_text;
    }

    public void setText(String text) {
        this.announcement_text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
