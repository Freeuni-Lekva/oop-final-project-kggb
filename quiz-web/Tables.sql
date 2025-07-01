-- Drop tables in proper dependency order
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
DROP TABLE IF EXISTS announcements;
DROP TABLE IF EXISTS quizzes;
DROP TABLE IF EXISTS users;

-- Users
CREATE TABLE users (
                       username VARCHAR(64) NOT NULL PRIMARY KEY,
                       first_name VARCHAR(64) NOT NULL,
                       last_name VARCHAR(64) NOT NULL,
                       date_joined DATETIME NOT NULL,
                       encrypted_password VARCHAR(255) NOT NULL,
                       profile_picture VARCHAR(255)
);

-- User roles
CREATE TABLE private_users (
                               username VARCHAR(64) NOT NULL PRIMARY KEY,
                               FOREIGN KEY (username) REFERENCES users(username)
);

CREATE TABLE admin_users (
                             username VARCHAR(64) NOT NULL PRIMARY KEY,
                             FOREIGN KEY (username) REFERENCES users(username)
);

-- Friendships
CREATE TABLE friends (
                         first_friend_username VARCHAR(64) NOT NULL,
                         second_friend_username VARCHAR(64) NOT NULL,
                         PRIMARY KEY (first_friend_username, second_friend_username),
                         FOREIGN KEY (first_friend_username) REFERENCES users(username),
                         FOREIGN KEY (second_friend_username) REFERENCES users(username)
);

-- Friend requests
CREATE TABLE friend_requests (
                                 request_sent_from VARCHAR(64) NOT NULL,
                                 request_sent_to VARCHAR(64) NOT NULL,
                                 PRIMARY KEY (request_sent_from, request_sent_to),
                                 FOREIGN KEY (request_sent_from) REFERENCES users(username),
                                 FOREIGN KEY (request_sent_to) REFERENCES users(username)
);

-- Messages
CREATE TABLE messages (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          sent_from VARCHAR(64) NOT NULL,
                          sent_to VARCHAR(64) NOT NULL,
                          message TEXT NOT NULL,
                          title VARCHAR(255),
                          FOREIGN KEY (sent_from) REFERENCES users(username),
                          FOREIGN KEY (sent_to) REFERENCES users(username)
);

-- Achievements
CREATE TABLE achievements (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              name VARCHAR(64) NOT NULL,
                              description TEXT,
                              image_url VARCHAR(255)
);

CREATE TABLE user_achievements (
                                   username VARCHAR(64) NOT NULL,
                                   achievement_id BIGINT NOT NULL,
                                   PRIMARY KEY (username, achievement_id),
                                   FOREIGN KEY (username) REFERENCES users(username),
                                   FOREIGN KEY (achievement_id) REFERENCES achievements(id)
);

-- Quizzes
CREATE TABLE quizzes (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         quiz_name VARCHAR(120) UNIQUE NOT NULL,
                         category VARCHAR(64),
                         description TEXT,
                         creator VARCHAR(64),
                         FOREIGN KEY (creator) REFERENCES users(username)
);

-- Quiz takes history
CREATE TABLE quiz_takes_history (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    username VARCHAR(64) NOT NULL,
                                    quiz_id BIGINT NOT NULL,
                                    score BIGINT,
                                    time_taken DATETIME,
                                    time_spent TIME,
                                    FOREIGN KEY (username) REFERENCES users(username),
                                    FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
);

-- Quiz ratings
CREATE TABLE quiz_ratings (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              player VARCHAR(64) NOT NULL,
                              quiz_id BIGINT NOT NULL,
                              rating BIGINT,
                              review TEXT,
                              FOREIGN KEY (player) REFERENCES users(username),
                              FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
);

-- Questions mapping
CREATE TABLE quiz_questions (
                                quiz_id BIGINT NOT NULL,
                                question_id BIGINT NOT NULL,
                                question_type ENUM('TF', 'MC', 'FIB', 'PR') NOT NULL,
                                PRIMARY KEY (quiz_id, question_id, question_type),
                                FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
);

-- Question types
CREATE TABLE true_or_false_questions (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         question TEXT NOT NULL,
                                         correct_answer VARCHAR(250) NOT NULL,
                                         quiz_id BIGINT NOT NULL,
                                         FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
);

CREATE TABLE fill_in_the_blank_questions (
                                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             question TEXT NOT NULL,
                                             correct_answer VARCHAR(250) NOT NULL,
                                             quiz_id BIGINT NOT NULL,
                                             FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
);

CREATE TABLE multiple_choice_questions (
                                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           question TEXT NOT NULL,
                                           correct_answer VARCHAR(250) NOT NULL,
                                           incorrect_choice_1 VARCHAR(250),
                                           incorrect_choice_2 VARCHAR(250),
                                           incorrect_choice_3 VARCHAR(250),
                                           quiz_id BIGINT NOT NULL,
                                           FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
);

CREATE TABLE picture_response_questions (
                                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                            picture_url VARCHAR(255),
                                            question TEXT NOT NULL,
                                            correct_answer VARCHAR(255) NOT NULL,
                                            quiz_id BIGINT NOT NULL,
                                            FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
);

-- Reported quizzes
CREATE TABLE reported_quizzes (
                                  quiz_id BIGINT NOT NULL,
                                  username VARCHAR(64) NOT NULL,
                                  message TEXT,
                                  type VARCHAR(250),
                                  PRIMARY KEY (quiz_id, username),
                                  FOREIGN KEY (quiz_id) REFERENCES quizzes(id),
                                  FOREIGN KEY (username) REFERENCES users(username)
);

-- Announcements
CREATE TABLE announcements (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               type VARCHAR(255),
                               message TEXT,
                               title VARCHAR(255),
                               created_at DATETIME
);
