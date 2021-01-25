package hr.ferit.dominikzivko.myfinance.ui.dest.categories;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

import hr.ferit.dominikzivko.myfinance.data.Category;
import hr.ferit.dominikzivko.myfinance.data.CategoryDetails;
import hr.ferit.dominikzivko.myfinance.databinding.ItemCategoryBinding;

public class CategoryAdapter extends ListAdapter<CategoryDetails, CategoryAdapter.ViewHolder> {

    private final ItemClickListener itemClickListener;

    public CategoryAdapter(ItemClickListener itemClickListener) {
        super(CategoryDetails.DIFF_CALLBACK);
        this.itemClickListener = itemClickListener;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), itemClickListener);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemCategoryBinding binding;

        public ViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CategoryDetails categoryDetails, ItemClickListener clickListener) {
            binding.setCategoryDetails(categoryDetails);
            binding.setClickListener(clickListener);
            binding.executePendingBindings();
        }
    }

    public interface ItemClickListener extends Serializable {
        void onClick(Category category);
    }
}