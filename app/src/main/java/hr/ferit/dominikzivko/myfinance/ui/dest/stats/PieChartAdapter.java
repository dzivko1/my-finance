package hr.ferit.dominikzivko.myfinance.ui.dest.stats;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.data.PieData;

import hr.ferit.dominikzivko.myfinance.databinding.ItemPieChartBinding;

public class PieChartAdapter extends ListAdapter<PieData, PieChartAdapter.ViewHolder> {

    private static final DiffUtil.ItemCallback<PieData> DIFF_CALLBACK = new DiffUtil.ItemCallback<PieData>() {
        @Override
        public boolean areItemsTheSame(@NonNull PieData oldItem, @NonNull PieData newItem) {
            return oldItem == newItem;
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull PieData oldItem, @NonNull PieData newItem) {
            return oldItem.equals(newItem);
        }
    };

    protected PieChartAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPieChartBinding binding = ItemPieChartBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemPieChartBinding binding;

        public ViewHolder(@NonNull ItemPieChartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.chartPie.getDescription().setEnabled(false);
        }

        public void bind(PieData data) {
            binding.chartPie.setVisibility(data.getEntryCount() > 0 ? View.VISIBLE : View.GONE);
            binding.chartPie.setData(data);
            binding.chartPie.notifyDataSetChanged();
            binding.chartPie.invalidate();
            binding.executePendingBindings();
        }
    }
}
