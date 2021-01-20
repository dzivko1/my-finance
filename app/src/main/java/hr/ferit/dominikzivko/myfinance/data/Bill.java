package hr.ferit.dominikzivko.myfinance.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.Objects;

@Entity(
        foreignKeys = {
                @ForeignKey(entity = Category.class, parentColumns = {"id"}, childColumns = {"categoryID"}),
                @ForeignKey(entity = Party.class, parentColumns = {"id"}, childColumns = {"recipientPartyID"})
        },
        indices = {
                @Index("categoryID"),
                @Index("recipientPartyID")
        }
)
public class Bill {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private Date date;
    private int categoryID;
    private int recipientPartyID;
    private String description;
    private double value;

    @Ignore
    public Bill() {
    }

    public Bill(Date date, int categoryID, int recipientPartyID, String description, double value) {
        this.date = date;
        this.categoryID = categoryID;
        this.recipientPartyID = recipientPartyID;
        this.description = description;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getRecipientPartyID() {
        return recipientPartyID;
    }

    public void setRecipientPartyID(int recipientPartyID) {
        this.recipientPartyID = recipientPartyID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bill bill = (Bill) o;
        return categoryID == bill.categoryID &&
                recipientPartyID == bill.recipientPartyID &&
                Double.compare(bill.value, value) == 0 &&
                Objects.equals(date, bill.date) &&
                Objects.equals(description, bill.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, categoryID, recipientPartyID, description, value);
    }
}
