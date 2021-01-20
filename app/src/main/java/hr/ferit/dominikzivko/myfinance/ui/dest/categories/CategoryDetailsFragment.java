package hr.ferit.dominikzivko.myfinance.ui.dest.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import hr.ferit.dominikzivko.myfinance.R;
import hr.ferit.dominikzivko.myfinance.databinding.FragmentCategoryDetailsBinding;
import hr.ferit.dominikzivko.myfinance.ui.ViewModelProviders;
import hr.ferit.dominikzivko.myfinance.ui.dest.MainActivity;

public class CategoryDetailsFragment extends Fragment {

    private CategoryDetailsViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        viewModel = ViewModelProviders.withBillRepository(this)
                .get(CategoryDetailsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentCategoryDetailsBinding binding = FragmentCategoryDetailsBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int categoryID = CategoryDetailsFragmentArgs.fromBundle(requireArguments()).getCategoryID();
        viewModel.setCategory(categoryID);
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity activity = (MainActivity) requireActivity();
        activity.setFabAction(v -> activity.startNewCategory());
        activity.setFabIcon(R.drawable.ic_baseline_category_add_24);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.details_actions, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_details_edit:
                editCategory();
                return true;
            case R.id.action_details_delete:
                deleteCategory();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editCategory() {
        ((MainActivity) requireActivity()).editCategory(viewModel.getCategoryDetails().getValue());
    }

    private void deleteCategory() {
        viewModel.deleteCurrentCategory();
        navigateUp();
    }

    private void navigateUp() {
        NavHostFragment.findNavController(this).navigateUp();
    }
}