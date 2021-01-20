package hr.ferit.dominikzivko.myfinance.ui.dest.parties;

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
import hr.ferit.dominikzivko.myfinance.databinding.FragmentPartyDetailsBinding;
import hr.ferit.dominikzivko.myfinance.ui.ViewModelProviders;
import hr.ferit.dominikzivko.myfinance.ui.dest.MainActivity;

public class PartyDetailsFragment extends Fragment {

    private PartyDetailsViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        viewModel = ViewModelProviders.withBillRepository(this)
                .get(PartyDetailsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentPartyDetailsBinding binding = FragmentPartyDetailsBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int partyID = PartyDetailsFragmentArgs.fromBundle(requireArguments()).getPartyID();
        viewModel.setParty(partyID);
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity activity = (MainActivity) requireActivity();
        activity.setFabAction(v -> activity.startNewParty());
        activity.setFabIcon(R.drawable.ic_baseline_party_add_24);
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
                editParty();
                return true;
            case R.id.action_details_delete:
                deleteParty();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editParty() {
        ((MainActivity) requireActivity()).editParty(viewModel.getParty().getValue());
    }

    private void deleteParty() {
        viewModel.deleteCurrentParty();
        navigateUp();
    }

    private void navigateUp() {
        NavHostFragment.findNavController(this).navigateUp();
    }
}