package hr.ferit.dominikzivko.myfinance.ui.dest.bills;

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
import hr.ferit.dominikzivko.myfinance.databinding.FragmentBillDetailsBinding;
import hr.ferit.dominikzivko.myfinance.ui.ViewModelProviders;
import hr.ferit.dominikzivko.myfinance.ui.dest.MainActivity;

public class BillDetailsFragment extends Fragment {

    private BillDetailsViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        viewModel = ViewModelProviders.withBillRepository(this)
                .get(BillDetailsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentBillDetailsBinding binding = FragmentBillDetailsBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int billID = BillDetailsFragmentArgs.fromBundle(requireArguments()).getBillID();
        viewModel.setBill(billID);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) requireActivity()).resetFab();
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
                editBill();
                return true;
            case R.id.action_details_delete:
                deleteBill();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editBill() {
        ((MainActivity) requireActivity()).editBill(viewModel.getBillDetails().getValue());
    }

    private void deleteBill() {
        viewModel.deleteCurrentBill();
        navigateUp();
    }

    private void navigateUp() {
        NavHostFragment.findNavController(this).navigateUp();
    }
}