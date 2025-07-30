package main;

import managers.BankAccountManager;
import managers.ExpenditureManager;
import models.BankAccount;
import models.Expenditure;
import managers.ReceiptManager;
import managers.CategoryManager;
import analysis.FinancialAnalysis;
import graph.AccountGraph;
import reports.ReportGenerator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import utils.MyList;
import utils.MyMap;
import java.util.Scanner;
import utils.MySet;
import java.util.InputMismatchException;

/**
 * Entry point of the application. Handles menu-driven navigation with input
 * validation.
 */
public class MainApp {

    // Static variables - shared across all instances
    static Scanner scanner = new Scanner(System.in);
    static ExpenditureManager expenditureManager = new ExpenditureManager();
    static BankAccountManager bankManager = new BankAccountManager();
    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    static CategoryManager categoryManager = new CategoryManager();
    static ReceiptManager receiptManager = new ReceiptManager();
    static FinancialAnalysis analysis = new FinancialAnalysis(expenditureManager);
    static AccountGraph accountGraph = new AccountGraph();
    static ReportGenerator reportGenerator = new ReportGenerator();

    // Constants for better maintainability
    private static final String MENU_SEPARATOR = "=".repeat(50);
    private static final String ERROR_PREFIX = "❌ ERROR: ";
    private static final String WARNING_PREFIX = "⚠️ WARNING: ";
    private static final String SUCCESS_PREFIX = "✅ SUCCESS: ";
    private static final String INFO_PREFIX = "ℹ️ INFO: ";

    /**
     * Main method - Entry point of the application
     * 
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {
        displayWelcomeMessage();

        // Load data from files
        bankManager.loadFromFile("accounts.txt");
        expenditureManager.loadFromFile("expenditures.txt");
        categoryManager.loadFromFile("categories.txt");
        receiptManager.loadFromFile("receipts.txt");

        // Main loop
        while (true) {
            try {
                displayMainMenu();
                int choice = getValidMenuChoice();

                if (choice == 0) {
                    // Save before exiting
                    bankManager.saveToFile("accounts.txt");
                    expenditureManager.saveToFile("expenditures.txt");
                    categoryManager.saveToFile("categories.txt");
                    receiptManager.saveToFile("receipts.txt");

                    System.out.println("✅ All data saved. Exiting...");
                    break;
                }

                processMenuChoice(choice);

            } catch (Exception e) {
                System.out.println(ERROR_PREFIX + "Unexpected error: " + e.getMessage());
                System.out.println(INFO_PREFIX + "Please try again.");
            }
        }
    }

    /**
     * Display welcome message
     */
    private static void displayWelcomeMessage() {
        System.out.println("\n" + MENU_SEPARATOR);
        System.out.println("🏦 WELCOME TO NKWA EXPENDITURE MANAGEMENT SYSTEM 🏦");
        System.out.println(MENU_SEPARATOR);
        System.out.println("📋 This system helps you manage financial expenditures efficiently.");
        System.out.println("💡 Follow the prompts carefully for best results.");
        System.out.println(MENU_SEPARATOR);
    }

    /**
     * Display main menu options
     */
    private static void displayMainMenu() {
        System.out.println("\n" + MENU_SEPARATOR);
        System.out.println("🏠 MAIN MENU - Choose an option:");
        System.out.println(MENU_SEPARATOR);

        // Expenditure Management
        System.out.println("📊 EXPENDITURE MANAGEMENT:");
        System.out.println("   1. Add New Expenditure");
        System.out.println("   2. View All Expenditures");
        System.out.println("   7. Search Expenditures");
        System.out.println("   17. Sort Expenditures by Category");
        System.out.println("   18. Sort Expenditures by Date");

        // Bank Account Management
        System.out.println("\n🏦 BANK ACCOUNT MANAGEMENT:");
        System.out.println("   3. Create Bank Account");
        System.out.println("   4. View Bank Accounts");
        System.out.println("   14. Add Account Transfer");
        System.out.println("   15. View Account Relationship Graph");
        System.out.println("   16. Show Reachable Accounts");

        // Category Management
        System.out.println("\n📂 CATEGORY MANAGEMENT:");
        System.out.println("   5. Add New Category");
        System.out.println("   6. View All Categories");

        // Receipt Management
        System.out.println("\n🧾 RECEIPT MANAGEMENT:");
        System.out.println("   8. Upload Receipt");
        System.out.println("   9. Review Next Receipt");
        System.out.println("   10. View Receipt Queue Size");

        // Analysis & Reports
        System.out.println("\n📈 ANALYSIS & REPORTS:");
        System.out.println("   11. Show Monthly Burn Rate");
        System.out.println("   12. Forecast Profitability");
        System.out.println("   13. Show Top Spending Categories");
        System.out.println("   19. Generate System Report");

        System.out.println("\n❌ EXIT:");
        System.out.println("   0. Exit Application");

        System.out.println(MENU_SEPARATOR);
        System.out.print("👉 Enter your choice (0-19): ");
    }

    /**
     * Get valid menu choice with input validation
     */
    private static int getValidMenuChoice() {
        while (true) {
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume leftover newline

                if (choice >= 0 && choice <= 19) {
                    return choice;
                } else {
                    System.out.print(WARNING_PREFIX + "Please enter a number between 0 and 19: ");
                }
            } catch (InputMismatchException e) {
                System.out.print(ERROR_PREFIX + "Please enter a valid number (0-19): ");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    /**
     * Process the user's menu choice
     */
    private static void processMenuChoice(int choice) {
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
                System.out.println(INFO_PREFIX + "Pending receipts: " + receiptManager.getQueueSize());
                break;
            case 11:
                showBurnRate();
                break;
            case 12:
                analysis.forecastProfitability();
                break;
            case 13:
                showTopCategories();
                break;
            case 14:
                addTransfer();
                break;
            case 15:
                accountGraph.displayGraph();
                break;
            case 16:
                showReachableAccounts();
                break;
            case 17:
                sortByCategory();
                break;
            case 18:
                sortByDate();
                break;
            case 19:
                reportGenerator.generateReport();
                break;
            case 0:
                exitApplication();
                break;
            default:
                System.out.println(ERROR_PREFIX + "Invalid choice. Please try again.");
                break;
        }
    }

    /**
     * Add new expenditure with comprehensive input validation
     */
    private static void addExpenditure() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("➕ ADD NEW EXPENDITURE");
        System.out.println("=".repeat(30));

        try {
            // Get expenditure code
            String code = getValidString("💼 Enter Expenditure Code: ",
                    "Expenditure code cannot be empty!");

            // Get amount
            double amount = getValidAmount("💰 Enter Amount (GHS): ");

            // Get date
            Date date = getValidDate("📅 Enter Date (dd-MM-yyyy): ");

            // Get phase
            String phase = getValidString("🏗️ Enter Phase (e.g., construction): ",
                    "Phase cannot be empty!");

            // Get category
            String category = getValidString("📊 Enter Category (e.g., Cement): ",
                    "Category cannot be empty!");

            // Get bank account ID
            String accountId = getValidString("🏦 Enter Bank Account ID: ",
                    "Bank Account ID cannot be empty!");

            // Check if account exists and has sufficient funds
            if (!bankManager.withdrawFromAccount(accountId, amount)) {
                System.out.println(ERROR_PREFIX + "Insufficient funds or account not found.");
                System.out.println(INFO_PREFIX + "Please check your account ID and balance.");
                return;
            }

            // Get receipt path (optional)
            System.out.print("🧾 Enter Receipt Path (optional, press Enter to skip): ");
            String receiptpath = scanner.nextLine().trim();

            // Use a default receipt path if empty
            if (receiptpath.isEmpty()) {
                receiptpath = "receipts/" + code + "_receipt.txt";
                System.out.println(INFO_PREFIX + "Using default receipt path: " + receiptpath);
            }

            // Create and add expenditure
            Expenditure exp = new Expenditure(code, amount, date, phase, category, accountId, receiptpath);
            expenditureManager.addExpenditure(exp);
            receiptManager.saveReceipt(receiptpath, exp.toString());

            // Link expenditure to bank account
            BankAccount acc = bankManager.getAccount(accountId);
            if (acc != null) {
                acc.addExpenditure(code);
            }

            System.out.println(SUCCESS_PREFIX + "Expenditure recorded successfully!");
            System.out.println(INFO_PREFIX + "Amount GHS " + amount + " withdrawn from account " + accountId);

        } catch (Exception e) {
            System.out.println(ERROR_PREFIX + "Failed to add expenditure: " + e.getMessage());
            System.out.println(INFO_PREFIX + "Please try again with valid inputs.");
        }
    }

    /**
     * View all expenditures
     */
    private static void viewExpenditures() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("👁️ ALL EXPENDITURES");
        System.out.println("=".repeat(30));

        MyList<Expenditure> list = expenditureManager.getAll();
        if (list.isEmpty()) {
            System.out.println(INFO_PREFIX + "No expenditures recorded yet.");
            System.out.println("💡 Use option 1 to add your first expenditure.");
        } else {
            System.out.println(INFO_PREFIX + "Total expenditures: " + list.size());
            System.out.println("-".repeat(50));
            for (Expenditure exp : list) {
                System.out.println(exp);
            }
        }
    }

    /**
     * Create new bank account with validation
     */
    private static void createBankAccount() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("🏦 CREATE BANK ACCOUNT");
        System.out.println("=".repeat(30));

        try {
            String id = getValidString("🆔 Enter Bank Account ID: ",
                    "Bank Account ID cannot be empty!");

            String bank = getValidString("🏦 Enter Bank Name: ",
                    "Bank name cannot be empty!");

            double balance = getValidAmount("💰 Enter Initial Balance (GHS): ");

            BankAccount acc = new BankAccount(id, bank, balance);
            bankManager.addAccount(acc);

            System.out.println(SUCCESS_PREFIX + "Bank account created successfully!");
            System.out.println(INFO_PREFIX + "Account ID: " + id + ", Bank: " + bank + ", Balance: GHS " + balance);

        } catch (Exception e) {
            System.out.println(ERROR_PREFIX + "Failed to create bank account: " + e.getMessage());
        }
    }

    /**
     * View all bank accounts
     */
    private static void viewBankAccounts() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("🏦 ALL BANK ACCOUNTS");
        System.out.println("=".repeat(30));

        MyList<BankAccount> accounts = bankManager.getAllAccounts();
        if (accounts.isEmpty()) {
            System.out.println(INFO_PREFIX + "No bank accounts available.");
            System.out.println("💡 Use option 3 to create your first bank account.");
        } else {
            System.out.println(INFO_PREFIX + "Total accounts: " + accounts.size());
            System.out.println("-".repeat(50));
            for (BankAccount acc : accounts) {
                System.out.println(acc);
            }
        }
    }

    /**
     * Add new category with validation
     */
    private static void addCategory() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("📂 ADD NEW CATEGORY");
        System.out.println("=".repeat(30));

        String name = getValidString("📊 Enter Category Name: ",
                "Category name cannot be empty!");

        if (categoryManager.addCategory(name)) {
            System.out.println(SUCCESS_PREFIX + "Category '" + name + "' added successfully!");
        } else {
            System.out.println(WARNING_PREFIX + "Category '" + name + "' already exists.");
            System.out.println(INFO_PREFIX + "Please choose a different category name.");
        }
    }

    /**
     * View all categories
     */
    private static void viewCategories() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("📂 ALL CATEGORIES");
        System.out.println("=".repeat(30));

        MySet<String> categories = categoryManager.getAllCategories();
        if (categories.isEmpty()) {
            System.out.println(INFO_PREFIX + "No categories available.");
            System.out.println("💡 Use option 5 to add your first category.");
        } else {
            System.out.println(INFO_PREFIX + "Total categories: " + categories.size());
            System.out.println("-".repeat(30));
            for (String cat : categories) {
                System.out.println("📊 " + cat);
            }
        }
    }

    /**
     * Add transfer relationship between accounts
     */
    private static void addTransfer() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("💸 ADD ACCOUNT TRANSFER");
        System.out.println("=".repeat(30));

        String from = getValidString("🏦 Enter Source Account ID: ",
                "Source account ID cannot be empty!");

        String to = getValidString("🏦 Enter Destination Account ID: ",
                "Destination account ID cannot be empty!");

        if (from.equals(to)) {
            System.out.println(ERROR_PREFIX + "Source and destination accounts cannot be the same!");
            return;
        }

        accountGraph.addTransfer(from, to);
        System.out.println(SUCCESS_PREFIX + "Transfer relationship recorded successfully!");
        System.out.println(INFO_PREFIX + "Transfer: " + from + " → " + to);
    }

    /**
     * Show reachable accounts from a given account
     */
    private static void showReachableAccounts() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("🔗 REACHABLE ACCOUNTS");
        System.out.println("=".repeat(30));

        String id = getValidString("🆔 Enter Account ID to explore: ",
                "Account ID cannot be empty!");

        MySet<String> reachable = accountGraph.getReachableAccounts(id);

        if (reachable.isEmpty()) {
            System.out.println(INFO_PREFIX + "No reachable accounts found from account: " + id);
        } else {
            System.out.println(SUCCESS_PREFIX + "Found " + reachable.size() + " reachable accounts from " + id + ":");
            System.out.println("-".repeat(40));
            for (String acc : reachable) {
                System.out.println("🔗 " + acc);
            }
        }
    }

    /**
     * Show monthly burn rate
     */
    private static void showBurnRate() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("📅 MONTHLY BURN RATE");
        System.out.println("=".repeat(30));

        MyMap<String, Double> burn = analysis.calculateMonthlyBurnRate();
        if (burn.isEmpty()) {
            System.out.println(INFO_PREFIX + "No expenditure data available to calculate burn rate.");
            System.out.println("💡 Add some expenditures first using option 1.");
        } else {
            System.out.println(INFO_PREFIX + "Monthly spending breakdown (GHS):");
            System.out.println("-".repeat(40));
            for (MyMap.Entry<String, Double> entry : burn.entrySet()) {
                System.out.println("📅 " + entry.getKey() + ": GHS " + String.format("%.2f", entry.getValue()));
            }
        }
    }

    /**
     * Show top spending categories
     */
    private static void showTopCategories() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("📊 TOP SPENDING CATEGORIES");
        System.out.println("=".repeat(30));

        MyMap<String, Double> topCats = analysis.topCategories();
        if (topCats.isEmpty()) {
            System.out.println(INFO_PREFIX + "No expenditure data available.");
            System.out.println("💡 Add some expenditures first using option 1.");
        } else {
            System.out.println(INFO_PREFIX + "Highest spending categories (GHS):");
            System.out.println("-".repeat(40));
            for (MyMap.Entry<String, Double> entry : topCats.entrySet()) {
                System.out.println("📊 " + entry.getKey() + ": GHS " + String.format("%.2f", entry.getValue()));
            }
        }
    }

    /**
     * Sort expenditures by category
     */
    private static void sortByCategory() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("🗂️ EXPENDITURES BY CATEGORY");
        System.out.println("=".repeat(30));

        MyList<Expenditure> sorted = expenditureManager.sortByCategory();
        if (sorted.isEmpty()) {
            System.out.println(INFO_PREFIX + "No expenditures to sort.");
        } else {
            System.out.println(INFO_PREFIX + "Expenditures sorted by category:");
            System.out.println("-".repeat(50));
            for (Expenditure exp : sorted) {
                System.out.println(exp);
            }
        }
    }

    /**
     * Sort expenditures by date
     */
    private static void sortByDate() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("📅 EXPENDITURES BY DATE");
        System.out.println("=".repeat(30));

        MyList<Expenditure> sorted = expenditureManager.sortByDate();
        if (sorted.isEmpty()) {
            System.out.println(INFO_PREFIX + "No expenditures to sort.");
        } else {
            System.out.println(INFO_PREFIX + "Expenditures sorted by date:");
            System.out.println("-".repeat(50));
            for (Expenditure exp : sorted) {
                System.out.println(exp);
            }
        }
    }

    /**
     * Search expenditures with validation
     */
    private static void searchMenu() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("🔍 SEARCH EXPENDITURES");
        System.out.println("=".repeat(30));
        System.out.println("1. 📅 By Date Range");
        System.out.println("2. 📊 By Category");
        System.out.println("3. 💰 By Cost Range");
        System.out.println("4. 🏦 By Bank Account");
        System.out.println("=".repeat(30));

        int option = getValidSearchOption();

        switch (option) {
            case 1:
                searchByDateRange();
                break;
            case 2:
                searchByCategory();
                break;
            case 3:
                searchByCostRange();
                break;
            case 4:
                searchByAccount();
                break;
            default:
                System.out.println(ERROR_PREFIX + "Invalid search option.");
                break;
        }
    }

    /**
     * Get valid search option
     */
    private static int getValidSearchOption() {
        while (true) {
            try {
                System.out.print("👉 Choose search option (1-4): ");
                int option = scanner.nextInt();
                scanner.nextLine();

                if (option >= 1 && option <= 4) {
                    return option;
                } else {
                    System.out.println(WARNING_PREFIX + "Please enter a number between 1 and 4.");
                }
            } catch (InputMismatchException e) {
                System.out.println(ERROR_PREFIX + "Please enter a valid number (1-4).");
                scanner.nextLine();
            }
        }
    }

    /**
     * Search by date range
     */
    private static void searchByDateRange() {
        try {
            System.out.println("\n📅 SEARCH BY DATE RANGE");
            System.out.println("-".repeat(30));

            Date start = getValidDate("📅 Enter Start Date (dd-MM-yyyy): ");
            Date end = getValidDate("📅 Enter End Date (dd-MM-yyyy): ");

            if (start.after(end)) {
                System.out.println(ERROR_PREFIX + "Start date cannot be after end date!");
                return;
            }

            MyList<Expenditure> result = expenditureManager.searchByDateRange(start, end);
            showSearchResults(result, "Date Range: " + dateFormat.format(start) + " to " + dateFormat.format(end));

        } catch (Exception e) {
            System.out.println(ERROR_PREFIX + "Error searching by date range: " + e.getMessage());
        }
    }

    /**
     * Search by category
     */
    private static void searchByCategory() {
        System.out.println("\n📊 SEARCH BY CATEGORY");
        System.out.println("-".repeat(30));

        String category = getValidString("📊 Enter Category Name: ",
                "Category name cannot be empty!");

        MyList<Expenditure> result = expenditureManager.searchByCategory(category);
        showSearchResults(result, "Category: " + category);
    }

    /**
     * Search by cost range
     */
    private static void searchByCostRange() {
        System.out.println("\n💰 SEARCH BY COST RANGE");
        System.out.println("-".repeat(30));

        double min = getValidAmount("💰 Enter Minimum Amount (GHS): ");
        double max = getValidAmount("💰 Enter Maximum Amount (GHS): ");

        if (min > max) {
            System.out.println(ERROR_PREFIX + "Minimum amount cannot be greater than maximum amount!");
            return;
        }

        MyList<Expenditure> result = expenditureManager.searchByCostRange(min, max);
        showSearchResults(result, "Cost Range: GHS " + min + " to GHS " + max);
    }

    /**
     * Search by account
     */
    private static void searchByAccount() {
        System.out.println("\n🏦 SEARCH BY BANK ACCOUNT");
        System.out.println("-".repeat(30));

        String accountId = getValidString("🏦 Enter Bank Account ID: ",
                "Bank Account ID cannot be empty!");

        MyList<Expenditure> result = expenditureManager.searchByAccount(accountId);
        showSearchResults(result, "Bank Account: " + accountId);
    }

    /**
     * Upload receipt with validation
     */
    private static void uploadReceipt() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("📤 UPLOAD RECEIPT");
        System.out.println("=".repeat(30));

        String path = getValidString("📁 Enter receipt file path (e.g., receipts/12345.jpg): ",
                "Receipt file path cannot be empty!");

        receiptManager.uploadReceipt(path);
        System.out.println(SUCCESS_PREFIX + "Receipt uploaded successfully!");
        System.out.println(INFO_PREFIX + "Receipt added to review queue: " + path);
    }

    /**
     * Review receipt
     */
    private static void reviewReceipt() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("🔍 REVIEW RECEIPT");
        System.out.println("=".repeat(30));

        if (receiptManager.isEmpty()) {
            System.out.println(INFO_PREFIX + "No receipts to review.");
            System.out.println("💡 Use option 8 to upload receipts first.");
            return;
        }

        String next = receiptManager.peekNextReceipt();
        System.out.println("🔍 Next receipt to review: " + next);
        System.out.println("-".repeat(40));

        String confirm = getValidYesNo("✅ Mark as reviewed? (y/n): ");

        if (confirm.equalsIgnoreCase("y")) {
            receiptManager.reviewReceipt(); // dequeue
            System.out.println(SUCCESS_PREFIX + "Receipt reviewed and removed from queue.");
        } else {
            System.out.println(INFO_PREFIX + "Receipt left in queue for later review.");
        }
    }

    /**
     * Show search results
     */
    private static void showSearchResults(MyList<Expenditure> list, String searchCriteria) {
        System.out.println("\n📋 SEARCH RESULTS");
        System.out.println("🔍 Search: " + searchCriteria);
        System.out.println("=".repeat(50));

        if (list.isEmpty()) {
            System.out.println(INFO_PREFIX + "No expenditures found matching your search criteria.");
            System.out.println("💡 Try adjusting your search parameters.");
        } else {
            System.out.println(SUCCESS_PREFIX + "Found " + list.size() + " matching expenditure(s):");
            System.out.println("-".repeat(50));
            for (Expenditure exp : list) {
                System.out.println(exp);
            }
        }
    }

    /**
     * Exit application safely
     */
    private static void exitApplication() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("👋 THANK YOU FOR USING NKWA EXPENDITURE MANAGEMENT!");
        System.out.println("=".repeat(50));
        System.out.println("💾 All data has been processed.");
        System.out.println("🔒 Application closing safely...");
        System.out.println("=".repeat(50));
        System.exit(0);
    }

    // ============ UTILITY METHODS FOR INPUT VALIDATION ============

    /**
     * Get valid string input (non-empty)
     */
    private static String getValidString(String prompt, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println(ERROR_PREFIX + errorMessage);
            System.out.println(INFO_PREFIX + "Please try again.");
        }
    }

    /**
     * Get valid amount (positive number)
     */
    private static double getValidAmount(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double amount = scanner.nextDouble();
                scanner.nextLine(); // consume newline

                if (amount > 0) {
                    return amount;
                } else {
                    System.out.println(ERROR_PREFIX + "Amount must be greater than 0!");
                    System.out.println(INFO_PREFIX + "Please enter a positive number.");
                }
            } catch (InputMismatchException e) {
                System.out.println(ERROR_PREFIX + "Please enter a valid number!");
                System.out.println(INFO_PREFIX + "Example: 100.50");
                scanner.nextLine(); // clear invalid input
            }
        }
    }

    /**
     * Get valid date input
     */
    private static Date getValidDate(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String dateStr = scanner.nextLine().trim();

                if (dateStr.isEmpty()) {
                    System.out.println(ERROR_PREFIX + "Date cannot be empty!");
                    System.out.println(INFO_PREFIX + "Please enter date in format: dd-MM-yyyy (e.g., 25-12-2023)");
                    continue;
                }

                Date date = dateFormat.parse(dateStr);
                return date;

            } catch (Exception e) {
                System.out.println(ERROR_PREFIX + "Invalid date format!");
                System.out.println(INFO_PREFIX + "Please use format: dd-MM-yyyy (e.g., 25-12-2023)");
                System.out.println(INFO_PREFIX + "Make sure day is 01-31, month is 01-12, and year is 4 digits.");
            }
        }
    }

    /**
     * Get valid yes/no input
     */
    private static String getValidYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y") || input.equals("yes") || input.equals("n") || input.equals("no")) {
                return input;
            }

            System.out.println(ERROR_PREFIX + "Please enter 'y' for yes or 'n' for no.");
        }
    }
}