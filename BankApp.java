import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Minimalistic command-line Bank ATM application (MVP).
 * Demonstrates Java OOP principles and uses SQLite via DBHelper.
 *
 * Clear concept mapping (this file):
 * 1. Data types & variables: `scanner` (Scanner), `db` (DBHelper), primitives used for IDs, amounts.
 * 2. Methods: `main`, menu handlers (`handleCreateAccount`, `handleLogin`, etc.), input helpers `readInt`/`readDouble`.
 * 4. Encapsulation: uses public APIs of `Account` and `DBHelper` rather than accessing internals.
 * 8. Database Support: interacts with `DBHelper` to persist and read data (createCustomer, createAccount, updateAccountBalance).
 * 9. Input validation: checks for empty names, non-negative deposits, PIN validation, and repeated parsing loops in `readInt`/`readDouble`.
 * 10. Error handling: catches `NumberFormatException` in input methods and `IllegalArgumentException` around `deposit`/`withdraw` operations.
 */
public class BankApp {
    // 1. Data types & variables: `scanner` for CLI input, `db` for DB operations.
    private static final Scanner scanner = new Scanner(System.in);
    private static final DBHelper db = new DBHelper();

    public static void main(String[] args) {
        System.out.println("=== Minimal Bank ATM (MVP) ===");

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Choose an option: ");
            switch (choice) {
                case 1:
                    handleCreateAccount();
                    break;
                case 2:
                    handleLogin();
                    break;
                case 3:
                    handleListAccounts();
                    break;
                case 4:
                    System.out.println("Goodbye.");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println();
        System.out.println("1) Create customer & account");
        System.out.println("2) Login to account");
        System.out.println("3) List all accounts (admin)");
        System.out.println("4) Exit");
    }

    // Create customer and an initial account
    private static void handleCreateAccount() {
        System.out.println("-- Create Account --");
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine().trim();
        // 9. Input validation: check for empty name
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        // 9. Input validation: PIN read via `readInt` (parsing loop handles invalid input)
        int pin = readInt("Set a numeric PIN (4 digits recommended): ");
        if (pin < 0) {
            System.out.println("Invalid PIN.");
            return;
        }

        System.out.print("Account type (checking/savings): ");
        String type = scanner.nextLine().trim();
        if (type.isEmpty()) type = "checking";

        // 9. Input validation: initial deposit must be non-negative
        double initial = readDouble("Initial deposit (>=0): ");
        if (initial < 0) {
            System.out.println("Initial deposit must be non-negative.");
            return;
        }

        int customerId = db.createCustomer(name, pin);
        if (customerId == -1) {
            System.out.println("Failed to create customer.");
            return;
        }
        int accountId = db.createAccount(customerId, type, initial);
        if (accountId == -1) {
            System.out.println("Failed to create account.");
            return;
        }
        System.out.printf("Account created successfully. Account ID: %d\n", accountId);
    }

    // Login by customer name + PIN
    private static void handleLogin() {
        System.out.println("-- Login --");
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        int pin = readInt("Enter PIN: ");

        Account account = db.getAccountByCustomerNameAndPin(name, pin);
        if (account == null) {
            System.out.println("Authentication failed or no account found for that user.");
            return;
        }

        System.out.printf("Welcome, %s! (Account #%d)\n", name, account.getId());
        accountSession(account);
    }

    // Simple account session menu
    private static void accountSession(Account account) {
        boolean active = true;
        while (active) {
            System.out.println();
            System.out.println("a) View balance");
            System.out.println("b) Deposit");
            System.out.println("c) Withdraw");
            System.out.println("d) Logout");
            System.out.print("Choose: ");
            String s = scanner.nextLine().trim();
            switch (s) {
                case "a":
                    System.out.printf("Balance: %.2f\n", account.getBalance());
                    break;
                case "b":
                    double dep = readDouble("Amount to deposit: ");
                    try {
                        // 9. Input validation & 10. Error handling: Account.deposit validates amount and may throw
                        account.deposit(dep);
                        boolean ok = db.updateAccountBalance(account.getId(), account.getBalance());
                        if (ok) System.out.println("Deposit successful.");
                        else System.out.println("Failed to persist deposit.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case "c":
                    double w = readDouble("Amount to withdraw: ");
                    try {
                        // 9. Input validation & 10. Error handling: Account.withdraw validates amount and may throw
                        account.withdraw(w);
                        boolean ok = db.updateAccountBalance(account.getId(), account.getBalance());
                        if (ok) System.out.println("Withdrawal successful.");
                        else System.out.println("Failed to persist withdrawal.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case "d":
                    System.out.println("Logging out.");
                    active = false;
                    break;
                default:
                    System.out.println("Unknown option.");
            }
        }
    }

    // Admin listing
    private static void handleListAccounts() {
        System.out.println("-- All Accounts --");
        List<Account> accounts = db.listAccounts();
        if (accounts.isEmpty()) System.out.println("No accounts found.");
        for (Account a : accounts) System.out.println(a);
    }

    // Utility methods for safe input
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String line = scanner.nextLine().trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String line = scanner.nextLine().trim();
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
