package managers;

import models.Expenditure;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Manages a collection of Expenditure records using a HashMap.
 * Handles loading from and saving to a text file.
 */
public class ExpenditureManager {

    private final HashMap<String, Expenditure> expenditures; // Key: code
    private final String filePath = "expenditures.txt";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public ExpenditureManager() {
        expenditures = new HashMap<>();
        loadFromFile(); // Load data on startup
    }

    /**
     * Adds a new expenditure to the system and saves it to file.
     */
    public void addExpenditure(Expenditure exp) {
        expenditures.put(exp.getCode(), exp);
        saveToFile();
    }

    /**
     * Finds an expenditure by its code.
     */
    public Expenditure findByCode(String code) {
        return expenditures.get(code);
    }

    /**
     * Returns a list of all expenditures.
     */
    public List<Expenditure> getAll() {
        return new ArrayList<>(expenditures.values());
    }

    /**
     * Loads expenditure data from a text file into memory.
     * Format: code|amount|date|phase|category|accountId|receiptPath
     */
    private void loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 7) {
                    String code = parts[0];
                    double amount = Double.parseDouble(parts[1]);
                    Date date = dateFormat.parse(parts[2]);
                    String phase = parts[3];
                    String category = parts[4];
                    String accountId = parts[5];
                    String receiptPath = parts[6];

                    Expenditure exp = new Expenditure(code, amount, date, phase, category, accountId, receiptPath);
                    expenditures.put(code, exp);
                }
            }

        } catch (Exception e) {
            System.out.println("Error loading expenditures: " + e.getMessage());
        }
    }

    /**
     * Saves all expenditure records to a text file.
     */
    private void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Expenditure exp : expenditures.values()) {
                String line = exp.getCode() + "|" +
                              exp.getAmount() + "|" +
                              dateFormat.format(exp.getDate()) + "|" +
                              exp.getPhase() + "|" +
                              exp.getCategory() + "|" +
                              exp.getAccountId() + "|" +
                              (exp.getReceiptPath() == null ? "" : exp.getReceiptPath());

                bw.write(line);
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving expenditures: " + e.getMessage());
        }
    }
    public List<Expenditure> searchByDateRange(Date start, Date end) {
        List<Expenditure> result = new ArrayList<>();
        for (Expenditure exp : expenditures.values()) {
            if (!exp.getDate().before(start) && !exp.getDate().after(end)) {
                result.add(exp);
            }
        }
        return result;
    }
    public List<Expenditure> searchByCategory(String category) {
        List<Expenditure> result = new ArrayList<>();
        for (Expenditure exp : expenditures.values()) {
            if (exp.getCategory().equalsIgnoreCase(category)) {
                result.add(exp);
            }
        }
        return result;
    }
    public List<Expenditure> searchByCostRange(double min, double max) {
        List<Expenditure> result = new ArrayList<>();
        for (Expenditure exp : expenditures.values()) {
            if (exp.getAmount() >= min && exp.getAmount() <= max) {
                result.add(exp);
            }
        }
        return result;
    }
    public List<Expenditure> searchByAccount(String accountId) {
        List<Expenditure> result = new ArrayList<>();
        for (Expenditure exp : expenditures.values()) {
            if (exp.getAccountId().equalsIgnoreCase(accountId)) {
                result.add(exp);
            }
        }
        return result;
    }

}
