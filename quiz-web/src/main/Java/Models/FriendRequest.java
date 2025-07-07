package Models;

public class FriendRequest {

    private String requestFromUsername;
    private String requestToUsername;

    public FriendRequest(String requestFromUsername, String requestToUsername) {
        this.requestFromUsername = requestFromUsername;
        this.requestToUsername = requestToUsername;
    }

    public String getRequestFromUsername() {
        return requestFromUsername;
    }

    public void setRequestFromUsername(String requestFromUsername) {
        this.requestFromUsername = requestFromUsername;
    }

    public String getRequestToUsername() {
        return requestToUsername;
    }

    public void setRequestToUsername(String requestToUsername) {
        this.requestToUsername = requestToUsername;
    }
}
