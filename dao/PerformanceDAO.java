package dao;

import model.Performance;
import util.DBConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date; // Explicit import of java.sql.Date
import java.util.ArrayList;
import java.util.List;

public class PerformanceDAO {
    public void insertPerformance(String athleteId, Performance p) throws SQLException {
        String sql = "INSERT INTO performance (athlete_id, height, weight, vertical_jump, sit_ups, shuttle_run, endurance_run, perf_date, drive_link) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, athleteId);
            stmt.setInt(2, p.getHeight());
            stmt.setInt(3, p.getWeight());
            stmt.setInt(4, p.getVerticalJump());
            stmt.setInt(5, p.getSitUps());
            stmt.setInt(6, p.getShuttleRun());
            stmt.setInt(7, p.getEnduranceRun());
            stmt.setDate(8, Date.valueOf(p.getDate()));
            stmt.setString(9, p.getDriveLink());
            stmt.executeUpdate();
        }
    }

    public List<Performance> getAllPerformances(String athleteId) throws SQLException {
        List<Performance> perfs = new ArrayList<>();
        String sql = "SELECT * FROM performance WHERE athlete_id=?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, athleteId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                perfs.add(new Performance(
                        rs.getInt("height"),
                        rs.getInt("weight"),
                        rs.getInt("vertical_jump"),
                        rs.getInt("sit_ups"),
                        rs.getInt("shuttle_run"),
                        rs.getInt("endurance_run"),
                        rs.getDate("perf_date").toLocalDate(),
                        rs.getString("drive_link")
                ));
            }
        }
        return perfs;
    }

    // New delete method to remove all performances by athleteId
    public void deletePerformancesByAthlete(String athleteId) throws SQLException {
        String sql = "DELETE FROM performance WHERE athlete_id=?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, athleteId);
            stmt.executeUpdate();
        }
    }

    // Delete a performance record by performance ID
    public void deletePerformanceById(int perfId) throws SQLException {
        String sql = "DELETE FROM performance WHERE id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, perfId);
            stmt.executeUpdate();
        }
    }

    // Update a performance record by performance ID (including drive_link)
    public void updatePerformanceById(int perfId, Performance p) throws SQLException {
        String sql = "UPDATE performance SET height=?, weight=?, vertical_jump=?, sit_ups=?, shuttle_run=?, endurance_run=?, perf_date=?, drive_link=? WHERE id=?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, p.getHeight());
            stmt.setInt(2, p.getWeight());
            stmt.setInt(3, p.getVerticalJump());
            stmt.setInt(4, p.getSitUps());
            stmt.setInt(5, p.getShuttleRun());
            stmt.setInt(6, p.getEnduranceRun());
            stmt.setDate(7, Date.valueOf(p.getDate()));
            stmt.setString(8, p.getDriveLink());
            stmt.setInt(9, perfId);
            stmt.executeUpdate();
        }
    }
}
