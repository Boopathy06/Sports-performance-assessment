package model;

import java.util.LinkedList;

public class Athlete {
    private String id, name, gender, location;
    private int age;
    private LinkedList<Performance> performances = new LinkedList<>();
    private String fitnessGoal = "";
    public Athlete(String id, String name, int age, String gender, String location) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.location = location;
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public LinkedList<Performance> getPerformances() { return performances; }
    public String getFitnessGoal() { return fitnessGoal; }
    public void setFitnessGoal(String fitnessGoal) { this.fitnessGoal = fitnessGoal; }

    public int getLeaderboardScore() {
        return performances.stream()
                .mapToInt(p -> p.getVerticalJump() + p.getSitUps())
                .max()
                .orElse(0);
    }
}
