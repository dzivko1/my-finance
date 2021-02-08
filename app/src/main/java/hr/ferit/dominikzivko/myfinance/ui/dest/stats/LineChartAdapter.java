package hr.ferit.dominikzivko.myfinance.ui.dest.stats;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.data.LineData;

import hr.ferit.dominikzivko.myfinance.databinding.ItemLineChartBinding;

public class LineChartAdapter extends ListAdapter<LineData, LineChartAdapter.ViewHolder> {

    private static final DiffUtil.ItemCallback<LineData> DIFF_CALLBACK = new DiffUtil.ItemCallback<LineData>() {
        @Override
        public boolean areItemsTheSame(@NonNull LineData oldItem, @NonNull LineData newItem) {
            return oldItem == newItem;
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull LineData oldItem, @NonNull LineData newItem) {
            return oldItem.equals(newItem);
        }
    };

    protected LineChartAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemLineChartBinding binding = ItemLineChartBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemLineChartBinding binding;

        public ViewHolder(@NonNull ItemLineChartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.chartLine.setAutoScaleMinMaxEnabled(true);
            binding.chartLine.getDescription().setEnabled(false);
        }

        public void bind(LineData data) {
            binding.chartLine.setData(data);
            binding.chartLine.notifyDataSetChanged();
            binding.chartLine.invalidate();
            binding.executePendingBindings();
        }
    }
}
