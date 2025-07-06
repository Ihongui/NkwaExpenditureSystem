package main;

import managers.BankAccountManager;
import managers.ExpenditureManager;
import models.BankAccount;
import models.Expenditure;
import managers.ReceiptManager;
import managers.CategoryManager;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Entry point of the application. Handles menu-driven navigation.
 */
public class MainApp {

    static Scanner scanner = new Scanner(System.in);
    static ExpenditureManager expenditureManager = new ExpenditureManager();
    static BankAccountManager bankManager = new BankAccountManager();
    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    static CategoryManager categoryManager = new CategoryManager();
    static ReceiptManager receiptManager = new ReceiptManager();



    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Nkwa Expenditure Management ===");
            System.out.println("1. Add New Expenditure");
            System.out.println("2. View All Expenditures");
            System.out.println("3. Create Bank Account");
            System.out.println("4. View Bank Accounts");
            System.out.println("5. Add New Category");
            System.out.println("6. View All Categories");
            System.out.println("7. Search Expenditures");
            System.out.println("8. Upload Receipt");
            System.out.println("9. Review Next Receipt");
            System.out.println("10. View Receipt Queue Size");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume leftover newline

            switch (choice) {
            case 1:
                addExpenditure();
                break;
            case 2:
                viewExpenditures();
                break;
            case 3:
                createBankAccount();
                break;
            case 4:
                viewBankAccounts();
                break;
            case 5:
                addCategory();
                break;
            case 6:
                viewCategories();
                break;
            case 7:
                searchMenu();
                break;
            case 8:
                uploadReceipt();
                break;
            case 9:
                reviewReceipt();
                break;
            case 10:
                System.out.println("📦 Pending receipts: " + receiptManager.getQueueSize());
                break;


            case 0:
                System.out.println("Exiting... Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }

        }
    }

    private static void addExpenditure() {
        try {
            System.out.print("Expenditure Code: ");
            String code = scanner.nextLine();

            System.out.print("Amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();

            System.out.print("Date (dd-MM-yyyy): ");
            String dateStr = scanner.nextLine();
            Date date = dateFormat.parse(dateStr);

            System.out.print("Phase (e.g., construction): ");
            String phase = scanner.nextLine();

            System.out.print("Category (e.g., Cement): ");
            String category = scanner.nextLine();

            System.out.print("Bank Account ID: ");
            String accountId = scanner.nextLine();

            // Withdraw from bank
            if (!bankManager.withdrawFromAccount(accountId, amount)) {
                System.out.println("❌ Insufficient funds or account not found.");
                return;
            }

            System.out.print("Receipt path (optional): ");
            String receipt = scanner.nextLine();

            // Add to expenditure system
            Expenditure exp = new Expenditure(code, amount, date, phase, category, accountId, receipt);
            expenditureManager.addExpenditure(exp);

            // Link expenditure to bank account
            BankAccount acc = bankManager.getAccount(accountId);
            if (acc != null) acc.addExpenditure(code);

            System.out.println("✅ Expenditure recorded successfully.");

        } catch (Exception e) {
            System.out.println("⚠️ Error adding expenditure: " + e.getMessage());
        }
    }

    private static void viewExpenditures() {
        List<Expenditure> list = expenditureManager.getAll();
        if (list.isEmpty()) {
            System.out.println("No expenditures recorded yet.");
        } else {
            for (Expenditure exp : list) {
                System.out.println(exp);
            }
        }
    }

    private static void createBankAccount() {
        System.out.print("Bank Account ID: ");
        String id = scanner.nextLine();

        System.out.print("Bank Name: ");
        String bank = scanner.nextLine();

        System.out.print("Initial Balance: ");
        double balance = scanner.nextDouble();
        scanner.nextLine();

        BankAccount acc = new BankAccount(id, bank, balance);
        bankManager.addAccount(acc);

        System.out.println("✅ Bank account created.");
    }

    private static void viewBankAccounts() {
        List<BankAccount> accounts = bankManager.getAllAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No bank accounts available.");
        } else {
            for (BankAccount acc : accounts) {
                System.out.println(acc);
            }
        }
    }
    private static void addCategory() {
        System.out.print("Enter category name: ");
        String name = scanner.nextLine();
        if (categoryManager.addCategory(name)) {
            System.out.println("✅ Category added.");
        } else {
            System.out.println("⚠️ Category already exists.");
        }
    }

    private static void viewCategories() {
        Set<String> categories = categoryManager.getAllCategories();
        if (categories.isEmpty()) {
            System.out.println("No categories yet.");
        } else {
            System.out.println("📂 Categories:");
            for (String cat : categories) {
                System.out.println("- " + cat);
            }
        }
    }
    private static void searchMenu() {
        System.out.println("\n--- Search Expenditures ---");
        System.out.println("1. By Date Range");
        System.out.println("2. By Category");
        System.out.println("3. By Cost Range");
        System.out.println("4. By Bank Account");
        System.out.print("Choose option: ");
        
        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
        case 1:
            try {
                System.out.print("Start Date (dd-MM-yyyy): ");
                Date start = dateFormat.parse(scanner.nextLine());

                System.out.print("End Date (dd-MM-yyyy): ");
                Date end = dateFormat.parse(scanner.nextLine());

                List<Expenditure> result = expenditureManager.searchByDateRange(start, end);
                showSearchResults(result);
            } catch (Exception e) {
                System.out.println("Invalid date format.");
            }
            break;

        case 2:
            System.out.print("Enter category: ");
            String cat = scanner.nextLine();
            List<Expenditure> resultByCategory = expenditureManager.searchByCategory(cat);
            showSearchResults(resultByCategory);
            break;

        case 3:
            System.out.print("Min amount: ");
            double min = scanner.nextDouble();
            System.out.print("Max amount: ");
            double max = scanner.nextDouble();
            scanner.nextLine(); // flush newline
            List<Expenditure> resultByCost = expenditureManager.searchByCostRange(min, max);
            showSearchResults(resultByCost);
            break;

        case 4:
            System.out.print("Bank Account ID: ");
            String accId = scanner.nextLine();
            List<Expenditure> resultByAccount = expenditureManager.searchByAccount(accId);
            showSearchResults(resultByAccount);
            break;

        default:
            System.out.println("Invalid option.");
            break;
    }

    }
    private static void uploadReceipt() {
        System.out.print("Enter receipt file path (e.g., receipts/12345.jpg): ");
        String path = scanner.nextLine();
        receiptManager.uploadReceipt(path);
        System.out.println("✅ Receipt added to queue for review.");
    }
    private static void reviewReceipt() {
        if (receiptManager.isEmpty()) {
            System.out.println("📭 No receipts to review.");
            return;
        }

        String next = receiptManager.peekNextReceipt();
        System.out.println("🔍 Reviewing: " + next);
        System.out.print("Mark as reviewed? (y/n): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            receiptManager.reviewNextReceipt(); // dequeue
            System.out.println("✅ Receipt reviewed and removed from queue.");
        } else {
            System.out.println("⏳ Receipt left in queue.");
        }
    }


    private static void showSearchResults(List<Expenditure> list) {
        if (list.isEmpty()) {
            System.out.println("No expenditures found.");
        } else {
            System.out.println("\n📋 Matching Expenditures:");
            for (Expenditure exp : list) {
                System.out.println(exp);
            }
        }
    }

}
