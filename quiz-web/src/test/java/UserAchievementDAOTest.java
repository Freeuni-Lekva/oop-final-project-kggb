import DAOs.DBConnection;
import DAOs.UserAchievementDAO;
import Models.UserAchievement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class UserAchievementDAOTest {

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
            }

            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT IGNORE INTO achievements (id, name, description, image_url) VALUES (?, ?, ?, ?)")) {
                stmt.setLong(1, 1234);
                stmt.setString(2, "fastest");
                stmt.setString(3, "the fastest");
                stmt.setString(4, "fastestUrl");
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT IGNORE INTO achievements (id, name, description, image_url) VALUES (?, ?, ?, ?)")) {
                stmt.setLong(1, 5678);
                stmt.setString(2, "smartest");
                stmt.setString(3, "the smartest");
                stmt.setString(4, "smartestUrl");
                stmt.executeUpdate();
            }
            UserAchievementDAO.addUserAchievement("batila", 1234);
        }
    }

    @AfterEach
    public void cleanup() throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM user_achievements WHERE username = ?")) {
                stmt.setString(1, "batila");
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM achievements WHERE id IN (?, ?)")) {
                stmt.setLong(1, 1234);
                stmt.setLong(2, 5678);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM users WHERE username = ?")) {
                stmt.setString(1, "batila");
                stmt.executeUpdate();
            }
        }
    }

    @Test
    public void testAddUserAchievement() throws SQLException {
        UserAchievementDAO.addUserAchievement("batila", 5678);
        ArrayList<UserAchievement> achievements = new UserAchievementDAO().getUserAchievements("batila");
        boolean found = false;
        for (UserAchievement a : achievements) {
            if (a.getUsername().equals("batila") && a.getAchievementId() == 5678) {
                found = true;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testRemoveUserAchievement() throws SQLException {
        UserAchievementDAO.removeUserAchievement("batila", 1234);
        ArrayList<UserAchievement> achievements = new UserAchievementDAO().getUserAchievements("batila");
        boolean found = false;
        for (UserAchievement a : achievements) {
            if (a.getUsername().equals("batila") && a.getAchievementId() == 1234) {
                found = true;
            }
        }
        assertFalse(found);
    }

    @Test
    public void testGetUserAchievements() throws SQLException {
        ArrayList<UserAchievement> achievements = new UserAchievementDAO().getUserAchievements("batila");
        boolean found = false;
        for (UserAchievement a : achievements) {
            if (a.getUsername().equals("batila") && a.getAchievementId() == 1234) {
                found = true;
            }
        }
        assertTrue(found);
    }
}