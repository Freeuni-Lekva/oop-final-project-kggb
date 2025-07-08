import DAOs.AchievementDAO;
import DAOs.DBConnection;
import Models.Achievement;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AchievementDAOTest {

    @BeforeEach
    public void setup() throws SQLException {
        try (Connection connection = DBConnection.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM achievements WHERE name IN ('Veteran', 'Champion', 'Quiz Master')"
            )) {
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO achievements (id, name, description, image_url) VALUES " +
                            "(1, 'Veteran', 'Completed 100 quizzes', 'https://example.com/veteran.png')," +
                            "(2, 'Champion', 'Top score in 10 quizzes', 'https://example.com/champion.png')," +
                            "(3, 'Quiz Master', 'Created 20 quizzes', 'https://example.com/master.png')"
            )) {
                stmt.executeUpdate();
            }
        }
    }

    @AfterEach
    public void cleanup() throws SQLException {
        try (Connection connection = DBConnection.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM achievements WHERE name IN ('Veteran', 'Champion', 'Quiz Master', 'New Star', 'Updated Title')"
            )) {
                stmt.executeUpdate();
            }
        }
    }

    @Test
    public void addAchievementTest() throws SQLException {
        AchievementDAO.addAchievement("New Star", "Took first quiz", "https://example.com/newstar.png");

        Achievement achievement = AchievementDAO.getAchievementByName("New Star");
        assertNotNull(achievement);
        assertEquals("New Star", achievement.getName());
        assertEquals("Took first quiz", achievement.getDescription());
        assertEquals("https://example.com/newstar.png", achievement.getImageUrl());
    }

    @Test
    public void getAchievementByNameTest() throws SQLException {
        Achievement achievement = AchievementDAO.getAchievementByName("Champion");
        assertNotNull(achievement);
        assertEquals("Champion", achievement.getName());
        assertEquals("Top score in 10 quizzes", achievement.getDescription());
    }

    @Test
    public void getAllAchievementsTest() throws SQLException {
        List<Achievement> achievements = AchievementDAO.getAchievements();
        assertTrue(achievements.size() >= 3); // Should include at least those inserted in setup
        assertTrue(achievements.stream().anyMatch(a -> a.getName().equals("Veteran")));
        assertTrue(achievements.stream().anyMatch(a -> a.getName().equals("Quiz Master")));
    }

    @Test
    public void updateAchievementTest() throws SQLException {
        Achievement achievement = AchievementDAO.getAchievementByName("Veteran");
        assertNotNull(achievement);

        AchievementDAO.updateAchievement(achievement.getId(), "Updated Title", "Updated description", "https://example.com/updated.png");

        Achievement updated = AchievementDAO.getAchievementByName("Updated Title");
        assertNotNull(updated);
        assertEquals("Updated Title", updated.getName());
        assertEquals("Updated description", updated.getDescription());
        assertEquals("https://example.com/updated.png", updated.getImageUrl());
    }

    @Test
    public void deleteAchievementTest() throws SQLException {
        AchievementDAO.addAchievement("Temp Award", "To be deleted", "https://example.com/temp.png");
        Achievement achievement = AchievementDAO.getAchievementByName("Temp Award");
        assertNotNull(achievement);

        AchievementDAO.deleteAchievement(achievement.getId());

        Achievement deleted = AchievementDAO.getAchievementByName("Temp Award");
        assertNull(deleted);
    }
}
