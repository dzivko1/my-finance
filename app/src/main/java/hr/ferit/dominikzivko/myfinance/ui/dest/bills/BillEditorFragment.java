package hr.ferit.dominikzivko.myfinance.ui.dest.bills;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

import hr.ferit.dominikzivko.myfinance.ActionFailedException;
import hr.ferit.dominikzivko.myfinance.R;
import hr.ferit.dominikzivko.myfinance.databinding.FragmentBillEditorBinding;
import hr.ferit.dominikzivko.myfinance.ui.DatePickerFragment;
import hr.ferit.dominikzivko.myfinance.ui.SimpleTextWatcher;
import hr.ferit.dominikzivko.myfinance.ui.UIUtils;
import hr.ferit.dominikzivko.myfinance.ui.ViewModelProviders;
import hr.ferit.dominikzivko.myfinance.ui.dest.MainActivity;
import hr.ferit.dominikzivko.myfinance.ui.dest.categories.CategoryAdapter;
import hr.ferit.dominikzivko.myfinance.ui.dest.parties.PartyAdapter;

public class BillEditorFragment extends Fragment {

    private BillEditorViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.withBillRepository(requireActivity())
                .get(BillEditorViewModel.class);

        // TODO Transitions don't work well with nav-graph's animations. Figure it out!
        /*TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.slide_in_bottom));
        setExitTransition(inflater.inflateTransition(R.transition.slide_out_bottom));*/
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentBillEditorBinding binding = FragmentBillEditorBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);

        binding.etBillEditorDate.setInputType(InputType.TYPE_NULL);
        // Both click and focus change listeners are needed to register taps every time
        // if we want the field to remain focusable
        binding.etBillEditorDate.setOnClickListener(this::showDatePicker);
        // Focus is disabled instead, so no need for second listener
        //binding.etDate.setOnFocusChangeListener((v, hasFocus) -> showDatePicker(v));

        binding.etBillEditorDate.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                viewModel.setDate(UIUtils.getDateFromString(s.toString()));
            }
        });

        binding.btnBillEditorPickCategory.setOnClickListener(v -> navigateToCategoryPicker());
        binding.btnBillEditorPickRecipient.setOnClickListener(v -> navigateToRecipientPicker());

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity activity = (MainActivity) requireActivity();
        activity.setFabAction(v -> acceptEdits());
        activity.setFabIcon(R.drawable.ic_baseline_check_24);
    }

    private void showDatePicker(View editText) {
        DatePickerFragment datePicker = new DatePickerFragment((view, year, month, dayOfMonth) -> {
            String dateString = UIUtils.getDateString(year, month, dayOfMonth);
            ((EditText) editText).setText(dateString);

            Calendar c = Calendar.getInstance();
            c.set(year, month, dayOfMonth);
            viewModel.setDate(c.getTime());
        });
        datePicker.show(getChildFragmentManager(), "datePicker");
    }

    private void acceptEdits() {
        try {
            viewModel.tryApply();
            navigateUp();
        } catch (ActionFailedException e) {
            Snackbar.make(requireView(), getResources().getString(e.getMessageResID()), Snackbar.LENGTH_LONG).show();
        }
    }

    private void navigateToCategoryPicker() {
        String title = getResources().getString(R.string.pick_category);
        CategoryAdapter.ItemClickListener itemClickListener = category -> {
            viewModel.setCategory(category);
            navigateUp();
        };
        BillEditorFragmentDirections.ActionBillEditorToPickCategory action =
                BillEditorFragmentDirections.actionBillEditorToPickCategory(title, itemClickListener);

        NavHostFragment.findNavController(this).navigate(action);
    }

    private void navigateToRecipientPicker() {
        String title = getResources().getString(R.string.pick_recipient);
        PartyAdapter.ItemClickListener itemClickListener = party -> {
            viewModel.setRecipient(party);
            navigateUp();
        };
        BillEditorFragmentDirections.ActionBillEditorToPickRecipient action =
                BillEditorFragmentDirections.actionBillEditorToPickRecipient(title, itemClickListener);

        NavHostFragment.findNavController(this).navigate(action);
    }

    private void navigateUp() {
        NavHostFragment.findNavController(this).navigateUp();
    }

}