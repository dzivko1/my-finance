package hr.ferit.dominikzivko.myfinance.data;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Party {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    @Embedded
    private Address address;

    @Ignore
    public Party() {
    }

    public Party(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Party party = (Party) o;
        return Objects.equals(name, party.name) &&
                Objects.equals(address, party.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address);
    }

    @Ignore
    public static final DiffUtil.ItemCallback<Party> DIFF_CALLBACK = new DiffUtil.ItemCallback<Party>() {
        @Override
        public boolean areItemsTheSame(@NonNull Party oldItem, @NonNull Party newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Party oldItem, @NonNull Party newItem) {
            return oldItem.equals(newItem);
        }
    };
}
