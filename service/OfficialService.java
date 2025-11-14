package service;

import dao.AthleteDAO;
import dao.PerformanceDAO;
import model.Athlete;
import model.Performance;
import util.InputUtil;
import util.PrintUtil;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OfficialService {
    private AthleteDAO athleteDAO = new AthleteDAO();
    private PerformanceDAO performanceDAO = new PerformanceDAO();

    public void manageAthleteProfiles() {
        System.out.println("Manage Athlete Profiles:\n1. Add Athlete\n2. Edit Athlete");
        int choice = InputUtil.getInt("Choose option: ");
        try {
            if (choice == 1) {
                String id;
                while (true) {
                    id = InputUtil.getString("New Athlete ID (unique): ");
                    if (athleteDAO.getAthlete(id) != null)
                        System.out.println("ID taken.");
                    else break;
                }
                String name = InputUtil.getString("Name: ");
                int age = InputUtil.getInt("Age: ");
                String gender = InputUtil.getString("Gender: ");
                String location = InputUtil.getString("Location: ");
                Athlete a = new Athlete(id, name, age, gender, location);
                athleteDAO.insertAthlete(a);
                System.out.println("Athlete added.");
            } else if (choice == 2) {
                String id = InputUtil.getString("Athlete ID to edit: ");
                Athlete athlete = athleteDAO.getAthlete(id);
                if (athlete == null) {
                    System.out.println("No such athlete found.");
                    return;
                }
                PrintUtil.printAthlete(athlete);
                System.out.print("Update profile? (y/n): ");
                String ans = InputUtil.getString("");
                if (ans.equalsIgnoreCase("y")) {
                    String name = InputUtil.getString("New name (blank skip): ");
                    if (!name.isEmpty()) athlete.setName(name);
                    String ageStr = InputUtil.getString("New age (blank skip): ");
                    if (!ageStr.isEmpty()) athlete.setAge(Integer.parseInt(ageStr));
                    String gender = InputUtil.getString("New gender (blank skip): ");
                    if (!gender.isEmpty()) athlete.setGender(gender);
                    String location = InputUtil.getString("New location (blank skip): ");
                    if (!location.isEmpty()) athlete.setLocation(location);
                    athleteDAO.updateAthlete(athlete);
                    System.out.println("Profile updated.");
                }
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public void reviewPerformances() {
        System.out.println("Review latest performances:");
        try {
            List<Athlete> athletes = athleteDAO.getAllAthletes();
            for (Athlete a : athletes) {
                List<Performance> perfList = performanceDAO.getAllPerformances(a.getId());
                if (!perfList.isEmpty()) {
                    Performance p = perfList.get(perfList.size() - 1);
                    System.out.printf("%s %s : %s%n", a.getId(), a.getName(), p);
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public void generateLeaderboards() {
        System.out.println("Leaderboard (Top 10 by Vertical Jump + Sit-ups):");
        try {
            List<Athlete> athletes = athleteDAO.getAllAthletes();
            // Compute scores and sort
            List<AthleteScore> scores = athletes.stream()
                    .map(a -> {
                        try {
                            List<Performance> perfs = performanceDAO.getAllPerformances(a.getId());
                            int score = perfs.stream().mapToInt(
                                    p -> p.getVerticalJump() + p.getSitUps()
                            ).sum();
                            return new AthleteScore(a, score);
                        } catch (SQLException e) {
                            return new AthleteScore(a, 0);
                        }
                    })
                    .sorted(Comparator.comparingInt((AthleteScore s) -> -s.score))
                    .collect(Collectors.toList());

            for (int i = 0; i < Math.min(10, scores.size()); i++) {
                AthleteScore s = scores.get(i);
                System.out.printf("%d. %s (%s) Score: %d%n", i + 1, s.athlete.getName(), s.athlete.getId(), s.score);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public void flagSuspiciousData() {
        System.out.println("Suspicious performances flagged:");
        try {
            List<Athlete> athletes = athleteDAO.getAllAthletes();
            for (Athlete a : athletes) {
                List<Performance> perfList = performanceDAO.getAllPerformances(a.getId());
                for (Performance p : perfList) {
                    if (p.getSitUps() > 100 || p.getVerticalJump() > 120 || p.getShuttleRun() < 5) {
                        System.out.printf("%s %s : %s%n", a.getId(), a.getName(), p);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
    private static class AthleteScore {
        public Athlete athlete;
        public int score;
        public AthleteScore(Athlete a, int s) { athlete = a; score = s; }
    }
}
