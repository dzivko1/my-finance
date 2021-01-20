package hr.ferit.dominikzivko.myfinance.data;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import java.util.Objects;

public class BillDetails {

    @Embedded
    private Bill bill;
    @Relation(
            entity = Category.class,
            parentColumn = "categoryID", entityColumn = "id")
    private Category category;
    @Relation(
            entity = Party.class,
            parentColumn = "recipientPartyID",
            entityColumn = "id"
    )
    private Party recipient;

    @Ignore
    public BillDetails() {
    }

    public BillDetails(Bill bill, Category category, Party recipient) {
        this.bill = bill;
        this.category = category;
        this.recipient = recipient;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Party getRecipient() {
        return recipient;
    }

    public void setRecipient(Party recipient) {
        this.recipient = recipient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillDetails that = (BillDetails) o;
        return Objects.equals(bill, that.bill) &&
                Objects.equals(category, that.category) &&
                Objects.equals(recipient, that.recipient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bill, category, recipient);
    }

    @Ignore
    public static final DiffUtil.ItemCallback<BillDetails> DIFF_CALLBACK = new DiffUtil.ItemCallback<BillDetails>() {
        @Override
        public boolean areItemsTheSame(@NonNull BillDetails oldItem, @NonNull BillDetails newItem) {
            return oldItem.getBill().getId() == newItem.getBill().getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull BillDetails oldItem, @NonNull BillDetails newItem) {
            return oldItem.equals(newItem);
        }
    };
}
