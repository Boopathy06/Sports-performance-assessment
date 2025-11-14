package service;

import dao.AthleteDAO;
import dao.PerformanceDAO;
import dao.UserDAO;
import model.Athlete;
import model.Performance;
import util.InputUtil;
import util.PrintUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class AthleteService {
    private final AthleteDAO athleteDAO = new AthleteDAO();
    private final PerformanceDAO performanceDAO = new PerformanceDAO();
    private final UserDAO userDAO = new UserDAO();

    public void viewOrUpdateProfile(String athleteId) {
        try {
            Athlete a = athleteDAO.getAthlete(athleteId);
            if (a == null) {
                System.out.println("Profile not found.");
                return;
            }
            PrintUtil.printAthlete(a);
            System.out.print("Update profile? (y/n): ");
            String ans = InputUtil.getString("");
            if (ans.equalsIgnoreCase("y")) {
                String name = InputUtil.getString("New name (blank skip): ");
                if (!name.isEmpty()) a.setName(name);

                String ageStr = InputUtil.getString("New age (blank skip): ");
                if (!ageStr.isEmpty()) a.setAge(Integer.parseInt(ageStr));

                String gender = InputUtil.getString("New gender (blank skip): ");
                if (!gender.isEmpty()) a.setGender(gender);

                String location = InputUtil.getString("New location (blank skip): ");
                if (!location.isEmpty()) a.setLocation(location);

                athleteDAO.updateAthlete(a);
                System.out.println("Profile updated.");
            }
        } catch (SQLException e) {
            System.out.println("Error accessing database: " + e.getMessage());
        }
    }

    public void submitPerformance(String athleteId) {
        try {
            Athlete a = athleteDAO.getAthlete(athleteId);
            if (a == null) {
                System.out.println("Profile not found.");
                return;
            }
            System.out.println("Enter performance data:");
            int height = InputUtil.getPositiveInt("Height (cm): ");
            int weight = InputUtil.getPositiveInt("Weight (kg): ");
            int vJump = InputUtil.getPositiveInt("Vertical Jump (cm): ");
            int sitUps = InputUtil.getPositiveInt("Sit-ups (1 min): ");
            int shuttleRun = InputUtil.getPositiveInt("Shuttle Run (seconds): ");
            int enduranceRun = InputUtil.getPositiveInt("Endurance Run (meters): ");
            String driveLink = InputUtil.getString("Enter Google Drive link (leave blank if none): ");
            if (driveLink.isEmpty()) {
                driveLink = null;
            }

            Performance p = new Performance(height, weight, vJump, sitUps, shuttleRun, enduranceRun, LocalDate.now(), driveLink);
            performanceDAO.insertPerformance(a.getId(), p);
            System.out.println("Performance submitted.");
        } catch (SQLException e) {
            System.out.println("Error accessing database: " + e.getMessage());
        }
    }

    public void viewPerformanceHistory(String athleteId) {
        try {
            List<Performance> perfList = performanceDAO.getAllPerformances(athleteId);
            if (perfList.isEmpty()) {
                System.out.println("No performance data.");
                return;
            }
            System.out.println("Performance History:");
            perfList.forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("Error accessing database: " + e.getMessage());
        }
    }

    public void performanceFeedback(String athleteId) {
        try {
            List<Performance> perfList = performanceDAO.getAllPerformances(athleteId);
            if (perfList.isEmpty()) {
                System.out.println("No performance data.");
                return;
            }
            Performance latest = perfList.get(perfList.size() - 1);
            System.out.println("Performance Feedback:");
            System.out.println("Vertical Jump: " + classify(latest.getVerticalJump(), 40, 30));
            System.out.println("Sit-ups: " + classify(latest.getSitUps(), 50, 35));
        } catch (SQLException e) {
            System.out.println("Error accessing database: " + e.getMessage());
        }
    }

    private String classify(int val, int good, int avg) {
        if (val >= good) return "Above Average";
        else if (val >= avg) return "Average";
        else return "Needs Improvement";
    }

    public void setFitnessGoals(String athleteId) {
        try {
            Athlete a = athleteDAO.getAthlete(athleteId);
            if (a == null) return;
            System.out.println("Current goal: " + (a.getFitnessGoal() == null ? "(none)" : a.getFitnessGoal()));
            String newGoal = InputUtil.getString("Enter new fitness goal: ");
            if (!newGoal.isEmpty()) {
                a.setFitnessGoal(newGoal);
                athleteDAO.updateAthlete(a);
            }
        } catch (SQLException e) {
            System.out.println("Error accessing database: " + e.getMessage());
        }
    }

    public void viewProgress(String athleteId) {
        try {
            List<Performance> perfList = performanceDAO.getAllPerformances(athleteId);
            if (perfList.size() < 2) {
                System.out.println("Not enough data to show progress.");
                return;
            }
            Performance first = perfList.get(0);
            Performance last = perfList.get(perfList.size() - 1);
            System.out.printf("Progress from %s to %s:\n", first.getDate(), last.getDate());
            System.out.printf("Vertical Jump: %d -> %d\n", first.getVerticalJump(), last.getVerticalJump());
            System.out.printf("Sit-ups: %d -> %d\n", first.getSitUps(), last.getSitUps());
        } catch (SQLException e) {
            System.out.println("Error accessing database: " + e.getMessage());
        }
    }

    public void searchAthletes() {
        try {
            String frag = InputUtil.getString("Enter athlete name fragment to search (blank for all): ").toLowerCase();
            List<Athlete> athletes = athleteDAO.getAllAthletes();
            athletes.stream()
                    .filter(a -> frag.isEmpty() || a.getName().toLowerCase().contains(frag))
                    .forEach(a -> System.out.printf("%s: %s, Age: %d, Location: %s%n", a.getId(), a.getName(), a.getAge(), a.getLocation()));
        } catch (SQLException e) {
            System.out.println("Error accessing database: " + e.getMessage());
        }
    }

    public void deleteAthleteDetails(String athleteId) {
        try {
            Athlete a = athleteDAO.getAthlete(athleteId);
            if (a == null) {
                System.out.println("Athlete not found.");
                return;
            }
            userDAO.deleteUsersByAthlete(athleteId);
            performanceDAO.deletePerformancesByAthlete(athleteId);
            athleteDAO.deleteAthlete(athleteId);
            System.out.println("Athlete and all related data deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting athlete: " + e.getMessage());
        }
    }

    public void updatePerformance(String athleteId) {
        try {
            List<Performance> performances = performanceDAO.getAllPerformances(athleteId);
            if (performances.isEmpty()) {
                System.out.println("No performances to update.");
                return;
            }
            for (int i = 0; i < performances.size(); i++) {
                System.out.println((i + 1) + ". " + performances.get(i));
            }
            int choice = InputUtil.getInt("Select performance number to update: ");
            if (choice < 1 || choice > performances.size()) {
                System.out.println("Invalid choice.");
                return;
            }
            Performance oldPerf = performances.get(choice - 1);
            int perfId = getPerformanceIdByDetails(athleteId, oldPerf);

            int height = InputUtil.getPositiveInt("New Height (cm): ");
            int weight = InputUtil.getPositiveInt("New Weight (kg): ");
            int vJump = InputUtil.getPositiveInt("New Vertical Jump (cm): ");
            int sitUps = InputUtil.getPositiveInt("New Sit-ups (1 min): ");
            int shuttleRun = InputUtil.getPositiveInt("New Shuttle Run (seconds): ");
            int enduranceRun = InputUtil.getPositiveInt("New Endurance Run (meters): ");

            String driveLinkPrompt = oldPerf.getDriveLink() == null ? "none" : oldPerf.getDriveLink();
            String driveLink = InputUtil.getString("New Drive Link (blank to remove, current: " + driveLinkPrompt + "): ");
            if (driveLink.isEmpty()) driveLink = null;

            Performance newPerf = new Performance(height, weight, vJump, sitUps, shuttleRun, enduranceRun, oldPerf.getDate(), driveLink);
            performanceDAO.updatePerformanceById(perfId, newPerf);
            System.out.println("Performance updated.");
        } catch (SQLException e) {
            System.out.println("Error updating performance: " + e.getMessage());
        }
    }

    public void deletePerformance(String athleteId) {
        try {
            List<Performance> performances = performanceDAO.getAllPerformances(athleteId);
            if (performances.isEmpty()) {
                System.out.println("No performances to delete.");
                return;
            }
            for (int i = 0; i < performances.size(); i++) {
                System.out.println((i + 1) + ". " + performances.get(i));
            }
            int choice = InputUtil.getInt("Select performance number to delete: ");
            if (choice < 1 || choice > performances.size()) {
                System.out.println("Invalid choice.");
                return;
            }
            Performance perf = performances.get(choice - 1);
            int perfId = getPerformanceIdByDetails(athleteId, perf);
            performanceDAO.deletePerformanceById(perfId);
            System.out.println("Performance deleted.");
        } catch (SQLException e) {
            System.out.println("Error deleting performance: " + e.getMessage());
        }
    }

    private int getPerformanceIdByDetails(String athleteId, Performance perf) throws SQLException {
        String sql = "SELECT id FROM performance WHERE athlete_id=? AND height=? AND weight=? AND vertical_jump=? AND sit_ups=? AND shuttle_run=? AND endurance_run=? AND perf_date=?";
        try (Connection conn = util.DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, athleteId);
            stmt.setInt(2, perf.getHeight());
            stmt.setInt(3, perf.getWeight());
            stmt.setInt(4, perf.getVerticalJump());
            stmt.setInt(5, perf.getSitUps());
            stmt.setInt(6, perf.getShuttleRun());
            stmt.setInt(7, perf.getEnduranceRun());
            stmt.setDate(8, java.sql.Date.valueOf(perf.getDate()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        }
        throw new SQLException("Performance record not found.");
    }
}
