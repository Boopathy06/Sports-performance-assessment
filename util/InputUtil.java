package util;

import java.util.Scanner;

public class InputUtil {
    private static Scanner sc = new Scanner(System.in);

    public static String getString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    public static int getInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    public static int getPositiveInt(String prompt) {
        while (true) {
            int val = getInt(prompt);
            if (val > 0) return val;
            System.out.println("Please enter a positive number.");
        }
    }
}
