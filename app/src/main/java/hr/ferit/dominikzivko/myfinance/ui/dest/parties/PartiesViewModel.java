package hr.ferit.dominikzivko.myfinance.ui.dest.parties;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import hr.ferit.dominikzivko.myfinance.data.BillRepository;
import hr.ferit.dominikzivko.myfinance.data.Party;

public class PartiesViewModel extends ViewModel {

    private final BillRepository billRepository;

    private final LiveData<List<Party>> parties;

    public PartiesViewModel(BillRepository billRepository) {
        this.billRepository = billRepository;
        parties = billRepository.getParties();
    }

    public LiveData<List<Party>> getParties() {
        return parties;
    }
}
