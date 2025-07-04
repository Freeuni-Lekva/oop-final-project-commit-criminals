package com.freeuni.quizapp.model;
import java.sql.Timestamp;
import java.util.List;


public class User {
    private int id;
    private String username;
    private String hashedPassword;
    private boolean isAdmin;
    private Timestamp createdAt;
    private String bio;
    private String profilePictureUrl;
    private List<Quiz> quizzesCreated;
    private List<QuizResult> quizzesTaken;
    private List<User> friends;

    public User(int id, String username, String hashed_password, boolean isAdmin, Timestamp createdAt, String bio, String profilePictureUrl) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashed_password;
        this.isAdmin = isAdmin;
        this.createdAt = createdAt;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
    }

    public List<Quiz> getQuizzesCreated() {
        return quizzesCreated;
    }

    public void setQuizzesCreated(List<Quiz> quizzesCreated) {
        this.quizzesCreated = quizzesCreated;
    }

    public List<QuizResult> getQuizzesTaken() {
        return quizzesTaken;
    }

    public void setQuizzesTaken(List<QuizResult> quizzesTaken) {
        this.quizzesTaken = quizzesTaken;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getBio() {
        return bio;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
