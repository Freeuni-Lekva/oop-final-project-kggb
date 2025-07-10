-- Drop tables in proper dependency order
USE assignment;

DROP TABLE IF EXISTS announcements;
DROP TABLE IF EXISTS private_users;
DROP TABLE IF EXISTS admin_users;
DROP TABLE IF EXISTS friends;
DROP TABLE IF EXISTS friend_requests;
DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS user_achievements;
DROP TABLE IF EXISTS achievements;
DROP TABLE IF EXISTS quiz_ratings;
DROP TABLE IF EXISTS quiz_takes_history;
DROP TABLE IF EXISTS quiz_questions;
DROP TABLE IF EXISTS true_or_false_questions;
DROP TABLE IF EXISTS fill_in_the_blank_questions;
DROP TABLE IF EXISTS multiple_choice_questions;
DROP TABLE IF EXISTS picture_response_questions;
DROP TABLE IF EXISTS reported_quizzes;
DROP TABLE IF EXISTS challenges;
DROP TABLE IF EXISTS quizzes;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       username VARCHAR(64) NOT NULL PRIMARY KEY,
                       first_name VARCHAR(64) NOT NULL,
                       last_name VARCHAR(64) NOT NULL,
                       date_joined DATETIME NOT NULL,
                       encrypted_password VARCHAR(255) NOT NULL,
                       profile_picture VARCHAR(255)
);

CREATE TABLE private_users (
                               username VARCHAR(64) NOT NULL PRIMARY KEY,
                               FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

CREATE TABLE admin_users (
                             username VARCHAR(64) NOT NULL PRIMARY KEY,
                             FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

CREATE TABLE friends (
                         first_friend_username VARCHAR(64) NOT NULL,
                         second_friend_username VARCHAR(64) NOT NULL,
                         PRIMARY KEY (first_friend_username, second_friend_username),
                         FOREIGN KEY (first_friend_username) REFERENCES users(username) ON DELETE CASCADE,
                         FOREIGN KEY (second_friend_username) REFERENCES users(username) ON DELETE CASCADE
);

CREATE TABLE friend_requests (
                                 request_sent_from VARCHAR(64) NOT NULL,
                                 request_sent_to VARCHAR(64) NOT NULL,
                                 PRIMARY KEY (request_sent_from, request_sent_to),
                                 FOREIGN KEY (request_sent_from) REFERENCES users(username) ON DELETE CASCADE,
                                 FOREIGN KEY (request_sent_to) REFERENCES users(username) ON DELETE CASCADE
);

CREATE TABLE messages (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          sent_from VARCHAR(64) NOT NULL,
                          sent_to VARCHAR(64) NOT NULL,
                          message TEXT NOT NULL,
                          title VARCHAR(255),
                          sent_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (sent_from) REFERENCES users(username) ON DELETE CASCADE,
                          FOREIGN KEY (sent_to) REFERENCES users(username) ON DELETE CASCADE
);

CREATE TABLE achievements (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              name VARCHAR(64) NOT NULL UNIQUE,
                              description TEXT,
                              image_url VARCHAR(255)
);

CREATE TABLE user_achievements (
                                   username VARCHAR(64) NOT NULL,
                                   achievement_id BIGINT NOT NULL,
                                   earned_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                   PRIMARY KEY (username, achievement_id),
                                   FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE,
                                   FOREIGN KEY (achievement_id) REFERENCES achievements(id) ON DELETE CASCADE
);

CREATE TABLE quizzes (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         quiz_name VARCHAR(120) NOT NULL,
                         category VARCHAR(64),
                         description TEXT,
                         creator VARCHAR(64),
                         date_created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         randomized BOOLEAN NOT NULL DEFAULT FALSE,
                         multi_page BOOLEAN NOT NULL DEFAULT FALSE,
                         immediate_score BOOLEAN NOT NULL DEFAULT FALSE,
                         FOREIGN KEY (creator) REFERENCES users(username) ON DELETE SET NULL
);

CREATE TABLE quiz_takes_history (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    username VARCHAR(64) NOT NULL,
                                    quiz_id BIGINT NOT NULL,
                                    score BIGINT,
                                    max_score BIGINT,
                                    time_taken DATETIME DEFAULT CURRENT_TIMESTAMP,
                                    time_spent TIME,
                                    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE,
                                    FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);

CREATE TABLE quiz_ratings (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              player VARCHAR(64) NOT NULL,
                              quiz_id BIGINT NOT NULL,
                              rating BIGINT CHECK (rating >= 1 AND rating <= 5),
                              review TEXT,
                              created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                              UNIQUE KEY unique_user_quiz_rating (player, quiz_id),
                              FOREIGN KEY (player) REFERENCES users(username) ON DELETE CASCADE,
                              FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);

-- Simplified approach: Remove the junction table and add question_order directly to question tables
CREATE TABLE true_or_false_questions (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         quiz_id BIGINT NOT NULL,
                                         question TEXT NOT NULL,
                                         correct_answer BOOLEAN NOT NULL,
                                         question_order INT NOT NULL DEFAULT 1,
                                         points INT NOT NULL DEFAULT 1,
                                         FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);

CREATE TABLE fill_in_the_blank_questions (
                                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             quiz_id BIGINT NOT NULL,
                                             question TEXT NOT NULL,
                                             correct_answer VARCHAR(250) NOT NULL,
                                             question_order INT NOT NULL DEFAULT 1,
                                             points INT NOT NULL DEFAULT 1,
                                             case_sensitive BOOLEAN NOT NULL DEFAULT FALSE,
                                             FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);

CREATE TABLE multiple_choice_questions (
                                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           quiz_id BIGINT NOT NULL,
                                           question TEXT NOT NULL,
                                           correct_answer VARCHAR(250) NOT NULL,
                                           incorrect_choice_1 VARCHAR(250),
                                           incorrect_choice_2 VARCHAR(250),
                                           incorrect_choice_3 VARCHAR(250),
                                           question_order INT NOT NULL DEFAULT 1,
                                           points INT NOT NULL DEFAULT 1,
                                           FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);

CREATE TABLE picture_response_questions (
                                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                            quiz_id BIGINT NOT NULL,
                                            picture_url VARCHAR(255),
                                            question TEXT NOT NULL,
                                            correct_answer VARCHAR(255) NOT NULL,
                                            question_order INT NOT NULL DEFAULT 1,
                                            points INT NOT NULL DEFAULT 1,
                                            FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);

CREATE TABLE reported_quizzes (
                                  quiz_id BIGINT NOT NULL,
                                  username VARCHAR(64) NOT NULL,
                                  message TEXT,
                                  report_type VARCHAR(250),
                                  reported_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                  status ENUM('pending', 'reviewed', 'resolved') DEFAULT 'pending',
                                  PRIMARY KEY (quiz_id, username),
                                  FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE,
                                  FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

CREATE TABLE announcements (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               type VARCHAR(255),
                               message TEXT,
                               title VARCHAR(255),
                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                               created_by VARCHAR(64),
                               active BOOLEAN DEFAULT TRUE,
                               FOREIGN KEY (created_by) REFERENCES admin_users(username) ON DELETE SET NULL
);

CREATE TABLE challenges (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            challenger VARCHAR(64) NOT NULL,
                            challenged VARCHAR(64) NOT NULL,
                            quiz_id BIGINT NOT NULL,
                            challenge_message TEXT,
                            sent_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                            status ENUM('pending', 'accepted', 'declined', 'completed') DEFAULT 'pending',
                            challenger_score BIGINT NULL,
                            challenged_score BIGINT NULL,
                            completed_at DATETIME NULL,
                            FOREIGN KEY (challenger) REFERENCES users(username) ON DELETE CASCADE,
                            FOREIGN KEY (challenged) REFERENCES users(username) ON DELETE CASCADE,
                            FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_quizzes_creator ON quizzes(creator);
CREATE INDEX idx_quizzes_category ON quizzes(category);
CREATE INDEX idx_quiz_takes_username ON quiz_takes_history(username);
CREATE INDEX idx_quiz_takes_quiz_id ON quiz_takes_history(quiz_id);
CREATE INDEX idx_messages_sent_to ON messages(sent_to);
CREATE INDEX idx_messages_sent_from ON messages(sent_from);
CREATE INDEX idx_challenges_challenged ON challenges(challenged);
CREATE INDEX idx_challenges_challenger ON challenges(challenger);
