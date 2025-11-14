package dao;
import model.Athlete;
import util.DBConfig;
import java.sql.*;
import java.util.*;

public class AthleteDAO {

    public void insertAthlete(Athlete a) throws SQLException {
        String sql = "INSERT INTO athlete (id, name, age, gender, location, fitness_goal) VALUES (?, ?, ?, ?, ?, ?)";
        try(Connection conn = DBConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, a.getId());
            stmt.setString(2, a.getName());
            stmt.setInt(3, a.getAge());
            stmt.setString(4, a.getGender());
            stmt.setString(5, a.getLocation());
            stmt.setString(6, a.getFitnessGoal());
            stmt.executeUpdate();
        }
    }

    public void updateAthlete(Athlete a) throws SQLException {
        String sql = "UPDATE athlete SET name=?, age=?, gender=?, location=?, fitness_goal=? WHERE id=?";
        try(Connection conn = DBConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, a.getName());
            stmt.setInt(2, a.getAge());
            stmt.setString(3, a.getGender());
            stmt.setString(4, a.getLocation());
            stmt.setString(5, a.getFitnessGoal());
            stmt.setString(6, a.getId());
            stmt.executeUpdate();
        }
    }



    public Athlete getAthlete(String id) throws SQLException {
        String sql = "SELECT * FROM athlete WHERE id=?";
        try(Connection conn = DBConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                Athlete a = new Athlete(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("location")
                );
                a.setFitnessGoal(rs.getString("fitness_goal"));
                return a;
            }
        }
        return null;
    }

    public List<Athlete> getAllAthletes() throws SQLException {
        List<Athlete> athletes = new ArrayList<>();
        String sql = "SELECT * FROM athlete";
        try(Connection conn = DBConfig.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                Athlete a = new Athlete(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("location")
                );
                a.setFitnessGoal(rs.getString("fitness_goal"));
                athletes.add(a);
            }
        }
        return athletes;
    }
    public void deleteAthlete(String athleteId) throws SQLException {
        String sql = "DELETE FROM athlete WHERE id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, athleteId);
            stmt.executeUpdate();
        }
    }

}
