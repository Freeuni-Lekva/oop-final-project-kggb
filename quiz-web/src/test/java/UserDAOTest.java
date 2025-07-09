import DAOs.DBConnection;
import DAOs.UserDAO;
import Models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {

    @BeforeEach
    void setup() throws Exception {
        try (Connection connection = DBConnection.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM users WHERE username IN ('tgela23', 'ekali23', 'lbati23', 'lalala', 'saba123')")) {
                stmt.executeUpdate();
            }

            String hashedPassword = UserDAO.hashPassword("1234"); // Get the SHA hash of "1234"
            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO users (username, first_name, last_name, date_joined, encrypted_password, profile_picture) " +
                            "VALUES ('tgela23', 'tamari', 'gelashvili', NOW(), ?, 'https://example.com/tamari.jpg')")) {
                stmt.setString(1, hashedPassword); // Use hashed password
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO users (username, first_name, last_name, date_joined, encrypted_password, profile_picture) " +
                            "VALUES ('ekali23', 'elene', 'kalinichenko', NOW(), ?, 'https://example.com/elene.jpg')")) {
                stmt.setString(1, UserDAO.hashPassword("5555")); // Hash "5555"
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO users (username, first_name, last_name, date_joined, encrypted_password, profile_picture) " +
                            "VALUES ('lbati23', 'luka', 'batilashvili', NOW(), ?, 'https://example.com/batila.jpg')")) {
                stmt.setString(1, UserDAO.hashPassword("6767")); // Hash "6767"
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO friends (first_friend_username, second_friend_username) VALUES ('tgela23', 'ekali23')")) {
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO friends (first_friend_username, second_friend_username) VALUES ('ekali23', 'lbati23')")) {
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO admin_users (username) VALUES ('tgela23')")) {
                stmt.executeUpdate();
            }
        }
    }

    @AfterEach
    void cleanup() throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM users WHERE username IN ('tgela23', 'ekali23', 'lbati23', 'lalala', 'saba123')")) {
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM admin_users WHERE username IN ('tgela23')"
            )){
                stmt.executeUpdate();
            }
        }
    }

    @Test
    void createUserTest() throws Exception {
        UserDAO.createUser(
                "saba123",
                "saba",
                "gogichashvili",
                "8989",
                "https://media.istockphoto.com/id/1268654991/photo/stylish-senior-man-portrait.jpg?s=612x612&w=0&k=20&c=qeDXG0xX_z-55l804-7O9GR9tEh3COAyqK-iV6f_SLY="
        );

        User user = UserDAO.getUser("saba123");
        assertNotNull(user);
        assertEquals("saba123", user.getUsername());
        assertEquals("saba", user.getFirst_name());
        assertEquals("gogichashvili", user.getLast_name());
        assertEquals("https://media.istockphoto.com/id/1268654991/photo/stylish-senior-man-portrait.jpg?s=612x612&w=0&k=20&c=qeDXG0xX_z-55l804-7O9GR9tEh3COAyqK-iV6f_SLY=", user.getProfile_picture());
        assertNotNull(user.getDate_joined());
    }

    @Test
    void deleteUserTest() throws Exception {
        UserDAO.createUser(
                "lalala",
                "lala",
                "lalashvili",
                "la2la1",
                null
        );
        UserDAO userDAO = new UserDAO();
        userDAO.deleteUser("lalala");
        User user = UserDAO.getUser("lalala");
        assertNull(user);
    }

    @Test
    void getUserTest() throws Exception {
        assertEquals("tgela23", UserDAO.getUser("tgela23").getUsername());
        assertEquals("luka", UserDAO.getUser("lbati23").getFirst_name());
        assertEquals("batilashvili", UserDAO.getUser("lbati23").getLast_name());
    }

    @Test
    void userExistsTest()  throws Exception {
        assertTrue(UserDAO.userExists("tgela23"));
        assertFalse(UserDAO.userExists("blabla"));
    }

    @Test
    void correctPassword() throws Exception {
        assertTrue(UserDAO.correctPassword("tgela23", "1234"));
        assertFalse(UserDAO.correctPassword("ekali23", "1234"));
        assertFalse(UserDAO.correctPassword("tako", "1234"));
    }

    @Test
    void getFriendsTest() throws Exception {
        ArrayList<User> friends = UserDAO.getFriends("tgela23", 10);
        assertEquals(1, friends.size());
        User friend = friends.get(0);
        assertEquals("ekali23", friend.getUsername());
    }

    @Test
    void isAdminTest() throws Exception {
        assertTrue(UserDAO.isAdmin("tgela23"));
        assertFalse(UserDAO.isAdmin("ekali23"));
    }

}
