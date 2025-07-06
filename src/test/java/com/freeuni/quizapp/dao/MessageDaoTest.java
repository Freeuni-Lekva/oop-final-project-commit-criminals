package com.freeuni.quizapp.dao;

import com.freeuni.quizapp.dao.impl.FriendSystemDaoImpl;
import com.freeuni.quizapp.dao.impl.MessageDaoImpl;
import com.freeuni.quizapp.enums.MessageType;
import com.freeuni.quizapp.model.Message;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled("This is an incomplete file - not testing in CI")
public class MessageDaoTest {
    private static Connection connection;
    private static MessageDaoImpl messageDaoImpl;

    private static final Timestamp TIMESTAMP = Timestamp.valueOf("2025-12-12 03:50:38");
    private static final Message message = new Message(4, 1, 3, MessageType.text, "Nothing, wbu?", false, TIMESTAMP);

    @BeforeAll
    public static void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE users (" +
                "user_id INT AUTO_INCREMENT PRIMARY KEY," +
                "username VARCHAR(100) NOT NULL UNIQUE," +
                "hashed_password VARCHAR(255) NOT NULL," +
                "is_admin BOOLEAN DEFAULT FALSE," +
                "bio TEXT," +
                "profile_picture_url VARCHAR(255)," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")");

        statement.executeUpdate("CREATE TABLE messages (" +
                "message_id INT AUTO_INCREMENT PRIMARY KEY," +
                "from_user_id INT NOT NULL," +
                "to_user_id INT NOT NULL," +
                "text TEXT NOT NULL," +
                "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (from_user_id) REFERENCES users(user_id) ON DELETE CASCADE," +
                "FOREIGN KEY (to_user_id) REFERENCES users(user_id) ON DELETE CASCADE" +
                ")");

        statement.close();
    }

    @BeforeEach
    public void setUpEach() throws SQLException {
        messageDaoImpl = new MessageDaoImpl(connection);
        Statement statement = connection.createStatement();
        statement.executeUpdate("ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1");
        statement.executeUpdate("ALTER TABLE messages ALTER COLUMN message_id RESTART WITH 1");
        statement.executeUpdate("INSERT INTO users (username, hashed_password, is_admin) VALUES " +
                "('lkhiz23', 'pwd', TRUE), " +
                "('lchkh23', 'pwd2', TRUE), " +
                "('sansi23', 'pwd3', TRUE)");
        statement.executeUpdate("INSERT INTO messages (from_user_id, to_user_id, text) VALUES " +
                "(1, 2, 'Hey lchkh23!'), " +
                "(2, 1, 'Hey lkhiz23!'), " +
                "(3, 1, 'Yo, whats up?')");
        statement.close();
    }

    @AfterEach
    public void deleteFromTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM messages");
        statement.executeUpdate("DELETE FROM users");
        statement.close();
    }

    @Test
    public void getLastMessageTest() throws SQLException {
        assertEquals("Hey lchkh23!", messageDaoImpl.getLastMessage(1, 2).getContent());
    }

    @Test
    public void addMessageTest() throws SQLException {
        messageDaoImpl.addMessage(message);
        assertTrue(message.equals(messageDaoImpl.getLastMessage(1, 3)));
    }
}
