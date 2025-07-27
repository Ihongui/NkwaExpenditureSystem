package managers;

import models.BankAccount;
import utils.MyList;
import utils.MyMap;
import utils.MyHashMap;
import utils.FileStorage;
import utils.MyArrayList;

/**
 * Manages all bank accounts including creation, retrieval, and balance updates.
 */
public class BankAccountManager {

    private final MyMap<String, BankAccount> accounts;

    public BankAccountManager() {
        this.accounts = new MyHashMap<>();
    }

    /**
     * Adds a new bank account to the system.
     */
    public void addAccount(BankAccount account) {
        accounts.put(account.getAccountId(), account);
    }

    /**
     * Retrieves a bank account by its ID.
     */
    public BankAccount getAccount(String id) {
        return accounts.get(id);
    }

    /**
     * Returns a list of all bank accounts in the system.
     */
    public MyList<BankAccount> getAllAccounts() {
        MyList<BankAccount> list = new MyArrayList<>();
        for (MyMap.Entry<String, BankAccount> entry : accounts.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }

    /**
     * Withdraws money from the specified bank account if balance is sufficient.
     */
    public boolean withdrawFromAccount(String accountId, double amount) {
        BankAccount acc = getAccount(accountId);
        if (acc != null && acc.getBalance() >= amount) {
            acc.setBalance(acc.getBalance() - amount);
            return true;
        }
        return false;
    }

    /**
     * Deposits money to a specified bank account.
     */
    public boolean depositToAccount(String accountId, double amount) {
        BankAccount acc = getAccount(accountId);
        if (acc != null) {
            acc.setBalance(acc.getBalance() + amount);
            return true;
        }
        return false;
    }

    /**
     * Checks if an account exists by its ID.
     */
    public boolean accountExists(String id) {
        return accounts.containsKey(id);
    }

    /**
     * Saves all bank accounts to a file.
     */
    public void saveToFile(String filepath) {
        MyList<String> lines = new MyArrayList<>();
        for (BankAccount acc : getAllAccounts()) {
            lines.add(acc.toFileString()); // You must implement toFileString() in BankAccount
        }
        FileStorage.writeLines(filepath, lines);
    }

    /**
     * Loads all bank accounts from a file.
     */
    public void loadFromFile(String filepath) {
        MyList<String> lines = FileStorage.readLines(filepath);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(",");
            if (parts.length >= 3) {
                try {
                    String id = parts[0].trim();
                    String bank = parts[1].trim();
                    double balance = Double.parseDouble(parts[2].trim());

                    BankAccount acc = new BankAccount(id, bank, balance);
                    addAccount(acc);
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Invalid balance in line: " + line);
                }
            } else {
                System.out.println("⚠️ Skipping invalid account line: " + line);
            }
        }
    }
}
