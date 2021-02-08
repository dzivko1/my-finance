package hr.ferit.dominikzivko.myfinance.ui.dest.stats;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.List;

import hr.ferit.dominikzivko.myfinance.data.BillRepository;
import hr.ferit.dominikzivko.myfinance.data.DayBillValue;
import hr.ferit.dominikzivko.myfinance.data.Party;

public class PartyStatsViewModel extends ViewModel {

    private final BillRepository billRepository;

    private final HashMap<Integer, LiveData<List<DayBillValue>>> dayBillValuesCache = new HashMap<>();
    private LiveData<List<Party>> parties;

    public PartyStatsViewModel(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public LiveData<List<DayBillValue>> getDayBillValues(int recipientID) {
        LiveData<List<DayBillValue>> dayBillValues = dayBillValuesCache.get(recipientID);
        if (dayBillValues == null) {
            dayBillValues = billRepository.getDayBillValuesForRecipient(recipientID);
            dayBillValuesCache.put(recipientID, dayBillValues);
        }
        return dayBillValues;
    }

    public LiveData<List<Party>> getParties() {
        if (parties == null) {
            parties = billRepository.getParties();
        }
        return parties;
    }
}