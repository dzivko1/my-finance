package hr.ferit.dominikzivko.myfinance.ui.dest.parties;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import hr.ferit.dominikzivko.myfinance.data.BillRepository;
import hr.ferit.dominikzivko.myfinance.data.Party;

public class PartyDetailsViewModel extends ViewModel {

    private final BillRepository billRepository;

    private LiveData<Party> party;

    public PartyDetailsViewModel(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public LiveData<Party> getParty() {
        return party;
    }

    public void setParty(int partyID) {
        party = billRepository.getPartyByID(partyID);
    }

    public void deleteCurrentParty() {
        billRepository.deleteParty(getParty().getValue().getId());
    }
}