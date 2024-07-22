package service;

public class UserDTO {
    private String userId;
    private String name;
    private String password;
    private String email;
    private boolean teamLeaderStatus;

 

    public UserDTO() {
    }

    public UserDTO(String userId, String name, String password, String email, boolean teamLeaderStatus) {
        this.userId = userId;
        this.name = name;
        this.password=password;
        this.email = email;
        this.teamLeaderStatus = teamLeaderStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setPassword(String pass) {
    	password=pass;
    }
    public String getPassword() {
    	return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isTeamLeaderStatus() {
        return teamLeaderStatus;
    }

    public void setTeamLeaderStatus(boolean teamLeaderStatus) {
        this.teamLeaderStatus = teamLeaderStatus;
    }
}
