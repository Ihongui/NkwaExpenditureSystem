package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a company bank account used for funding expenditures.
 */
public class BankAccount {
    private String accountId;      // Unique account ID
    private String bankName;       // Name of the bank
    private double balance;        // Current balance
    private List<String> expenditureCodes; // References to expenditures made from this account

    // Constructor to initialize a bank account
    public BankAccount(String accountId, String bankName, double balance) {
        this.accountId = accountId;
        this.bankName = bankName;
        this.balance = balance;
        this.expenditureCodes = new ArrayList<>();
    }

    // Getters
    public String getAccountId() { return accountId; }
    public String getBankName() { return bankName; }
    public double getBalance() { return balance; }
    public List<String> getExpenditureCodes() { return expenditureCodes; }

    // Add an expenditure reference to the list
    public void addExpenditure(String code) {
        expenditureCodes.add(code);
    }

    // Withdraw amount from balance (called during expenditure)
    public boolean withdraw(double amount) {
        if (amount > balance) {
            return false; // Not enough funds
        }
        balance -= amount;
        return true;
    }

    // Deposit amount (e.g., initial funding or refund)
    public void deposit(double amount) {
        balance += amount;
    }

    // Display bank account info
    @Override
    public String toString() {
        return "Bank Account [" +
               "ID: " + accountId +
               ", Bank: " + bankName +
               ", Balance: GHS " + balance +
               ", Transactions: " + expenditureCodes.size() +
               "]";
    }
}
