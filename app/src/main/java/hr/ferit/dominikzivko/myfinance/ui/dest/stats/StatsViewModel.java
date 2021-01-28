package hr.ferit.dominikzivko.myfinance.ui.dest.stats;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.List;

import hr.ferit.dominikzivko.myfinance.data.BillRepository;
import hr.ferit.dominikzivko.myfinance.data.CategoryDetails;
import hr.ferit.dominikzivko.myfinance.data.CategoryTotal;

public class StatsViewModel extends ViewModel {

    private final BillRepository billRepository;

    private LiveData<List<CategoryDetails>> categories;
    private LiveData<Integer> totalBills;
    private LiveData<Integer> totalCategories;
    private LiveData<Double> totalSpent;
    private LiveData<Double> monthSpent;

    private final HashMap<Integer, LiveData<CategoryTotal>> categoryTotalsCache = new HashMap<>();
    private final HashMap<Integer, LiveData<List<CategoryTotal>>> subcategoryTotalsCache = new HashMap<>();

    public StatsViewModel(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public LiveData<List<CategoryDetails>> getCategories() {
        if (categories == null) {
            categories = billRepository.getCategories();
        }
        return categories;
    }

    public LiveData<Integer> getTotalBills() {
        if (totalBills == null) {
            totalBills = billRepository.getBillCount();
        }
        return totalBills;
    }

    public LiveData<Integer> getTotalCategories() {
        if (totalCategories == null) {
            totalCategories = billRepository.getCategoryCount();
        }
        return totalCategories;
    }

    public LiveData<Double> getTotalSpent() {
        if (totalSpent == null) {
            totalSpent = billRepository.getSummedBillsValue();
        }
        return totalSpent;
    }

    public LiveData<Double> getMonthSpent() {
        if (monthSpent == null) {
            monthSpent = billRepository.getSummedBillsValueForThisMonth();
        }
        return monthSpent;
    }

    public LiveData<CategoryTotal> getTotalByCategory(int categoryID) {
        LiveData<CategoryTotal> categoryTotal = categoryTotalsCache.get(categoryID);
        if (categoryTotal == null) {
            categoryTotal = billRepository.getTotalByCategory(categoryID);
            categoryTotalsCache.put(categoryID, categoryTotal);
        }
        return categoryTotal;
    }

    public LiveData<List<CategoryTotal>> getTotalBySubcategories(int categoryID) {
        LiveData<List<CategoryTotal>> subcategoryTotals = subcategoryTotalsCache.get(categoryID);
        if (subcategoryTotals == null) {
            subcategoryTotals = billRepository.getTotalBySubcategories(categoryID);
            subcategoryTotalsCache.put(categoryID, subcategoryTotals);
        }
        return subcategoryTotals;
    }
}