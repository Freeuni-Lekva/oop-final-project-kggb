import DAOs.DBConnection;
import DAOs.PrivateUserDAO;
import Models.PrivateUser;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PrivateUserDAOTest {

    @BeforeEach
    void setup() throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT IGNORE INTO users (username, first_name, last_name, date_joined, encrypted_password) " +
                            "VALUES ('ekali', 'elene', 'kalinichenko', NOW(), 'root'), " +
                            "('batila', 'luka', 'batilashvili', NOW(), 'root')")) {
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM private_users WHERE username IN ('ekali', 'batila')")) {
                stmt.executeUpdate();
            }
        }
    }

    @AfterEach
    void cleanup() throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM private_users WHERE username IN ('ekali', 'batila')")) {
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM users WHERE username IN ('ekali', 'batila')")) {
                stmt.executeUpdate();
            }
        }
    }

    @Test
    void testAddAndIsPrivateUser() throws Exception {
        PrivateUserDAO.addPrivateUser("ekali");

        assertTrue(PrivateUserDAO.isPrivateUser("ekali"));
        assertFalse(PrivateUserDAO.isPrivateUser("batila"));
    }

    @Test
    void testRemovePrivateUser() throws Exception {
        PrivateUserDAO.addPrivateUser("ekali");
        assertTrue(PrivateUserDAO.isPrivateUser("ekali"));

        PrivateUserDAO.removePrivateUser("ekali");
        assertFalse(PrivateUserDAO.isPrivateUser("ekali"));
    }

    @Test
    void testGetAllPrivateUsers() throws Exception {
        PrivateUserDAO.addPrivateUser("ekali");
        PrivateUserDAO.addPrivateUser("batila");

        List<PrivateUser> users = PrivateUserDAO.getAllPrivateUsers();
        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("ekali")));
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("batila")));
    }
}
