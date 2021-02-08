package hr.ferit.dominikzivko.myfinance.ui.dest.parties;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;

import hr.ferit.dominikzivko.myfinance.ActionFailedException;
import hr.ferit.dominikzivko.myfinance.R;
import hr.ferit.dominikzivko.myfinance.databinding.FragmentPartyEditorBinding;
import hr.ferit.dominikzivko.myfinance.ui.ViewModelProviders;
import hr.ferit.dominikzivko.myfinance.ui.dest.MainActivity;

public class PartyEditorFragment extends Fragment {

    private PartyEditorViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.withBillRepository(requireActivity())
                .get(PartyEditorViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentPartyEditorBinding binding = FragmentPartyEditorBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity activity = (MainActivity) requireActivity();
        activity.setFabAction(v -> acceptEdits());
        activity.setFabIcon(R.drawable.ic_baseline_check_24);
    }

    private void acceptEdits() {
        try {
            viewModel.tryApply();
            navigateUp();
        } catch (ActionFailedException e) {
            Snackbar.make(requireView(), getResources().getString(e.getMessageResID()), Snackbar.LENGTH_LONG).show();
        }
    }

    private void navigateUp() {
        NavHostFragment.findNavController(this).navigateUp();
    }
}