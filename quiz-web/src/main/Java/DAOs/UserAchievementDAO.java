package DAOs;

import Models.UserAchievement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserAchievementDAO {

        public static void addUserAchievement(String username, long achievementId) throws SQLException {
            try(Connection connection = DBConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user_achievements " +
                        "(username, achievement_id) VALUES (?, ?)")) {
                preparedStatement.setString(1, username);
                preparedStatement.setLong(2, achievementId);
                preparedStatement.executeUpdate();
            }
        }

        public static void removeUserAchievement(String username, long achievementId) throws SQLException {
            try(Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM user_achievements " +
                    "WHERE username = ? AND achievement_id = ?")) {
                preparedStatement.setString(1, username);
                preparedStatement.setLong(2, achievementId);
                preparedStatement.executeUpdate();
            }
        }

        public ArrayList<UserAchievement> getUserAchievements(String username) throws SQLException {
            ArrayList<UserAchievement> achievements = new ArrayList<>();
            try(Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user_achievements " +
                    "WHERE username = ?")) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    achievements.add(new UserAchievement(
                            resultSet.getString("username"),
                            resultSet.getLong("achievement_id"),
                            resultSet.getTimestamp("earned_at")
                    ));
                }
            }
            return achievements;
        }
}
