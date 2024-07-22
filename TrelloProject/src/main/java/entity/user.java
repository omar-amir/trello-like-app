package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class user {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id") 
    private String userId;

    @Column(name = "name") 
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password") 
    private String password;
    
    @Column(name = "teamLeaderStatus")
    private boolean teamLeaderStatus;

    public user() {
    }
    
    public user(String userId, String name, String email, String password, boolean teamLeaderStatus) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.teamLeaderStatus=teamLeaderStatus;
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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public boolean getTeamLeaderStatus() {
        return teamLeaderStatus;
    }
    public void setTeamLeaderStatus(boolean isTeamLeader) {
        this.teamLeaderStatus=isTeamLeader;
    }
}