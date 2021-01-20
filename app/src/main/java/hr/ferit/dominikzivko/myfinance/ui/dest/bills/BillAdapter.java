package hr.ferit.dominikzivko.myfinance.ui.dest.bills;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import hr.ferit.dominikzivko.myfinance.data.BillDetails;
import hr.ferit.dominikzivko.myfinance.databinding.ItemBillBinding;

public class BillAdapter extends ListAdapter<BillDetails, BillAdapter.ViewHolder> {

    private final ItemClickListener itemClickListener;

    protected BillAdapter(ItemClickListener itemClickListener) {
        super(BillDetails.DIFF_CALLBACK);
        this.itemClickListener = itemClickListener;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemBillBinding binding = ItemBillBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(getItem(position), itemClickListener);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemBillBinding binding;

        public ViewHolder(ItemBillBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(BillDetails bill, ItemClickListener clickListener) {
            binding.setBillDetails(bill);
            binding.setClickListener(clickListener);
            binding.executePendingBindings();
        }
    }

    public interface ItemClickListener {
        void onClick(int billID);
    }
}