package DAOs;

import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;

public class UserDAO {
    private String username;
    private String first_name;
    private String last_name;
    private String date_joined;
    private String profile_picture;


    public UserDAO(String username, String first_name, String last_name, String date_joined, String profile_picture){
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.date_joined = date_joined;
        this.profile_picture = profile_picture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getDate_joined() {
        return date_joined;
    }

    public void setDate_joined(String date_joined) {
        this.date_joined = date_joined;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }


    public static void createUser(String username, String first_name, String last_name, String date_joined, String password, String pfp_url) throws SQLException {
        String encrypted_password = hashPassword(password);
        if (pfp_url.isEmpty()) {
            pfp_url = "https://media.istockphoto.com/id/2171382633/vector/user-profile-icon-anonymous-person-symbol-blank-avatar-graphic-vector-illustration.jpg?s=612x612&w=0&k=20&c=ZwOF6NfOR0zhYC44xOX06ryIPAUhDvAajrPsaZ6v1-w=";
        }
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO users(username, first_name, last_name, date_joined, encrypted_password, profile_picture) " +
                             "VALUES(?, ?, ?, NOW(), ?, ?)")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, first_name);
            preparedStatement.setString(3, last_name);
            preparedStatement.setString(4, encrypted_password);
            preparedStatement.setString(5, pfp_url);
            preparedStatement.executeUpdate();
        }
    }

    public static void deleteUser(String username) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE username = ?")) {
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        }
    }

    public static UserDAO getUser(String username) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new UserDAO(
                        resultSet.getString("username"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("date_joined"),
                        resultSet.getString("profile_picture"));
            }
        }
        return null;
    }

    public static boolean userExists(String username) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT 1 FROM users WHERE username = ?")) {
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        }
    }

    public static boolean correctPassword(String username, String password) throws SQLException {
        String encrypted_password = hashPassword(password);
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT encrypted_password FROM users WHERE username = ?")) {
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                String real_password = rs.getString("encrypted_password");
                return real_password.equals(encrypted_password);
            }
        }
        return false;
    }

    public ArrayList<UserDAO> getFriends(int limit) throws SQLException {
        ArrayList<UserDAO> friends = new ArrayList<>();
        String query = "SELECT u.* FROM users u\n" +
                "            JOIN friends f ON (\n" +
                "                (f.first_friend_username = ? AND f.second_friend_username = u.username)\n" +
                "                OR (f.second_friend_username = ? AND f.first_friend_username = u.username)\n" +
                "            )\n" +
                "            LIMIT ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, this.username);
            ps.setString(2, this.username);
            ps.setInt(3, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                friends.add(new UserDAO(
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("date_joined"),
                        rs.getString("profile_picture")));
            }
        }

        return friends;
    }

    public static boolean isAdmin(String username) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT 1 FROM admin_users WHERE username = ?")) {
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        }
    }

    private static String hashPassword(String password) {
        String hashedWord = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            synchronized (md) {
                md.reset();
                md.update(password.getBytes());
            }
            byte[] inputDigest = md.digest();
            hashedWord = hexToString(inputDigest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashedWord;
    }

    private static String hexToString(byte[] bytes) {
        StringBuilder buff = new StringBuilder();
        for (byte b : bytes) {
            int val = b & 0xff;
            if (val < 16) buff.append('0');
            buff.append(Integer.toString(val, 16));
        }
        return buff.toString();
    }
}
