package Models;

public class Friend {

    private String firstFriendUsername;
    private String secondFriendUsername;

    public Friend(String firstFriendUsername, String secondFriendUsername) {
        this.firstFriendUsername = firstFriendUsername;
        this.secondFriendUsername = secondFriendUsername;
    }

    public String getFirstFriendUsername() {
        return firstFriendUsername;
    }

    public void setFirstFriendUsername(String firstFriendUsername) {
        this.firstFriendUsername = firstFriendUsername;
    }

    public String getSecondFriendUsername() {
        return secondFriendUsername;
    }

    public void setSecondFriendUsername(String secondFriendUsername) {
        this.secondFriendUsername = secondFriendUsername;
    }
}