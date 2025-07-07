package Models;

public class User {
    private String username;
    private String first_name;
    private String last_name;
    private String date_joined;
    private String profile_picture;

    public User(String username, String first_name, String last_name, String date_joined, String profile_picture){
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

}