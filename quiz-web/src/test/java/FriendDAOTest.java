import DAOs.DBConnection;
import DAOs.FriendDAO;
import Models.Friend;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FriendDAOTest {

    FriendDAO friendDAO = new FriendDAO();

    @BeforeEach
    public void setup() throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT IGNORE INTO users (username) VALUES (?), (?), (?)")) {
                stmt.setString(1, "batila");
                stmt.setString(2, "tamari");
                stmt.setString(3, "elene");
                stmt.executeUpdate();
                FriendDAO.addFriends("batila", "tamari");
                FriendDAO.addFriends("batila", "elene");
        }
    }

    @AfterEach
    public void cleanup() throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM friends WHERE (first_friend_username IN (?, ?, ?)) " +
                            "OR (second_friend_username IN (?, ?, ?))")) {
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
    public void testAddFriends() throws SQLException {
        FriendDAO.addFriends("tamari", "elene");
        assertTrue(friendDAO.areFriends("tamari", "elene"));
    }

    @Test
    public void testRemoveFriends() throws SQLException {
        boolean before = friendDAO.areFriends("batila", "tamari");
        assertTrue(before);
        friendDAO.removeFriends("batila", "tamari");
        boolean after = friendDAO.areFriends("batila", "tamari");
        assertFalse(after);
        assertTrue(friendDAO.areFriends("batila", "elene"));
    }

    @Test
    public void testAreFriends() throws SQLException {
        assertTrue(friendDAO.areFriends("batila", "tamari"));
        assertTrue(friendDAO.areFriends("tamari", "batila"));
        assertFalse(friendDAO.areFriends("tamari", "elene"));
    }

    @Test
    public void testGetFriendsOfUser() throws SQLException {
        ArrayList<Friend> friends = friendDAO.getFriendsOfUser("batila");
        boolean foundElene = false;
        for (Friend friend : friends) {
            if ((friend.getFirstFriendUsername().equals("batila") && friend.getSecondFriendUsername().equals("elene")) ||
                    (friend.getFirstFriendUsername().equals("elene") && friend.getSecondFriendUsername().equals("batila"))) {
                foundElene = true;
            }
        }
        assertTrue(foundElene);
    }
}