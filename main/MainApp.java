package main;
import model.Athlete;
import model.Role;
import model.User;
import service.AthleteService;
import service.OfficialService;
import util.InputUtil;
import util.PrintUtil;
import dao.UserDAO;
import dao.AthleteDAO;

public class MainApp {
    private AthleteService athleteService;
    private OfficialService officialService;
    private UserDAO userDAO = new UserDAO();
    private AthleteDAO athleteDAO = new AthleteDAO();
    private User currentUser = null;

    public static void main(String[] args) {
        new MainApp().run();
    }

    public void run() {
        setupDemo();
        athleteService = new AthleteService();
        officialService = new OfficialService();

        System.out.println("Welcome to the Sports Assessment System!");
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.println("\n1. Athlete Registration\n2. Login");
            int choice = InputUtil.getInt("Choose option: ");
            switch (choice) {
                case 1: athleteRegistration(); break;
                case 2: loggedIn = login(); break;
                default: System.out.println("Invalid choice."); break;
            }
        }

        boolean sessionActive = true;
        while (sessionActive) {
            showMenu();
            int option = InputUtil.getInt("Select option: ");
            if (currentUser.getRole() == Role.ATHLETE) {
                sessionActive = athleteMenu(option);
            } else {
                sessionActive = officialMenu(option);
            }
        }
        System.out.println("Goodbye!");
    }

    private void setupDemo() {
        try {
            String officialUsername = "official1";
            if (userDAO.getUser(officialUsername) == null) {
                userDAO.insertUser(new User(officialUsername, "password123", Role.OFFICIAL, null));
                System.out.println("Demo official user created: " + officialUsername);
            }

        } catch (Exception e) {
            System.out.println("Error in setupDemo: " + e.getMessage());
        }
    }

    private void athleteRegistration() {
        System.out.println("\n-- Athlete Registration --");
        try {
            String id;
            while (true) {
                id = InputUtil.getString("Choose Athlete ID (unique): ");
                if (athleteDAO.getAthlete(id) != null) {
                    System.out.println("ID already taken. Try again.");
                } else break;
            }
            String name = InputUtil.getString("Full Name: ");
            int age = InputUtil.getInt("Age: ");
            String gender = InputUtil.getString("Gender (M/F): ");
            String location = InputUtil.getString("Location: ");
            Athlete newAthlete = new Athlete(id, name, age, gender, location);
            athleteDAO.insertAthlete(newAthlete);

            String username;
            while (true) {
                username = InputUtil.getString("Choose username for login: ");
                if (userDAO.getUser(username) != null) {
                    System.out.println("Username taken. Try again.");
                } else break;
            }
            String password = InputUtil.getString("Choose password: ");
            userDAO.insertUser(new User(username, password, Role.ATHLETE, id));
            System.out.println("Registration complete! You may now login.");
        } catch (Exception e) {
            System.out.println("Error during registration: " + e.getMessage());
        }
    }

    private boolean login() {
        System.out.println("\n-- Login --");
        String username = InputUtil.getString("Username: ");
        String password = InputUtil.getString("Password: ");
        try {
            User user = userDAO.getUser(username);
            if (user != null && user.getPassword().equals(password)) {
                currentUser = user;
                System.out.println("Login successful! Welcome " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage());
        }
        System.out.println("Invalid username or password.");
        return false;
    }

    private void showMenu() {
        System.out.println();
        if (currentUser.getRole() == Role.ATHLETE) {
            System.out.println("Athlete Menu:\n 1. View/Update Profile\n 2. Submit Performance\n 3. View Performance History\n 4. Update Performance\n 5. Delete Performance\n 6. Get Performance Feedback\n 7. Set Fitness Goals\n 8. View Progress\n 9. Search Other Athletes\n 10. Delete Account\n 11. Help\n 12. Logout");
        } else {
            System.out.println(
                    "Official Menu:\n"
                            + "1. Manage Athlete Profiles\n"
                            + "2. Review Performances\n"
                            + "3. Generate Leaderboards\n"
                            + "4. Flag Suspicious Data\n"
                            + "5. Logout"
            );
        }
    }

    private boolean athleteMenu(int choice) {
        switch (choice) {
            case 1: athleteService.viewOrUpdateProfile(currentUser.getAthleteId()); break;
            case 2: athleteService.submitPerformance(currentUser.getAthleteId()); break;
            case 3: athleteService.viewPerformanceHistory(currentUser.getAthleteId()); break;
            case 4: athleteService.updatePerformance(currentUser.getAthleteId()); break;
            case 5: athleteService.deletePerformance(currentUser.getAthleteId()); break;
            case 6: athleteService.performanceFeedback(currentUser.getAthleteId()); break;
            case 7: athleteService.setFitnessGoals(currentUser.getAthleteId()); break;
            case 8: athleteService.viewProgress(currentUser.getAthleteId()); break;
            case 9: athleteService.searchAthletes(); break;
            case 10: athleteService.deleteAthleteDetails(currentUser.getAthleteId()); break;
            case 11: PrintUtil.showHelp(); break;
            case 12: logout(); return false;
            default: System.out.println("Invalid option."); break;
        }
        return true;
    }

    private boolean officialMenu(int choice) {
        switch (choice) {
            case 1: officialService.manageAthleteProfiles(); break;
            case 2: officialService.reviewPerformances(); break;
            case 3: officialService.generateLeaderboards(); break;
            case 4: officialService.flagSuspiciousData(); break;
            case 5: logout(); return false;
            default: System.out.println("Invalid option."); break;
        }
        return true;
    }

    private void logout() {
        System.out.println("Logging out...");
        currentUser = null;
        run();
        System.exit(0);
    }
}
