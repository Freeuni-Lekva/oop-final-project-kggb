import DAOs.AdminUserDAO;
import DAOs.DBConnection;
import Models.AdminUser;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AdminUserDAOTest {

    @BeforeEach
    void setup() throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT IGNORE INTO users (username) VALUES ('elene'), ('tamari'), ('batila')")) {
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM admin_users WHERE username IN ('elene', 'tamari', 'batila')")) {
                stmt.executeUpdate();
            }
        }
    }

    @AfterEach
    void cleanup() throws Exception {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM admin_users WHERE username IN ('elene', 'tamari', 'batila')")) {
            stmt.executeUpdate();
        }try (Connection conn = DBConnection.getConnection();
              PreparedStatement stmt = conn.prepareStatement(
                      "DELETE FROM users WHERE username IN ('elene', 'tamari', 'batila')")) {
            stmt.executeUpdate();
        }
    }

    @Test
    void testAddAndIsAdminUser() throws Exception {
        AdminUserDAO.addAdminUser("elene");
        assertTrue(AdminUserDAO.isAdminUser("elene"));
        assertFalse(AdminUserDAO.isAdminUser("tamari"));
        assertFalse(AdminUserDAO.isAdminUser("tako"));
    }

    @Test
    void testRemoveAdminUser() throws Exception {
        AdminUserDAO.addAdminUser("elene");
        assertTrue(AdminUserDAO.isAdminUser("elene"));
        AdminUserDAO.removeAdminUser("elene");
        assertFalse(AdminUserDAO.isAdminUser("elene"));
    }

    @Test
    void testGetAllAdminUsers() throws Exception {
        AdminUserDAO.addAdminUser("tamari");
        AdminUserDAO.addAdminUser("batila");
        List<AdminUser> users = AdminUserDAO.getAllAdminUsers();
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("tamari")));
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("batila")));
    }
}
