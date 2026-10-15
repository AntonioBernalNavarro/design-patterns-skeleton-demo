package inventory;

import java.util.Scanner;

/**
 * Main class providing command-line interface for the inventory system.
 * Implements a complete menu-driven CLI application.
 *
 * Requirements:
 * - Menu with 6 options: Add, View, Sell, Stock, Statistics, Exit
 * - Input validation and error handling
 * - Sample data for testing
 * - User-friendly interface
 */
public class Main {

    // Class-level variables
    private static InventoryManager manager;
    private static Scanner scanner;

    /**
     * Main method: entry point of the application.
     */
    public static void main(String[] args) {
        // Initialize manager and scanner
        manager = new InventoryManager();
        scanner = new Scanner(System.in);

        // Load sample data
        System.out.println("Welcome to Inventory Management System!");
        loadSampleData();

        // Main application loop
        boolean running = true;
        while (running) {
            showMenu();
            int choice = getChoice();
            running = handleChoice(choice);
        }

        scanner.close();
        System.out.println("Goodbye!");
    }

    /**
     * Load sample data for testing.
     */
    private static void loadSampleData() {
        manager.addProduct("B001", "Java Programming", "BOOK", 29.99, 10);
        manager.addProduct("B002", "Design Patterns", "BOOK", 35.50, 8);
        manager.addProduct("E001", "Laptop", "ELECTRONICS", 999.99, 5);
        manager.addProduct("E002", "Mouse", "ELECTRONICS", 25.99, 15);
        System.out.println();
    }

    /**
     * Display main menu options.
     */
    private static void showMenu() {
        System.out.println("=== MAIN MENU ===");
        System.out.println("1. Add Product");
        System.out.println("2. View Inventory");
        System.out.println("3. Sell Product");
        System.out.println("4. Add Stock");
        System.out.println("5. View Statistics");
        System.out.println("6. Exit");
        System.out.print("Enter choice: ");
    }

    /**
     * Get user choice with input validation.
     *
     * @return user's menu choice (between 1 and 6), or -1 on invalid input
     */
    private static int getChoice() {
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            return choice;
        } catch (Exception e) {
            scanner.nextLine(); // consume invalid input
            System.out.println("Invalid input. Please enter a number.");
            return -1;
        }
    }

    /**
     * Handle user menu choice.
     *
     * @param choice - user's selected option
     * @return true to continue running, false to exit
     */
    private static boolean handleChoice(int choice) {
        switch (choice) {
            case 1:
                addProduct();
                break;
            case 2:
                manager.viewInventory();
                break;
            case 3:
                sellProduct();
                break;
            case 4:
                addStock();
                break;
            case 5:
                manager.viewStatistics();
                break;
            case 6:
                return false;
            default:
                System.out.println("Invalid choice.");
        }
        System.out.println();
        return true;
    }

    /**
     * Handle adding new product.
     */
    private static void addProduct() {
        System.out.print("Enter product ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter product name: ");
        String name = scanner.nextLine();

        System.out.print("Enter product type (BOOK/ELECTRONICS): ");
        String type = scanner.nextLine();

        double price = getDoubleInput("Enter product price: ");
        int quantity = getIntInput("Enter initial quantity: ");

        manager.addProduct(id, name, type, price, quantity);
    }

    /**
     * Handle selling product.
     */
    private static void sellProduct() {
        System.out.print("Enter product ID: ");
        String id = scanner.nextLine();

        int quantity = getIntInput("Enter quantity to sell: ");

        System.out.print("Enter discount type (STUDENT/BULK/NONE): ");
        String discountType = scanner.nextLine();

        manager.sellProduct(id, quantity, discountType);
    }

    /**
     * Handle adding stock.
     */
    private static void addStock() {
        System.out.print("Enter product ID: ");
        String id = scanner.nextLine();

        int quantity = getIntInput("Enter quantity to add: ");

        manager.addStock(id, quantity);
    }

    // Helper methods for input validation

    /**
     * Reads a string from the user.
     *
     * @param prompt - message to display
     * @return the user input
     */
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    /**
     * Reads an integer from the user with validation.
     *
     * @param prompt - message to display
     * @return the integer entered, or 0 if invalid
     */
    private static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = scanner.nextInt();
                scanner.nextLine(); // consume newline
                return value;
            } catch (Exception e) {
                scanner.nextLine(); // consume invalid input
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    /**
     * Reads a double from the user with validation.
     *
     * @param prompt - message to display
     * @return the double entered, or 0.0 if invalid
     */
    private static double getDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = scanner.nextDouble();
                scanner.nextLine(); // consume newline
                return value;
            } catch (Exception e) {
                scanner.nextLine(); // consume invalid input
                System.out.println("Invalid number. Try again.");
            }
        }
    }
}