package hr.ferit.dominikzivko.myfinance.ui.dest.stats;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.List;

import hr.ferit.dominikzivko.myfinance.data.BillRepository;
import hr.ferit.dominikzivko.myfinance.data.CategoryDetails;
import hr.ferit.dominikzivko.myfinance.data.DayBillValue;

public class CategoryStatsViewModel extends ViewModel {

    private final BillRepository billRepository;

    private final HashMap<Integer, LiveData<List<DayBillValue>>> dayBillValuesCache = new HashMap<>();
    private LiveData<List<CategoryDetails>> categories;

    public CategoryStatsViewModel(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public LiveData<List<DayBillValue>> getDayBillValues(int categoryID) {
        LiveData<List<DayBillValue>> dayBillValues = dayBillValuesCache.get(categoryID);
        if (dayBillValues == null) {
            dayBillValues = billRepository.getBillDayValuesForCategory(categoryID);
            dayBillValuesCache.put(categoryID, dayBillValues);
        }
        return dayBillValues;
    }

    public LiveData<List<CategoryDetails>> getCategories() {
        if (categories == null) {
            categories = billRepository.getCategories();
        }
        return categories;
    }
}