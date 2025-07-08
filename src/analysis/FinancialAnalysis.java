package analysis;

import managers.ExpenditureManager;
import models.Expenditure;

import java.text.SimpleDateFormat;
import java.util.*;

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
    public Map<String, Double> calculateMonthlyBurnRate() {
        Map<String, Double> burnMap = new TreeMap<>(); // Sorted by month

        for (Expenditure exp : expManager.getAll()) {
            String month = monthFormat.format(exp.getDate());
            burnMap.put(month, burnMap.getOrDefault(month, 0.0) + exp.getAmount());
        }

        return burnMap;
    }

    /**
     * Predicts profitability trend (very basic logic: increasing/decreasing monthly spend).
     */
    public void forecastProfitability() {
        Map<String, Double> burn = calculateMonthlyBurnRate();
        List<Double> values = new ArrayList<>(burn.values());

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
     * Returns top categories by total expenditure.
     */
    public Map<String, Double> topCategories() {
        Map<String, Double> categoryMap = new HashMap<>();

        for (Expenditure exp : expManager.getAll()) {
            String cat = exp.getCategory().toLowerCase();
            categoryMap.put(cat, categoryMap.getOrDefault(cat, 0.0) + exp.getAmount());
        }

        return sortMapByValueDescending(categoryMap);
    }

    /**
     * Utility: Sorts map by value (descending)
     */
    private Map<String, Double> sortMapByValueDescending(Map<String, Double> map) {
        List<Map.Entry<String, Double>> list = new ArrayList<>(map.entrySet());
        list.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        Map<String, Double> sorted = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : list) {
            sorted.put(entry.getKey(), entry.getValue());
        }

        return sorted;
    }
}
