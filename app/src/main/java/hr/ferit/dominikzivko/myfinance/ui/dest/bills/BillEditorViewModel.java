package hr.ferit.dominikzivko.myfinance.ui.dest.bills;

import androidx.lifecycle.ViewModel;

import java.util.Date;

import hr.ferit.dominikzivko.myfinance.ActionFailedException;
import hr.ferit.dominikzivko.myfinance.R;
import hr.ferit.dominikzivko.myfinance.data.Bill;
import hr.ferit.dominikzivko.myfinance.data.BillDetails;
import hr.ferit.dominikzivko.myfinance.data.BillRepository;
import hr.ferit.dominikzivko.myfinance.data.Category;
import hr.ferit.dominikzivko.myfinance.data.Party;

public class BillEditorViewModel extends ViewModel {

    private final BillRepository billRepository;

    private Bill editedBill;
    private Category category;
    private Party recipient;

    public BillEditorViewModel(BillRepository billRepository) {
        this.billRepository = billRepository;
        resetEditedBill();
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
        this.editedBill.setCategoryID(
                category != null ? category.getId() : Category.getRootCategory().getId());
    }

    public Party getRecipient() {
        return recipient;
    }

    public void setRecipient(Party recipient) {
        this.recipient = recipient;
        this.editedBill.setRecipientPartyID(recipient != null ? recipient.getId() : 0);
    }

    public Date getDate() {
        return editedBill.getDate();
    }

    public void setDate(Date date) {
        this.editedBill.setDate(date);
    }

    public String getDescription() {
        return this.editedBill.getDescription();
    }

    public void setDescription(String description) {
        this.editedBill.setDescription(description);
    }

    public double getValue() {
        return this.editedBill.getValue();
    }

    public void setValue(double value) {
        this.editedBill.setValue(value);
    }

    public void setEditedBill(BillDetails billDetails) {
        if (billDetails == null) {
            resetEditedBill();
            return;
        }

        this.editedBill = billDetails.getBill();
        setCategory(billDetails.getCategory());
        setRecipient(billDetails.getRecipient());
    }

    public void resetEditedBill() {
        this.editedBill = new Bill();
        setCategory(null);
        setRecipient(null);
    }

    public void tryApply() throws ActionFailedException {
        if (!checkValid()) throw new ActionFailedException(R.string.error_fill_bill);

        if (editedBill.getId() == 0) {
            billRepository.addBill(editedBill);
        } else {
            billRepository.updateBill(editedBill);
        }
    }

    private boolean checkValid() {
        return getDate() != null && getRecipient() != null;
    }
}