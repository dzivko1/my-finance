package hr.ferit.dominikzivko.myfinance.ui.dest.categories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import hr.ferit.dominikzivko.myfinance.data.BillRepository;
import hr.ferit.dominikzivko.myfinance.data.CategoryDetails;

public class CategoryDetailsViewModel extends ViewModel {

    private final BillRepository billRepository;

    private LiveData<CategoryDetails> categoryDetails;

    public CategoryDetailsViewModel(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public LiveData<CategoryDetails> getCategoryDetails() {
        return categoryDetails;
    }

    public void setCategory(int categoryID) {
        categoryDetails = billRepository.getCategoryByID(categoryID);
    }

    public void deleteCurrentCategory() {
        billRepository.deleteCategory(getCategoryDetails().getValue().getCategory().getId());
    }
}