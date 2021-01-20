package hr.ferit.dominikzivko.myfinance.ui.dest.bills;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import hr.ferit.dominikzivko.myfinance.data.BillDetails;
import hr.ferit.dominikzivko.myfinance.data.BillRepository;

public class BillDetailsViewModel extends ViewModel {

    private final BillRepository billRepository;

    private LiveData<BillDetails> billDetails;

    public BillDetailsViewModel(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public LiveData<BillDetails> getBillDetails() {
        return billDetails;
    }

    public void setBill(int billID) {
        billDetails = billRepository.getBillByID(billID);
    }

    public void deleteCurrentBill() {
        billRepository.deleteBill(getBillDetails().getValue().getBill().getId());
    }
}