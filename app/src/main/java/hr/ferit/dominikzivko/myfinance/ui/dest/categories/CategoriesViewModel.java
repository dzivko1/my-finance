package hr.ferit.dominikzivko.myfinance.ui.dest.categories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import hr.ferit.dominikzivko.myfinance.data.BillRepository;
import hr.ferit.dominikzivko.myfinance.data.Category;

public class CategoriesViewModel extends ViewModel {

    private final BillRepository billRepository;

    private final LiveData<List<Category>> categories;

    public CategoriesViewModel(BillRepository billRepository) {
        this.billRepository = billRepository;
        this.categories = billRepository.getCategories();
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }
}
