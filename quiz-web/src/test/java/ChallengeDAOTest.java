import DAOs.ChallengeDAO;
import DAOs.DBConnection;
import Models.Challenge;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChallengeDAOTest {
    @BeforeEach
    public void setup() throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT IGNORE INTO users (username, first_name, last_name, date_joined, encrypted_password) VALUES (?, ?, ?, NOW(), ?)")) {
                stmt.setString(1, "batila");
                stmt.setString(2, "luka");
                stmt.setString(3, "batila");
                stmt.setString(4, "root");
                stmt.executeUpdate();

                stmt.setString(1, "ekali");
                stmt.setString(2, "elene");
                stmt.setString(3, "ekali");
                stmt.setString(4, "password");
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT IGNORE INTO quizzes (id, quiz_name, category, description, creator) VALUES (?, ?, ?, ?, ?)")) {
                stmt.setLong(1, 12345);
                stmt.setString(2, "test");
                stmt.setString(3, "music");
                stmt.setString(4, "description");
                stmt.setString(5, "batila");
                stmt.executeUpdate();
            }

            ChallengeDAO.addChallenge("batila", "ekali", 12345, "hahaha",
                    0, 0, null);
        }
    }

    @AfterEach
    public void cleanup() throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM challenges WHERE challenger = ? OR challenged = ?")) {
                stmt.setString(1, "batila");
                stmt.setString(2, "ekali");
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM quizzes WHERE id = ?")) {
                stmt.setLong(1, 12345);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM users WHERE username IN (?, ?)")) {
                stmt.setString(1, "batila");
                stmt.setString(2, "ekali");
                stmt.executeUpdate();
            }
        }
    }

    @Test
    public void testAddChallenge() throws SQLException {
        ChallengeDAO.addChallenge(
                "batila", "ekali", 12345,
                "okokok", 5, 10, null
        );

        ArrayList<Challenge> challenges = ChallengeDAO.getAllChallenges();
        boolean found = false;
        for (Challenge c : challenges) {
            if (c.getChallengeMessage().equals("okokok") && c.getChallenger().equals("batila")
                    && c.getChallenged().equals("ekali") && c.getQuizID() == 12345) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testRemoveChallenge() throws SQLException {
        ArrayList<Challenge> challenges = ChallengeDAO.getAllChallenges();
        long id = challenges.get(0).getId();

        ChallengeDAO.removeChallenge(id);

        ArrayList<Challenge> after = ChallengeDAO.getAllChallenges();
        boolean found = false;
        for (Challenge c : after) {
            if (c.getId() == id) {
                found = true;
                break;
            }
        }
        assertFalse(found);
    }

    @Test
    public void testChangeChallengeStatus() throws SQLException {
        ArrayList<Challenge> challenges = ChallengeDAO.getAllChallenges();
        long id = challenges.get(0).getId();

        ChallengeDAO.changeChallengeStatus(id, "accepted");

        ArrayList<Challenge> updated = ChallengeDAO.getAllChallenges();
        boolean found = false;
        for (Challenge c : updated) {
            if (c.getId() == id && "accepted".equals(c.getStatus())) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testGetAllChallenges() throws SQLException {
        ArrayList<Challenge> challenges = ChallengeDAO.getAllChallenges();
        boolean found = false;
        for (Challenge c : challenges) {
            if (c.getChallenger().equals("batila") && c.getChallenged().equals("ekali")) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }
}