import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean done = false;

        while (!done) {
            System.out.println("\nChoose PDF to generate:");
            System.out.println("1 - P9 Report");
            System.out.println("2 - Account Statement");
            System.out.print("Enter your choice: ");

            String input = scanner.nextLine().trim();

            try {
                int choice = Integer.parseInt(input);

                switch (choice) {
                    case 1 -> {
                        PDFService.generateP9Report();
                        done = true;
                    }
                    case 2 -> {
                        PDFService.generateAccountStatement();
                        done = true;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
