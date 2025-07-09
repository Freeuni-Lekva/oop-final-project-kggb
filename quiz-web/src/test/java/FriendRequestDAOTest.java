import DAOs.DBConnection;
import DAOs.FriendRequestDAO;
import Models.FriendRequest;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FriendRequestDAOTest {

    @BeforeEach
    public void setup() throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                    "INSERT IGNORE INTO users (username) VALUES (?), (?), (?)")) {
                stmt.setString(1, "batila");
                stmt.setString(2, "tamari");
                stmt.setString(3, "elene");
                stmt.executeUpdate();

                FriendRequestDAO.addFriendRequest("batila", "tamari");
                FriendRequestDAO.addFriendRequest("batila", "elene");
        }
    }

    @AfterEach
    public void cleanup() throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM friend_requests WHERE (request_sent_from IN (?, ?, ?)) " +
                            "OR (request_sent_to IN (?, ?, ?))")) {
                stmt.setString(1, "batila");
                stmt.setString(2, "tamari");
                stmt.setString(3, "elene");
                stmt.setString(4, "batila");
                stmt.setString(5, "tamari");
                stmt.setString(6, "elene");
                stmt.executeUpdate();

            try (PreparedStatement stmnt = conn.prepareStatement(
                    "DELETE FROM users WHERE username IN (?, ?, ?)")) {
                stmnt.setString(1, "batila");
                stmnt.setString(2, "tamari");
                stmnt.setString(3, "elene");
                stmnt.executeUpdate();
            }
        }
    }

    @Test
    public void testAddFriendRequest() throws SQLException {
        FriendRequestDAO.addFriendRequest("tamari", "elene");
        assertTrue(FriendRequestDAO.friendRequestExists("tamari", "elene"));
    }

    @Test
    public void testRemoveFriendRequest() throws SQLException {
        boolean before = FriendRequestDAO.friendRequestExists("batila", "tamari");
        assertTrue(before);
        FriendRequestDAO.removeFriendRequest("batila", "tamari");
        boolean after = FriendRequestDAO.friendRequestExists("batila", "tamari");
        assertFalse(after);
        assertTrue(FriendRequestDAO.friendRequestExists("batila", "elene"));
    }

    @Test
    public void testFriendRequestExists() throws SQLException {
        assertTrue(FriendRequestDAO.friendRequestExists("batila", "tamari"));
        assertFalse(FriendRequestDAO.friendRequestExists("tamari", "batila"));
    }

    @Test
    public void testGetFriendRequestsToUser() throws SQLException {
        ArrayList<FriendRequest> requests = FriendRequestDAO.getFriendRequestsToUser("tamari");
        boolean foundRequest = false;
        for (FriendRequest r : requests) {
            if (r.getRequestFromUsername().equals("batila") && r.getRequestToUsername().equals("tamari")) {
                foundRequest = true;
            }
        }
        assertTrue(foundRequest);
    }

    @Test
    public void testGetFriendRequestsFromUser() throws SQLException {
        ArrayList<FriendRequest> requests = FriendRequestDAO.getFriendRequestsFromUser("batila");
        boolean foundElene = false;
        for (FriendRequest r : requests) {
            if (r.getRequestFromUsername().equals("batila") && r.getRequestToUsername().equals("elene")) {
                foundElene = true;
            }
        }
        assertTrue(foundElene);
    }
}