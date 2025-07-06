package com.freeuni.quizapp.model;

import com.freeuni.quizapp.enums.AchievementType;

import java.security.Timestamp;
import java.util.Objects;

public class Achievement {
    private int id;
    private int userId;
    private AchievementType type;
    private Timestamp achievedAt;


    public Achievement(int id, int userId, AchievementType type, Timestamp achievedAt) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.achievedAt = achievedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public AchievementType getType() {
        return type;
    }

    public void setType(AchievementType type) {
        this.type = type;
    }

    public Timestamp getAchievedAt() {
        return achievedAt;
    }

    public void setAchievedAt(Timestamp achievedAt) {
        this.achievedAt = achievedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Achievement that = (Achievement) o;
        return id == that.id && userId == that.userId && type == that.type && Objects.equals(achievedAt, that.achievedAt);
    }

}
