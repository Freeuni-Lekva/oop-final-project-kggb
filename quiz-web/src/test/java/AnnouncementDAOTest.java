import DAOs.AnnouncementDAO;
import DAOs.DBConnection;
import Models.Announcement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnnouncementDAOTest {

    @BeforeEach
    public void setup() throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                    "INSERT IGNORE INTO users (username) VALUES (?)")) {
                stmt.setString(1, "batila");
                stmt.executeUpdate();

            try (PreparedStatement stmnt = conn.prepareStatement(
                    "INSERT IGNORE INTO admin_users (username) VALUES (?)")) {
                stmnt.setString(1, "batila");
                stmnt.executeUpdate();
            }
                AnnouncementDAO.addAnnouncement("comics", "wanda", "marvel", "batila");
        }
    }

    @AfterEach
    public void cleanup() throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {

            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM announcements WHERE created_by = ?")) {
                stmt.setString(1, "batila");
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM admin_users WHERE username = ?")) {
                stmt.setString(1, "batila");
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
    public void testAddAnnouncement() throws SQLException {
        AnnouncementDAO.addAnnouncement("artists", "new song", "the beatles", "batila");
        ArrayList<Announcement> announcements = AnnouncementDAO.getAnnouncements();
        boolean found = false;
        for (Announcement a : announcements) {
            if (a.getMessage().equals("new song") &&
                    a.getTitle().equals("the beatles") &&
                    a.getCreatedBy().equals("batila")) {
                found = true;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testDeleteAnnouncement() throws SQLException {
        ArrayList<Announcement> announcements = AnnouncementDAO.getAnnouncements();
        long id = announcements.get(0).getId();
        AnnouncementDAO.removeAnnouncement(id);
        ArrayList<Announcement> after = AnnouncementDAO.getAnnouncements();
        boolean found = false;
        for (Announcement a : after) {
            if (a.getId() == id) {
                found = true;
            }
        }
        assertFalse(found);
    }

    @Test
    public void testUpdateAnnouncement() throws SQLException {
        ArrayList<Announcement> announcements = AnnouncementDAO.getAnnouncements();
        Announcement announcement = announcements.get(0);
        long id = announcement.getId();
        AnnouncementDAO.updateAnnouncement(id, "dishes", "new restaurant",
                "khinkali", "batila");

        ArrayList<Announcement> updated = AnnouncementDAO.getAnnouncements();
        boolean foundUpdated = false;
        for (Announcement a : updated) {
            if (a.getId() == id && a.getMessage().equals("new restaurant") && a.getTitle().equals("khinkali") &&
                    a.getType().equals("dishes") && a.getCreatedBy().equals("batila")) {
                foundUpdated = true;
            }
        }
        assertTrue(foundUpdated);
    }

    @Test
    public void testGetAllAnnouncements() throws SQLException {
        ArrayList<Announcement> announcements = AnnouncementDAO.getAnnouncements();
        boolean found = false;
        for (Announcement a : announcements) {
            if (a.getCreatedBy().equals("batila")) {
                found = true;
            }
        }
        assertTrue(found);
    }

}
