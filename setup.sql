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
                          type ENUM('challenge', 'friend_request', 'text') DEFAULT 'text',
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
                                  'Amateur_Author',
                                  'Prolific_Author',
                                  'Prodigious_Author',
                                  'Quiz_Machine',
                                  'I_am_the_Greatest',
                                  'Practice_Makes_Perfect'
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


-- Insert the Football Quiz
INSERT INTO quizzes (user_id, title, description)
VALUES
    (1, 'World Football Trivia', 'Test your football quiz knowledge with the questions of mixed difficulty.');

-- Insert questions for the quiz
INSERT INTO questions (quiz_id, text, type)
VALUES
    (1, 'Who won the FIFA World Cup in 2018?', 'multiple_choice'),
    (1, 'Who is the player known as “The Hand of God”?', 'multiple_choice'),
    (1, 'Name the football club where Khvicha Kvaratskhelia started his professional career.', 'multiple_choice'),
    (1, 'Which country has won the most FIFA World Cups?', 'multiple_choice'),
    (1, 'Which of the following two clubs share the same stadium?', 'multiple_choice');

-- Insert answers for first question (multiple_choice)
INSERT INTO answers (question_id, answer_text, is_correct)
VALUES
    (1, 'Brazil', FALSE),
    (1, 'Germany', FALSE),
    (1, 'France', TRUE),
    (1, 'Argentina', FALSE),

    (2, 'Maradona', TRUE),
    (2, 'Barcola', FALSE),
    (2, 'Di maria', FALSE),
    (2, 'Messi', FALSE),

    (3, 'FC Rustavi', FALSE),
    (3, 'Lokomotiv Moskov', FALSE),
    (3, 'Dinamo Tbilisi', TRUE),
    (3, 'PSG', FALSE),

    (4, 'Germany', FALSE),
    (4, 'Italy', FALSE),
    (4, 'Argentina', FALSE),
    (4, 'Brazil', TRUE),

    (5, 'AC Milan & FC Internazionale Milano', TRUE),
    (5, 'Bayern Munich & TSV 1860 Munich', FALSE),
    (5, 'Atletico Madrid & Rayo Vallecano', FALSE),
    (5, 'Dinamo Tbilisi & FC Iberia 1999', FALSE);


-- Insert NBA Basketball Quiz
INSERT INTO quizzes (user_id, title, description)
VALUES
    (2, 'NBA Legends & History', 'Test your knowledge of NBA basketball history, legends, and current stars.');

-- Insert questions for the NBA quiz
INSERT INTO questions (quiz_id, text, type)
VALUES
    (2, 'Who holds the record for most NBA championships won as a player?', 'multiple_choice'),
    (2, 'Which team has won the most NBA championships?', 'multiple_choice'),
    (2, 'Who is known as "His Airness"?', 'multiple_choice'),
    (2, 'What is the maximum number of players on an NBA roster during the regular season?', 'multiple_choice'),
    (2, 'Which player scored 100 points in a single NBA game?', 'multiple_choice');

-- Insert answers for NBA quiz questions
INSERT INTO answers (question_id, answer_text, is_correct)
VALUES
    (6, 'Michael Jordan', FALSE),
    (6, 'Bill Russell', TRUE),
    (6, 'Kareem Abdul-Jabbar', FALSE),
    (6, 'Magic Johnson', FALSE),

    (7, 'Los Angeles Lakers', FALSE),
    (7, 'Boston Celtics', TRUE),
    (7, 'Chicago Bulls', FALSE),
    (7, 'San Antonio Spurs', FALSE),

    (8, 'LeBron(Our Glorious King) James', FALSE),
    (8, 'Kobe Bryant', FALSE),
    (8, 'Michael Jordan', TRUE),
    (8, 'Magic Johnson', FALSE),

    (9, '12', FALSE),
    (9, '13', FALSE),
    (9, '15', TRUE),
    (9, '17', FALSE),

    (10, 'Kobe Bryant', FALSE),
    (10, 'Michael Jordan', FALSE),
    (10, 'Wilt Chamberlain', TRUE),
    (10, 'Stephen Curry', FALSE);



-- Insert Literature Question Response Quizzes
INSERT INTO quizzes (user_id, title, description)
VALUES
    (3, 'Classic Authors Quiz', 'Test your knowledge of famous authors and their works.'),
    (4, 'Literary Characters Quiz', 'Identify famous characters from literature.');

-- Insert questions for quizzes
INSERT INTO questions (quiz_id, text, type)
VALUES
    (3, 'Who wrote "Romeo and Juliet"?', 'question_response'),
    (3, 'Who is the author of "Pride and Prejudice"?', 'question_response'),
    (3, 'Which author created the character Sherlock Holmes?', 'question_response'),
    (3, 'Who wrote "To Kill a Mockingbird"?', 'question_response'),
    (3, 'Which Russian author wrote "Crime and Punishment"?', 'question_response'),

    (4, 'What is the name of Harry Potter\'s owl?', 'question_response'),
    (4, 'Who is the protagonist in "The Great Gatsby"?', 'question_response'),
    (4, 'What is the name of Atticus Finch\'s daughter in "To Kill a Mockingbird"?', 'question_response'),
    (4, 'Who is the captain in "Moby Dick"?', 'question_response'),
    (4, 'What is the name of the monster in Mary Shelley\'s "Frankenstein"?', 'question_response');

-- Insert answers for Classic Authors Quiz (text-based)
INSERT INTO answers (question_id, answer_text, is_correct)
VALUES
    (11, 'William Shakespeare', TRUE),
    (11, 'Shakespeare', TRUE),

    (12, 'Jane Austen', TRUE),
    (12, 'Austen', TRUE),

    (13, 'Arthur Conan Doyle', TRUE),
    (13, 'Conan Doyle', TRUE),
    (13, 'Doyle', TRUE),

    (14, 'Harper Lee', TRUE),
    (14, 'Lee', TRUE),

    (15, 'Fyodor Dostoevsky', TRUE),
    (15, 'Dostoevsky', TRUE),

-- Insert answers for Literary Characters Quiz (text-based)
    (16, 'Hedwig', TRUE),

    (17, 'Jay Gatsby', TRUE),
    (17, 'Gatsby', TRUE),

    (18, 'Scout Finch', TRUE),
    (18, 'Scout', TRUE),
    (18, 'Jean Louise Finch', TRUE),

    (19, 'Captain Ahab', TRUE),
    (19, 'Ahab', TRUE),

    (20, 'Frankenstein\'s monster', TRUE),
    (20, 'The monster', TRUE),
    (20, 'The creature', TRUE);




-- Insert GEOGRAPHY quiz
INSERT INTO quizzes (user_id, title, description)
VALUES
    (4, 'Geography quiz', 'Easy geography quiz to challenge yourself!');

INSERT INTO questions (quiz_id, text, type)
VALUES
    (5, 'Which is the longest river in the world?', 'multiple_choice'),
    (5, 'Which country has the most time zones?', 'multiple_choice'),
    (5, 'With how many countries does Georgia share a border?','multiple_choice'),
    (5, 'What is the capital of Canada?', 'multiple_choice'),
    (5, 'Which desert is the largest in the world by area?', 'multiple_choice'),
    (5, 'Which continent has the most countries?', 'multiple_choice'),
    (5, 'Which country has the flag with an unusual form?', 'multiple_choice'),
    (5, 'Which is one of the georgian regions occupied by russia?', 'multiple_choice');

-- Inserts answers for the fifth quiz
INSERT INTO answers (question_id, answer_text, is_correct)
VALUES
    (21, 'Nile', TRUE),
    (21, 'Amazon', FALSE),
    (21, 'Mississippi', FALSE),
    (21, 'Congo', FALSE),

    (22, 'Russia', FALSE),
    (22, 'United States', FALSE),
    (22, 'China', FALSE),
    (22, 'France', TRUE),

    (23, '3', FALSE),
    (23, '4', TRUE),
    (23, '5', FALSE),
    (23, '6', FALSE),

    (24, 'Toronto', FALSE),
    (24, 'Ottawa', TRUE),
    (24, 'Vancouver', FALSE),
    (24, 'Montreal', FALSE),

    (25, 'Sahara', FALSE),
    (25, 'Arabian', FALSE),
    (25, 'Gobi', FALSE),
    (25, 'Antarctic', TRUE),

    (26, 'Asia', FALSE),
    (26, 'Africa', TRUE),
    (26, 'Europe', FALSE),
    (26, 'North America', FALSE),

    (27, 'Bangladesh', FALSE),
    (27, 'Sri-Lanka', FALSE),
    (27, 'Nepal', TRUE),
    (27, 'Georgia', FALSE),

    (28, 'Abkhazia', TRUE),
    (28, 'Adjara', FALSE),
    (28, 'Guria', FALSE),
    (28, 'Svaneti', FALSE);


INSERT INTO quizzes (user_id, title, description)
VALUES
    (5, 'History quiz', 'Do you want to travel through time? - Take this quiz!');

INSERT INTO questions (quiz_id, text, type)
VALUES
    (6, 'The Great Wall of ________ was built to protect against invasions.', 'fill_in_blank'),
    (6, 'Queen ________ is one of the most famous rulers in Georgian history and reigned during the country\'s cultural peak.', 'fill_in_blank'),
    (6, 'The pyramids of ________ are one of the Seven Wonders of the Ancient World.','fill_in_blank'),
    (6, 'Nelson Mandela was the first Black president of ________.', 'fill_in_blank'),
    (6, 'World War II ended in the year ________.', 'fill_in_blank');

-- Inserts answers for the sixth quiz
INSERT INTO answers (question_id, answer_text, is_correct)
VALUES
    (29, 'China', TRUE),

    (30, 'Tamar', TRUE),

    (31, 'Egypt', TRUE),

    (32, 'South Africa', TRUE),

    (33, '1945', TRUE);



INSERT INTO quizzes (user_id, title, description)
VALUES
    (3, 'Movies', 'Test your reel knowledge!');

INSERT INTO questions (quiz_id, text, type)
VALUES
    (7, 'The movie Interstellar was directed by ________.', 'fill_in_blank'),
    (7, 'The character Jack Sparrow appears in the Pirates of the ________ series.', 'fill_in_blank'),
    (7, 'In The Matrix, the main character Neo is played by ________.','fill_in_blank'),
    (7, 'The wizarding school in Harry Potter is called ________ .', 'fill_in_blank'),
    (7, 'In Breakfast at Tiffany’s, the lead character Holly Golightly is played by ________ Hepburn.', 'fill_in_blank');

-- Insert answers for the 7th quiz
INSERT INTO answers (question_id, answer_text, is_correct)
VALUES
    (34, 'Christopher Nolan', TRUE),

    (35, 'Caribbean', TRUE),

    (36, 'Keanu Reeves', TRUE),

    (37, 'Hogwarts', TRUE),

    (38, 'Audrey', TRUE);

INSERT INTO quizzes (user_id, title, description)
VALUES
    (1, 'LOL Esports Quiz', 'Test your League Of Legends Esports Knowledge!');

INSERT INTO questions (quiz_id, text, type, image_url)
VALUES
    (8, 'Which professional League of Legends team won the World Championship and got the IG Kai’Sa skin as a reward?', 'multiple_choice', 'images/kaisa.jpg'),
    (8, 'In the 2022 Worlds Finals, which champion did DRX''s deft lock in during the iconic game 5 that won him the title?', 'multiple_choice', 'images/deft.jpg'),
    (8, 'During EU LCS Summer Semifinals, which unexpected champion did Caps play midlane that shocked the entire world?', 'multiple_choice', 'images/caps.jpg'),
    (8, 'At which event did G2 Esports beat SKT T1 in a 5 game series to reach a final?', 'multiple_choice', 'images/g2.jpg');

INSERT INTO answers (question_id, answer_text, is_correct)
VALUES
    (39, 'Infinity Gaming', FALSE),
    (39, 'Incredible Geniuses', FALSE),
    (39, 'Immortal Guardians', FALSE),
    (39, 'Invictus Gaming', TRUE),
    (40, 'Lucian', FALSE),
    (40, 'Varus', FALSE),
    (40, 'Caitlyn', TRUE),
    (40, 'Ezreal', FALSE),
    (41, 'Vayne', TRUE),
    (41, 'Lucian', FALSE),
    (41, 'Karma', FALSE),
    (41, 'Lulu', FALSE),
    (42, 'MSI 2022', FALSE),
    (42, 'Worlds 2021', FALSE),
    (42, 'Worlds 2019', FALSE),
    (42, 'MSI 2019', TRUE);