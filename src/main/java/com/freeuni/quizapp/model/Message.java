package com.freeuni.quizapp.model;

import com.freeuni.quizapp.enums.MessageType;

import java.security.Timestamp;

public class Message {
        private int id;
        private int senderId;
        private int receiverId;
        private MessageType type;
        private String content;
        private boolean isRead;
        private Timestamp sentAt;

    public Message(int id, int senderId, int receiverId, MessageType type, String content, boolean isRead, Timestamp sentAt) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.type = type;
        this.content = content;
        this.isRead = isRead;
        this.sentAt = sentAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Timestamp getSentAt() {
        return sentAt;
    }

    public void setSentAt(Timestamp sentAt) {
        this.sentAt = sentAt;
    }
}
