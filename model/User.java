package model;

public class User {
    private String username, password;
    private Role role;
    private String athleteId; // Only for athletes

    public User(String username, String password, Role role, String athleteId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.athleteId = athleteId;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public String getAthleteId() { return athleteId; }
}
