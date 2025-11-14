package util;
import model.Athlete;
public class PrintUtil {
    public static void printAthlete(Athlete a) {
        System.out.printf("ID: %s, Name: %s, Age: %d, Gender: %s, Location: %s, Fitness Goal: %s%n",
                a.getId(), a.getName(), a.getAge(), a.getGender(), a.getLocation(),
                a.getFitnessGoal().isEmpty() ? "(none)" : a.getFitnessGoal());
    }
    public static void showHelp() {
        System.out.println("\n--- Help / Instructions ---");
        System.out.println("1. Use the menu options by entering the corresponding number.");
        System.out.println("2. For athletes, you can update profile, submit performance, view history, set goals, and get feedback.");
        System.out.println("3. Officials can manage athlete profiles, review performances, generate leaderboards, and flag suspicious data.");
        System.out.println("4. Always input realistic and accurate data.");
        System.out.println("5. Contact officials for any issues.");
        System.out.println("6. To logout, select the logout option from the menu.");
        System.out.println("---------------------------\n");
    }
}

