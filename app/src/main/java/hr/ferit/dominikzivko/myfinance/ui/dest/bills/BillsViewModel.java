package hr.ferit.dominikzivko.myfinance.ui.dest.bills;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import hr.ferit.dominikzivko.myfinance.data.BillDetails;
import hr.ferit.dominikzivko.myfinance.data.BillRepository;

public class BillsViewModel extends ViewModel {

    private final BillRepository billRepository;

    private final LiveData<List<BillDetails>> bills;

    public BillsViewModel(BillRepository billRepository) {
        this.billRepository = billRepository;
        this.bills = billRepository.getBills();
    }

    public LiveData<List<BillDetails>> getBills() {
        return bills;
    }
}