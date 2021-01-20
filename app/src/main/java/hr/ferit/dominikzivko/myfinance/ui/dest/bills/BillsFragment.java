package hr.ferit.dominikzivko.myfinance.ui.dest.bills;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;

import org.jetbrains.annotations.NotNull;

import hr.ferit.dominikzivko.myfinance.databinding.FragmentBillsBinding;
import hr.ferit.dominikzivko.myfinance.ui.ViewModelProviders;
import hr.ferit.dominikzivko.myfinance.ui.dest.TopLevelFragmentDirections;

public class BillsFragment extends Fragment {

    private BillsViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.withBillRepository(this)
                .get(BillsViewModel.class);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentBillsBinding binding = FragmentBillsBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        setupRecycler(binding);
        return binding.getRoot();
    }

    private void setupRecycler(FragmentBillsBinding binding) {
        DividerItemDecoration divider = new DividerItemDecoration(
                binding.rvBills.getContext(), DividerItemDecoration.VERTICAL);
        binding.rvBills.addItemDecoration(divider);

        BillAdapter billAdapter = new BillAdapter(this::navigateToBill);
        binding.rvBills.setAdapter(billAdapter);

        viewModel.getBills().observe(getViewLifecycleOwner(), billAdapter::submitList);
    }

    private void navigateToBill(int billID) {
        TopLevelFragmentDirections.ActionBillListToDetails action =
                TopLevelFragmentDirections.actionBillListToDetails(billID);
        NavHostFragment.findNavController(this).navigate(action);
    }
}