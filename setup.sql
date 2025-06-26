-- Use or create database
CREATE DATABASE IF NOT EXISTS quizapp_db;
USE quizapp_db;

-- Drop tables in reverse FK dependency order
DROP TABLE IF EXISTS announcements;
DROP TABLE IF EXISTS achievements;
DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS friends;
DROP TABLE IF EXISTS friend_requests;
DROP TABLE IF EXISTS quiz_results;
DROP TABLE IF EXISTS user_answers;
DROP TABLE IF EXISTS answers;
DROP TABLE IF EXISTS questions;
DROP TABLE IF EXISTS quizzes;
DROP TABLE IF EXISTS users;

-- Users table (with bio and profile picture)
CREATE TABLE users (
                       user_id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(100) NOT NULL UNIQUE,
                       hashed_password VARCHAR(255) NOT NULL,
                       is_admin BOOLEAN DEFAULT FALSE,
                       bio TEXT,
                       profile_picture_url VARCHAR(255),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Quizzes table
CREATE TABLE quizzes (
                         quiz_id INT AUTO_INCREMENT PRIMARY KEY,
                         user_id INT NOT NULL,
                         title VARCHAR(255) NOT NULL,
                         description TEXT,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Questions table
CREATE TABLE questions (
                           question_id INT AUTO_INCREMENT PRIMARY KEY,
                           quiz_id INT NOT NULL,
                           text TEXT NOT NULL,
                           type ENUM('question_response', 'fill_in_blank', 'multiple_choice', 'picture_response') NOT NULL,
                           image_url VARCHAR(255),
                           FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id) ON DELETE CASCADE
);

-- Answers table
CREATE TABLE answers (
                         answer_id INT AUTO_INCREMENT PRIMARY KEY,
                         question_id INT NOT NULL,
                         answer_text TEXT NOT NULL,
                         is_correct BOOLEAN NOT NULL,
                         FOREIGN KEY (question_id) REFERENCES questions(question_id) ON DELETE CASCADE
);

-- User Answers table
CREATE TABLE user_answers (
                              user_answer_id INT AUTO_INCREMENT PRIMARY KEY,
                              user_id INT NOT NULL,
                              question_id INT NOT NULL,
                              given_answer TEXT NOT NULL,
                              is_correct BOOLEAN NOT NULL,
                              FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                              FOREIGN KEY (question_id) REFERENCES questions(question_id) ON DELETE CASCADE
);

-- Quiz Results table
CREATE TABLE quiz_results (
                              quiz_result_id INT AUTO_INCREMENT PRIMARY KEY,
                              user_id INT NOT NULL,
                              quiz_id INT NOT NULL,
                              total_score INT NOT NULL,
                              total_questions INT NOT NULL,
                              time_taken INT, -- in seconds
                              is_practice BOOLEAN DEFAULT FALSE,
                              completed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                              FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id) ON DELETE CASCADE
);

-- Friend Requests table
CREATE TABLE friend_requests (
                                 friend_request_id INT AUTO_INCREMENT PRIMARY KEY,
                                 from_user INT NOT NULL,
                                 to_user INT NOT NULL,
                                 status ENUM('pending', 'accepted', 'rejected') DEFAULT 'pending',
                                 sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 FOREIGN KEY (from_user) REFERENCES users(user_id) ON DELETE CASCADE,
                                 FOREIGN KEY (to_user) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Friends table
CREATE TABLE friends (
                         friendship_id INT AUTO_INCREMENT PRIMARY KEY,
                         friend1_user_id INT NOT NULL,
                         friend2_user_id INT NOT NULL,
                         UNIQUE KEY unique_friendship (friend1_user_id, friend2_user_id),
                         FOREIGN KEY (friend1_user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                         FOREIGN KEY (friend2_user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Messages table
CREATE TABLE messages (
                          message_id INT AUTO_INCREMENT PRIMARY KEY,
                          from_user_id INT NOT NULL,
                          to_user_id INT NOT NULL,
                          text TEXT NOT NULL,
                          timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (from_user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                          FOREIGN KEY (to_user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Achievements table
CREATE TABLE achievements (
                              achievement_id INT AUTO_INCREMENT PRIMARY KEY,
                              user_id INT NOT NULL,
                              achievement_name ENUM(
                                  'Amateur Author',
                                  'Prolific Author',
                                  'Prodigious Author',
                                  'Quiz Machine',
                                  'I am the Greatest',
                                  'Practice Makes Perfect'
                                  ) NOT NULL,
                              quiz_id INT,
                              achieved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                              FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id) ON DELETE CASCADE
);

-- Announcements table (NEW)
CREATE TABLE announcements (
                               announcement_id INT AUTO_INCREMENT PRIMARY KEY,
                               user_id INT NOT NULL,
                               title VARCHAR(255),
                               announcement_text TEXT NOT NULL,
                               url VARCHAR(255), -- optional image/quiz link
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Insert 5 admin users
INSERT INTO users (username, hashed_password, is_admin)
VALUES
    ('lkhiz23', 'a85cce133b87c29967f0c4cce6eaf76bf5d3f68b', TRUE),
    ('lchkh23', 'f87c1ea92d312bb8be0a16dfafd375f813f8255e', TRUE),
    ('sansi23', 'a27d4f58662f66473fe3e5f50bd70c44c1513f0f', TRUE),
    ('akave23', 'ccc28cccf8128a3f57f62b46407e4aa24f57a2b7', TRUE),
    ('lbegi23', 'dccb1290851d4887f849da9f1370629056592f36', TRUE);
