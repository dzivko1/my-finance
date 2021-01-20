package hr.ferit.dominikzivko.myfinance.ui.dest.parties;

import androidx.lifecycle.ViewModel;

import hr.ferit.dominikzivko.myfinance.ActionFailedException;
import hr.ferit.dominikzivko.myfinance.R;
import hr.ferit.dominikzivko.myfinance.data.Address;
import hr.ferit.dominikzivko.myfinance.data.BillRepository;
import hr.ferit.dominikzivko.myfinance.data.Party;

public class PartyEditorViewModel extends ViewModel {

    private final BillRepository billRepository;

    private Party editedParty;

    public PartyEditorViewModel(BillRepository billRepository) {
        this.billRepository = billRepository;
        resetEditedParty();
    }

    public String getName() {
        return editedParty.getName();
    }

    public void setName(String name) {
        editedParty.setName(name);
    }

    public Address getAddress() {
        return editedParty.getAddress();
    }

    public void setEditedParty(Party party) {
        if (party == null) {
            resetEditedParty();
            return;
        }
        this.editedParty = party;
    }

    public void resetEditedParty() {
        editedParty = new Party();
        editedParty.setAddress(new Address());
    }

    public void tryApply() throws ActionFailedException {
        if (!checkValid()) throw new ActionFailedException(R.string.error_fill_all_fields);

        if (editedParty.getId() == 0) {
            billRepository.addParty(editedParty);
        } else {
            billRepository.updateParty(editedParty);
        }
    }

    private boolean checkValid() {
        return getName() != null && !getName().trim().isEmpty() &&
                checkAddressValid(getAddress());
    }

    private boolean checkAddressValid(Address address) {
        return address != null &&
                address.getStreet() != null &&
                address.getStreetNumber() != null &&
                address.getCity() != null &&
                address.getZipcode() != null &&
                !address.getStreet().trim().isEmpty() &&
                !address.getStreetNumber().trim().isEmpty() &&
                !address.getCity().trim().isEmpty() &&
                !address.getZipcode().trim().isEmpty();
    }
}