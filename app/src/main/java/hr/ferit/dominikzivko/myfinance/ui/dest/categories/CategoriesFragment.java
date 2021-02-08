package hr.ferit.dominikzivko.myfinance.ui.dest.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import hr.ferit.dominikzivko.myfinance.R;
import hr.ferit.dominikzivko.myfinance.databinding.FragmentCategoriesBinding;
import hr.ferit.dominikzivko.myfinance.ui.ViewModelProviders;
import hr.ferit.dominikzivko.myfinance.ui.dest.MainActivity;

public class CategoriesFragment extends Fragment {

    private CategoriesViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.withBillRepository(this)
                .get(CategoriesViewModel.class);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentCategoriesBinding binding = FragmentCategoriesBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        setupRecycler(binding);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity activity = (MainActivity) requireActivity();
        activity.setFabAction(v -> activity.startNewCategory());
        activity.setFabIcon(R.drawable.ic_baseline_category_add_24);
    }

    private void setupRecycler(FragmentCategoriesBinding binding) {
        CategoryAdapter.ItemClickListener itemClickListener =
                CategoriesFragmentArgs.fromBundle(requireArguments()).getItemClickListener();
        CategoryAdapter categoryAdapter = new CategoryAdapter(itemClickListener);
        binding.rvCategories.setAdapter(categoryAdapter);

        viewModel.getCategories().observe(getViewLifecycleOwner(), categoryAdapter::submitList);
    }
}