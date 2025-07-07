package Models;

import java.sql.Timestamp;

public class UserAchievement {

    private String username;
    private long achievementId;
    private Timestamp earnedAt;

    public UserAchievement(String username, long achievementId, Timestamp earnedAt) {
        this.username = username;
        this.achievementId = achievementId;
        this.earnedAt = earnedAt;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public long getAchievementId() {
        return achievementId;
    }
    public void setAchievementId(long achievementId) {
        this.achievementId = achievementId;
    }
    public Timestamp getEarnedAt() {
        return earnedAt;
    }
    public void setEarnedAt(Timestamp earnedAt) {
        this.earnedAt = earnedAt;
    }
}
