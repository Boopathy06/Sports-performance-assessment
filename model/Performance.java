package model;

import java.time.LocalDate;

public class Performance {
    private int height, weight, verticalJump, sitUps, shuttleRun, enduranceRun;
    private LocalDate date;
    private String driveLink;

    public Performance(int height, int weight, int verticalJump, int sitUps, int shuttleRun, int enduranceRun, LocalDate date, String driveLink) {
        this.height = height;
        this.weight = weight;
        this.verticalJump = verticalJump;
        this.sitUps = sitUps;
        this.shuttleRun = shuttleRun;
        this.enduranceRun = enduranceRun;
        this.date = date;
        this.driveLink = driveLink;
    }

    public int getHeight() { return height; }
    public int getWeight() { return weight; }
    public int getVerticalJump() { return verticalJump; }
    public int getSitUps() { return sitUps; }
    public int getShuttleRun() { return shuttleRun; }
    public int getEnduranceRun() { return enduranceRun; }
    public LocalDate getDate() { return date; }
    public String getDriveLink() { return driveLink; }
    public void setDriveLink(String driveLink) { this.driveLink = driveLink; }

    @Override
    public String toString() {
        return String.format("%s - H:%d W:%d VJ:%d SU:%d SH:%d ER:%d Drive Link:%s",
                date, height, weight, verticalJump, sitUps, shuttleRun, enduranceRun,
                driveLink == null ? "None" : driveLink);
    }
}
