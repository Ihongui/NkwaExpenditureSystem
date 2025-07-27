package models;

import utils.MyList;
import utils.MyArrayList;

/**
 * Represents a bank account.
 */
public class BankAccount {
    private String accountId;
    private String bankName;
    private double balance;
    private MyList<String> expenditures; // Stores codes of expenditures linked to this account

    public BankAccount(String accountId, String bankName, double balance) {
        this.accountId = accountId;
        this.bankName = bankName;
        this.balance = balance;
        this.expenditures = new MyArrayList<>();
    }

    public String getAccountId() {
        return accountId;
    }

    public String getBankName() {
        return bankName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addExpenditure(String expenditureCode) {
        expenditures.add(expenditureCode);
    }

    public MyList<String> getExpenditures() {
        return expenditures;
    }
    // it is here we send data as a text
    public String toFileString() {
        return accountId + "," + bankName + "," + balance;
    }


    @Override
    public String toString() {
        return "🏦 Account ID: " + accountId +
               ", Bank: " + bankName +
               ", Balance: GHS " + balance +
               ", Transactions: " + expenditures.size();
    }
}
