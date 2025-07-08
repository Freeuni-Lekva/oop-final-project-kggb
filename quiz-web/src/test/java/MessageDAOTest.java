import DAOs.DBConnection;
import DAOs.MessageDAO;
import Models.Message;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MessageDAOTest {

    @BeforeEach
    public void setup() throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                    "INSERT IGNORE INTO users (username) VALUES (?), (?), (?)")) {
                stmt.setString(1, "batila");
                stmt.setString(2, "tamari");
                stmt.setString(3, "elene");
                stmt.executeUpdate();
                MessageDAO.addMessage("batila", "tamari", "hola tamar", "hola");
                MessageDAO.addMessage("batila", "elene", "hola elene", "hola");
        }
    }

    @AfterEach
    public void cleanup() throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM messages WHERE (sent_from IN (?, ?, ?)) OR (sent_to IN (?, ?, ?))")) {
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
    public void testAddMessage() throws SQLException {
        MessageDAO.addMessage("tamari", "elene", "hola elene", "hola");
        ArrayList<Message> messages = MessageDAO.messagesSentFromUser("tamari");
        boolean found = false;
        for (Message m : messages) {
            if (m.getSentTo().equals("elene") && m.getMessage().equals("hola elene") && m.getTitle().equals("hola")) {
                found = true;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testRemoveMessage() throws SQLException {
        ArrayList<Message> messages = MessageDAO.messagesSentFromUser("batila");
        assertFalse(messages.isEmpty());
        long id = messages.get(0).getId();
        MessageDAO.removeMessage(id);
        ArrayList<Message> messagesAfter = MessageDAO.messagesSentFromUser("batila");
        boolean idStillExists = false;
        for (Message m : messagesAfter) {
            if (m.getId() == id) {
                idStillExists = true;
            }
        }
        assertFalse(idStillExists);
    }

    @Test
    public void testMessagesSentToUser() throws SQLException {
        ArrayList<Message> messagesToTamari = MessageDAO.messagesSentToUser("tamari");
        boolean found = false;
        for (Message m : messagesToTamari) {
            if (m.getSentFrom().equals("batila") && m.getSentTo().equals("tamari")) {
                found = true;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testMessagesSentFromUser() throws SQLException {
        ArrayList<Message> messagesFromElene = MessageDAO.messagesSentFromUser("batila");
        boolean foundElene = false;
        for (Message m : messagesFromElene) {
            if (m.getSentTo().equals("elene")) {
                foundElene = true;
            }
        }
        assertTrue(foundElene);
    }
}