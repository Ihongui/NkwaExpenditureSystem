package managers;

import models.Expenditure;
import utils.MyHashMap;
import utils.MyList;
import utils.MyMap;
import utils.FileStorage;
import utils.MyArrayList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Manages storage and retrieval of expenditures using custom data structures.
 */
public class ExpenditureManager {

    private final MyMap<String, Expenditure> expenditures; // code → expenditure
    private final MyList<Expenditure> expenditureList;

    public ExpenditureManager() {
        expenditures = new MyHashMap<>();
        expenditureList = new MyArrayList<>();
    }

    public void addExpenditure(Expenditure exp) {
        expenditures.put(exp.getCode(), exp);
        expenditureList.add(exp);
        
    }

    public MyList<Expenditure> getAll() {
        return expenditureList;
    }

    public MyList<Expenditure> searchByDateRange(Date start, Date end) {
        MyList<Expenditure> result = new MyArrayList<>();
        for (int i = 0; i < expenditureList.size(); i++) {
            Expenditure exp = expenditureList.get(i);
            if (!exp.getDate().before(start) && !exp.getDate().after(end)) {
                result.add(exp);
            }
        }
        return result;
    }

    public MyList<Expenditure> searchByCategory(String category) {
        MyList<Expenditure> result = new MyArrayList<>();
        for (int i = 0; i < expenditureList.size(); i++) {
            Expenditure exp = expenditureList.get(i);
            if (exp.getCategory().equalsIgnoreCase(category)) {
                result.add(exp);
            }
        }
        return result;
    }

    public MyList<Expenditure> searchByCostRange(double min, double max) {
        MyList<Expenditure> result = new MyArrayList<>();
        for (int i = 0; i < expenditureList.size(); i++) {
            Expenditure exp = expenditureList.get(i);
            if (exp.getAmount() >= min && exp.getAmount() <= max) {
                result.add(exp);
            }
        }
        return result;
    }

    public MyList<Expenditure> searchByAccount(String accountId) {
        MyList<Expenditure> result = new MyArrayList<>();
        for (int i = 0; i < expenditureList.size(); i++) {
            Expenditure exp = expenditureList.get(i);
            if (exp.getBankAccountId().equals(accountId)) {
                result.add(exp);
            }
        }
        return result;
    }

    public MyList<Expenditure> sortByCategory() {
        MyList<Expenditure> sorted = cloneList();
        for (int i = 0; i < sorted.size(); i++) {
            for (int j = 0; j < sorted.size() - i - 1; j++) {
                if (sorted.get(j).getCategory().compareToIgnoreCase(sorted.get(j + 1).getCategory()) > 0) {
                    Expenditure temp = sorted.get(j);
                    sorted.set(j, sorted.get(j + 1));
                    sorted.set(j + 1, temp);
                }
            }
        }
        return sorted;
    }

    public MyList<Expenditure> sortByDate() {
        MyList<Expenditure> sorted = cloneList();
        for (int i = 0; i < sorted.size(); i++) {
            for (int j = 0; j < sorted.size() - i - 1; j++) {
                if (sorted.get(j).getDate().after(sorted.get(j + 1).getDate())) {
                    Expenditure temp = sorted.get(j);
                    sorted.set(j, sorted.get(j + 1));
                    sorted.set(j + 1, temp);
                }
            }
        }
        return sorted;
    }

    private MyList<Expenditure> cloneList() {
        MyList<Expenditure> copy = new MyArrayList<>();
        for (int i = 0; i < expenditureList.size(); i++) {
            copy.add(expenditureList.get(i));
        }
        return copy;
    }

    public Expenditure getByCode(String code) {
        return expenditures.get(code);
    }

    public int count() {
        return expenditureList.size();
    }

    /**
     * Save expenditures to file.
     */
    public void saveToFile(String filepath) {
        MyList<String> lines = new MyArrayList<>();
        for (int i = 0; i < expenditureList.size(); i++) {
            lines.add(expenditureList.get(i).toFileString());
        }
        FileStorage.writeLines(filepath, lines);
    }

    /**
     * Load expenditures from file.
     */
    public void loadFromFile(String filepath) {
        MyList<String> lines = FileStorage.readLines(filepath); // <- Already returns MyList<String>
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(",");
            if (parts.length >= 6) {
                try {
                    String code = parts[0].trim();
                    double amount = Double.parseDouble(parts[1].trim());
                    Date date = new SimpleDateFormat("dd-MM-yyyy").parse(parts[2].trim());
                    String phase = parts[3].trim();
                    String category = parts[4].trim();
                    String account = parts[5].trim();
                    String receipt = parts.length >= 7 ? parts[6].trim() : "";

                    addExpenditure(new Expenditure(code, amount, date, phase, category, account, receipt));
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Skipping invalid line (amount): " + line);
                } catch (ParseException e) {
                    System.out.println("⚠️ Skipping invalid line (date): " + line);
                }
            } else {
                System.out.println("⚠️ Skipping incomplete line: " + line);
            }
        }
    }
}
