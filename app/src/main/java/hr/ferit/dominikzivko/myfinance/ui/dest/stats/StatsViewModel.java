package hr.ferit.dominikzivko.myfinance.ui.dest.stats;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import hr.ferit.dominikzivko.myfinance.data.BillRepository;
import hr.ferit.dominikzivko.myfinance.data.CategoryTotal;

public class StatsViewModel extends ViewModel {

    private final BillRepository billRepository;

    private final LiveData<Integer> totalBills;
    private final LiveData<Integer> totalCategories;
    private final LiveData<Double> totalSpent;
    private final LiveData<Double> monthSpent;
    private final LiveData<List<CategoryTotal>> totalByCategory;

    public StatsViewModel(BillRepository billRepository) {
        this.billRepository = billRepository;
        this.totalBills = billRepository.getBillCount();
        this.totalCategories = billRepository.getCategoryCount();
        this.totalSpent = billRepository.getSummedBillsValue();
        this.monthSpent = billRepository.getSummedBillsValueForThisMonth();
        this.totalByCategory = billRepository.getTotalByCategory();
    }

    public LiveData<Integer> getTotalBills() {
        return totalBills;
    }

    public LiveData<Integer> getTotalCategories() {
        return totalCategories;
    }

    public LiveData<Double> getTotalSpent() {
        return totalSpent;
    }

    public LiveData<Double> getMonthSpent() {
        return monthSpent;
    }

    public LiveData<List<CategoryTotal>> getTotalByCategory() {
        return totalByCategory;
    }
}