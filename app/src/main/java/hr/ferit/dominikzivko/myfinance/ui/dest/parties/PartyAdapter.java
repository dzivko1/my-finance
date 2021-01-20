package hr.ferit.dominikzivko.myfinance.ui.dest.parties;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

import hr.ferit.dominikzivko.myfinance.data.Party;
import hr.ferit.dominikzivko.myfinance.databinding.ItemPartyBinding;

public class PartyAdapter extends ListAdapter<Party, PartyAdapter.ViewHolder> {

    private final ItemClickListener itemClickListener;

    protected PartyAdapter(ItemClickListener itemClickListener) {
        super(Party.DIFF_CALLBACK);
        this.itemClickListener = itemClickListener;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPartyBinding binding = ItemPartyBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), itemClickListener);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemPartyBinding binding;

        public ViewHolder(ItemPartyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Party party, ItemClickListener clickListener) {
            binding.setParty(party);
            binding.setClickListener(clickListener);
            binding.executePendingBindings();
        }
    }

    public interface ItemClickListener extends Serializable {
        void onClick(Party party);
    }
}