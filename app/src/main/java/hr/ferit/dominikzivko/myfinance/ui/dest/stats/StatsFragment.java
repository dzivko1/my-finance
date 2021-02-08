package hr.ferit.dominikzivko.myfinance.ui.dest.stats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import hr.ferit.dominikzivko.myfinance.R;
import hr.ferit.dominikzivko.myfinance.data.Category;
import hr.ferit.dominikzivko.myfinance.data.CategoryDetails;
import hr.ferit.dominikzivko.myfinance.data.CategoryTotal;
import hr.ferit.dominikzivko.myfinance.data.ObserverCaretaker;
import hr.ferit.dominikzivko.myfinance.databinding.FragmentStatsBinding;
import hr.ferit.dominikzivko.myfinance.ui.ViewModelProviders;
import hr.ferit.dominikzivko.myfinance.ui.dest.TopLevelFragmentDirections;

public class StatsFragment extends Fragment {

    private StatsViewModel viewModel;

    private PieChartAdapter categoryPieAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.withBillRepository(this)
                .get(StatsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentStatsBinding binding = FragmentStatsBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);

        setupTotalByCategoryChart(binding);
        setupCategoryPies(binding);

        binding.btnStatsByCategory.setOnClickListener(v -> navigateToCategoryStats());
        binding.btnStatsByRecipient.setOnClickListener(v -> navigateToRecipientStats());

        return binding.getRoot();
    }

    private void setupTotalByCategoryChart(FragmentStatsBinding binding) {
        binding.chartStatsTotalByCategoryBar.getDescription().setEnabled(false);
        binding.chartStatsTotalByCategoryBar.setAutoScaleMinMaxEnabled(true);

        BarDataSet dataSet = new BarDataSet(new ArrayList<>(Arrays.asList(new BarEntry(0, 0))), getResources().getString(R.string.total_by_category));
        BarData data = new BarData(dataSet);
        binding.chartStatsTotalByCategoryBar.setData(data);

        final HashMap<Integer, ObserverCaretaker<CategoryTotal>> observerStore = new HashMap<>();

        LiveData<List<CategoryDetails>> categoriesLiveData = viewModel.getCategories();
        categoriesLiveData.observe(getViewLifecycleOwner(), categories -> {

            List<Integer> ids = new ArrayList<>(categories.size());
            List<Integer> colors = new ArrayList<>(categories.size());
            List<String> labels = new ArrayList<>(categories.size());
            for (CategoryDetails cd : categories) {
                if (cd.getCategory().getId() == Category.getRootCategory().getId())
                    continue;

                ids.add(cd.getCategory().getId());
                colors.add(cd.getCategory().getColor());
                labels.add(cd.getCategory().getName());
            }
            dataSet.setColors(colors);
            binding.chartStatsTotalByCategoryBar.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

            dataSet.clear();

            int i = 0;
            for (int id : ids) {

                BarEntry entry = new BarEntry(i, 0);
                dataSet.addEntry(entry);

                ObserverCaretaker<CategoryTotal> observerCaretaker = observerStore.get(id);
                if (observerCaretaker == null) {
                    observerCaretaker = new ObserverCaretaker<>(getViewLifecycleOwner());
                    observerStore.put(id, observerCaretaker);
                }

                LiveData<CategoryTotal> categoryTotalLiveData = viewModel.getTotalByCategory(id);
                observerCaretaker.setObserver(categoryTotalLiveData, categoryTotal -> {
                    entry.setY(categoryTotal != null ? (float) categoryTotal.getTotalValue() : 0f);
                    binding.chartStatsTotalByCategoryBar.notifyDataSetChanged();
                    binding.chartStatsTotalByCategoryBar.invalidate();
                });

                i++;
            }
        });
    }

    private void setupCategoryPies(FragmentStatsBinding binding) {
        categoryPieAdapter = new PieChartAdapter();
        binding.rvCategoryPies.setAdapter(categoryPieAdapter);

        final HashMap<Integer, ObserverCaretaker<List<CategoryTotal>>> observerStore = new HashMap<>();

        LiveData<List<CategoryDetails>> categoriesLiveData = viewModel.getCategories();
        categoriesLiveData.observe(getViewLifecycleOwner(), categories -> {
            List<PieData> data = constructCategoryPiesData(categories, observerStore);
            categoryPieAdapter.submitList(data);
        });
    }

    private List<PieData> constructCategoryPiesData(List<CategoryDetails> categories, HashMap<Integer, ObserverCaretaker<List<CategoryTotal>>> observerStore) {
        List<PieData> dataByCategory = new ArrayList<>(categories.size());
        for (CategoryDetails cd : categories) {
            dataByCategory.add(makeCategoryPieData(cd, observerStore));
        }
        return dataByCategory;
    }

    private PieData makeCategoryPieData(CategoryDetails categoryDetails, HashMap<Integer, ObserverCaretaker<List<CategoryTotal>>> observerStore) {
        int id = categoryDetails.getCategory().getId();

        List<PieEntry> initialValues = new ArrayList<>();
        PieDataSet dataSet = new PieDataSet(initialValues, categoryDetails.getCategory().getName());

        ObserverCaretaker<List<CategoryTotal>> observerCaretaker = observerStore.get(id);
        if (observerCaretaker == null) {
            observerCaretaker = new ObserverCaretaker<>(getViewLifecycleOwner());
            observerStore.put(id, observerCaretaker);
        }

        LiveData<List<CategoryTotal>> categoryPiesLiveData = viewModel.getTotalBySubcategories(id);
        observerCaretaker.setObserver(categoryPiesLiveData, categoryTotals -> {
            if (categoryTotals.size() > 0) {
                List<PieEntry> values = new ArrayList<>(categoryTotals.size());
                List<Integer> colors = new ArrayList<>(categoryTotals.size());
                for (CategoryTotal ct : categoryTotals) {
                    values.add(new PieEntry((float) ct.getTotalValue(), ct.getCategoryName()));
                    colors.add(ct.getCategoryColor());
                }
                dataSet.setValues(values);
                dataSet.setColors(colors);
                categoryPieAdapter.notifyDataSetChanged();
            }
        });

        return new PieData(dataSet);
    }

    private void navigateToCategoryStats() {
        NavDirections action = TopLevelFragmentDirections.actionStatsToCategoryStats();
        NavHostFragment.findNavController(this).navigate(action);
    }

    private void navigateToRecipientStats() {
        NavDirections action = TopLevelFragmentDirections.actionStatsToPartyStats();
        NavHostFragment.findNavController(this).navigate(action);
    }

}