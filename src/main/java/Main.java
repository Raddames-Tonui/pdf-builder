
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose PDF to generate:");
        System.out.println("1 - P9 Report");
        System.out.println("2 - Account Statement");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        try {
            if (choice == 1) {
                PDFService.generateP9Report();
            } else if (choice == 2) {
                PDFService.generateAccountStatement();
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (Exception e) {
            System.err.println("Error generating PDF: " + e.getMessage());
        }
    }
}
