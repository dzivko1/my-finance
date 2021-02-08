package hr.ferit.dominikzivko.myfinance.ui.dest.parties;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;

import org.jetbrains.annotations.NotNull;

import hr.ferit.dominikzivko.myfinance.R;
import hr.ferit.dominikzivko.myfinance.databinding.FragmentPartiesBinding;
import hr.ferit.dominikzivko.myfinance.ui.ViewModelProviders;
import hr.ferit.dominikzivko.myfinance.ui.dest.MainActivity;

public class PartiesFragment extends Fragment {

    private PartiesViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.withBillRepository(this)
                .get(PartiesViewModel.class);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentPartiesBinding binding = FragmentPartiesBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        setupRecycler(binding);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity activity = (MainActivity) requireActivity();
        activity.setFabAction(v -> activity.startNewParty());
        activity.setFabIcon(R.drawable.ic_baseline_party_add_24);
    }

    private void setupRecycler(FragmentPartiesBinding binding) {
        DividerItemDecoration divider = new DividerItemDecoration(
                binding.rvParties.getContext(), DividerItemDecoration.VERTICAL);
        binding.rvParties.addItemDecoration(divider);

        PartyAdapter.ItemClickListener itemClickListener =
                PartiesFragmentArgs.fromBundle(requireArguments()).getItemClickListener();
        PartyAdapter partyAdapter = new PartyAdapter(itemClickListener);
        binding.rvParties.setAdapter(partyAdapter);

        viewModel.getParties().observe(getViewLifecycleOwner(), partyAdapter::submitList);
    }
}