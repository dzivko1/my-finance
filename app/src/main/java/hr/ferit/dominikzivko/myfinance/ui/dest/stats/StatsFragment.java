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

    private PieChartAdapter adapter;

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
        BarDataSet dataSet = new BarDataSet(new ArrayList<BarEntry>(Arrays.asList(new BarEntry(0, 0))), getResources().getString(R.string.total_by_category));
        BarData data = new BarData(dataSet);
        binding.chartStatsTotalByCategoryBar.setData(data);
        binding.chartStatsTotalByCategoryBar.setAutoScaleMinMaxEnabled(true);

        LiveData<List<CategoryDetails>> categoriesLiveData = viewModel.getCategories();
        categoriesLiveData.observe(getViewLifecycleOwner(), categories -> {

            dataSet.clear();
            List<Integer> colors = new ArrayList<>(categories.size());
            List<String> labels = new ArrayList<>(categories.size());

            int i = 0;
            for (CategoryDetails cd : categories) {
                if (cd.getCategory().getId() == Category.getRootCategory().getId())
                    continue;

                BarEntry entry = new BarEntry(i, 0);
                dataSet.addEntry(entry);
                colors.add(new Integer(0));
                labels.add("");

                LiveData<CategoryTotal> categoryTotalLiveData = viewModel.getTotalByCategory(cd.getCategory().getId());
                ObserverCaretaker<CategoryTotal> observerCaretaker = new ObserverCaretaker<>(getViewLifecycleOwner());
                int finalI = i;
                observerCaretaker.setObserver(categoryTotalLiveData, categoryTotal -> {
                    entry.setY((float) categoryTotal.getTotalValue());
                    colors.set(finalI, categoryTotal.getCategoryColor());
                    labels.set(finalI, categoryTotal.getCategoryName());

                    binding.chartStatsTotalByCategoryBar.notifyDataSetChanged();
                    binding.chartStatsTotalByCategoryBar.invalidate();
                });

                i++;
            }

            dataSet.setColors(colors);
            binding.chartStatsTotalByCategoryBar.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
            binding.chartStatsTotalByCategoryBar.notifyDataSetChanged();
            binding.chartStatsTotalByCategoryBar.invalidate();
        });
    }

    private void updateTotalByCategoryChart(List<CategoryTotal> categoryTotals, BarDataSet barDataSet, FragmentStatsBinding binding) {
        List<BarEntry> barEntries = new ArrayList<>(categoryTotals.size());
        List<String> labels = new ArrayList<>(categoryTotals.size());
        List<Integer> colors = new ArrayList<>(categoryTotals.size());

        int i = 0;
        for (CategoryTotal ct : categoryTotals) {
            barEntries.add(new BarEntry(i, (float) ct.getTotalValue()));
            labels.add(ct.getCategoryName());
            colors.add(ct.getCategoryColor());
            i++;
        }

        barDataSet.setValues(barEntries);
        barDataSet.setColors(colors);
        binding.chartStatsTotalByCategoryBar.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        binding.chartStatsTotalByCategoryBar.notifyDataSetChanged();
        binding.chartStatsTotalByCategoryBar.invalidate();
    }

    private void setupCategoryPies(FragmentStatsBinding binding) {
        adapter = new PieChartAdapter();
        binding.rvCategoryPies.setAdapter(adapter);

        LiveData<List<CategoryDetails>> categoriesLiveData = viewModel.getCategories();
        categoriesLiveData.observe(getViewLifecycleOwner(), categories -> {
            List<PieData> data = constructCategoryPiesData(categories);
            adapter.submitList(data);
        });
    }

    private List<PieData> constructCategoryPiesData(List<CategoryDetails> categories) {
        List<PieData> dataByCategory = new ArrayList<>(categories.size());
        for (CategoryDetails cd : categories) {
            dataByCategory.add(makeCategoryPieData(cd));
        }
        return dataByCategory;
    }

    private PieData makeCategoryPieData(CategoryDetails categoryDetails) {
        List<PieEntry> initialValues = new ArrayList<>();
        PieDataSet dataSet = new PieDataSet(initialValues, categoryDetails.getCategory().getName());

        LiveData<List<CategoryTotal>> categoryPiesLiveData = viewModel.getTotalBySubcategories(categoryDetails.getCategory().getId());
        ObserverCaretaker<List<CategoryTotal>> observerCaretaker = new ObserverCaretaker<>(getViewLifecycleOwner());

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
                adapter.notifyDataSetChanged();
            }
        });

        return new PieData(dataSet);
    }

    private void navigateToCategoryStats() {
        NavDirections action = TopLevelFragmentDirections.actionStatsToCategoryStats();
        NavHostFragment.findNavController(this).navigate(action);
    }

    private void navigateToRecipientStats() {

    }

}