package dao;
import model.User;
import model.Role;
import util.DBConfig;
import java.sql.*;

public class UserDAO {
    public void insertUser(User u) throws SQLException {
        String sql = "INSERT INTO user (username, password, role, athlete_id) VALUES (?, ?, ?, ?)";
        try(Connection conn = DBConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, u.getUsername());
            stmt.setString(2, u.getPassword());
            stmt.setString(3, u.getRole().name());
            stmt.setString(4, u.getAthleteId());
            stmt.executeUpdate();
        }
    }
    public User getUser(String username) throws SQLException {
        String sql = "SELECT * FROM user WHERE username=?";
        try(Connection conn = DBConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        Role.valueOf(rs.getString("role")),
                        rs.getString("athlete_id")
                );
            }
        }
        return null;
    }
    public void deleteUsersByAthlete(String athleteId) throws SQLException {
        String sql = "DELETE FROM user WHERE athlete_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, athleteId);
            stmt.executeUpdate();
        }
    }
}
