package analysis;

import managers.ExpenditureManager;
import models.Expenditure;
import utils.*;

import java.text.SimpleDateFormat;

/**
 * Provides financial analytics such as burn rate and spending trends.
 */
public class FinancialAnalysis {

    private ExpenditureManager expManager;
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MM-yyyy");

    public FinancialAnalysis(ExpenditureManager expManager) {
        this.expManager = expManager;
    }

    /**
     * Calculates total expenditure per month (burn rate).
     */
    public MyMap<String, Double> calculateMonthlyBurnRate() {
        MyMap<String, Double> burnMap = new MyHashMap<>();

        for (Expenditure exp : expManager.getAll()) {
            String month = monthFormat.format(exp.getDate());

            // Emulate getOrDefault
            Double current = burnMap.get(month);
            if (current == null) current = 0.0;

            burnMap.put(month, current + exp.getAmount());
        }

        return burnMap;
    }

    /**
     * Predicts profitability trend based on monthly burn rate.
     */
    public void forecastProfitability() {
        MyMap<String, Double> burn = calculateMonthlyBurnRate();

        // Convert values to list (manual since we don’t use java.util)
        MyList<Double> values = new MyArrayList<>();
        for (MyMap.Entry<String, Double> entry : burn.entrySet()) {
            values.add(entry.getValue());
        }

        if (values.size() < 2) {
            System.out.println("❌ Not enough data to forecast.");
            return;
        }

        double last = values.get(values.size() - 1);
        double secondLast = values.get(values.size() - 2);

        if (last < secondLast) {
            System.out.println("📈 Trend: Spending is decreasing. Profit outlook is positive.");
        } else if (last > secondLast) {
            System.out.println("📉 Trend: Spending is increasing. Profit may reduce.");
        } else {
            System.out.println("🔄 Trend: Spending is stable. Monitor future expenses.");
        }
    }

    /**
     * Returns top spending categories.
     */
    public MyMap<String, Double> topCategories() {
        MyMap<String, Double> categoryMap = new MyHashMap<>();

        for (Expenditure exp : expManager.getAll()) {
            String cat = exp.getCategory().toLowerCase();

            Double current = categoryMap.get(cat);
            if (current == null) current = 0.0;

            categoryMap.put(cat, current + exp.getAmount());
        }

        return sortMapByValueDescending(categoryMap);
    }

    /**
     * Utility: Sorts map by value (descending)
     */
    private MyMap<String, Double> sortMapByValueDescending(MyMap<String, Double> map) {
        MyList<MyMap.Entry<String, Double>> entries = new MyArrayList<>();
        for (MyMap.Entry<String, Double> entry : map.entrySet()) {
            entries.add(entry);
        }

        // Manual bubble sort (or replace with your comparator if defined)
        for (int i = 0; i < entries.size() - 1; i++) {
            for (int j = 0; j < entries.size() - i - 1; j++) {
                if (entries.get(j).getValue() < entries.get(j + 1).getValue()) {
                    MyMap.Entry<String, Double> temp = entries.get(j);
                    entries.set(j, entries.get(j + 1));
                    entries.set(j + 1, temp);
                }
            }
        }

        MyMap<String, Double> sorted = new MyHashMap<>();
        for (MyMap.Entry<String, Double> entry : entries) {
            sorted.put(entry.getKey(), entry.getValue());
        }

        return sorted;
    }
}
