package hr.ferit.dominikzivko.myfinance.ui.dest;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import hr.ferit.dominikzivko.myfinance.R;
import hr.ferit.dominikzivko.myfinance.databinding.FragmentTopLevelBinding;

public class TopLevelFragment extends Fragment {

    private String[] mainTabNames;
    private int[] mainTabIcons;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupTabRepresentation();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentTopLevelBinding binding = FragmentTopLevelBinding.inflate(inflater, container, false);
        setupViewPager(binding);
        return binding.getRoot();
    }

    private void setupTabRepresentation() {
        mainTabNames = getResources().getStringArray(R.array.main_tab_names);
        TypedArray taTabIcons = getResources().obtainTypedArray(R.array.main_tab_icons);
        mainTabIcons = new int[taTabIcons.length()];
        for (int i = 0; i < taTabIcons.length(); i++) {
            mainTabIcons[i] = taTabIcons.getResourceId(i, 0);
        }
        taTabIcons.recycle();
    }

    private void setupViewPager(FragmentTopLevelBinding binding) {
        binding.mainViewPager.setAdapter(new SectionsPagerAdapter(this));
        new TabLayoutMediator(binding.mainTabLayout, binding.mainViewPager,
                (tab, position) -> {
                    tab.setText(mainTabNames[position]);
                    tab.setIcon(mainTabIcons[position]);
                }
        ).attach();
    }
}