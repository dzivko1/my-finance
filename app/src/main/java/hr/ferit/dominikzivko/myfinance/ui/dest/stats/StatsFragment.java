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
import hr.ferit.dominikzivko.myfinance.data.CategoryTotal;
import hr.ferit.dominikzivko.myfinance.databinding.FragmentStatsBinding;
import hr.ferit.dominikzivko.myfinance.ui.ViewModelProviders;
import hr.ferit.dominikzivko.myfinance.ui.dest.TopLevelFragmentDirections;

public class StatsFragment extends Fragment {

    private StatsViewModel viewModel;

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

        setupTotalByCategoryCharts(binding);

        binding.btnStatsByCategory.setOnClickListener(v -> navigateToCategoryStats());
        binding.btnStatsByRecipient.setOnClickListener(v -> navigateToRecipientStats());

        return binding.getRoot();
    }

    private void setupTotalByCategoryCharts(FragmentStatsBinding binding) {
        BarDataSet barDataSet = setupTotalByCategoryBarChart(binding);
        PieDataSet pieDataSet = setupTotalByCategoryPieChart(binding);

        LiveData<List<CategoryTotal>> categoryTotalsLiveData = viewModel.getTotalByCategory();
        categoryTotalsLiveData.observe(getViewLifecycleOwner(), categoryTotals ->
                updateChartData(categoryTotals, barDataSet, pieDataSet, binding));
    }

    private BarDataSet setupTotalByCategoryBarChart(FragmentStatsBinding binding) {
        BarDataSet dataSet = new BarDataSet(new ArrayList<BarEntry>(Arrays.asList(new BarEntry(0, 0))), getResources().getString(R.string.total_by_category));
        BarData data = new BarData(dataSet);
        binding.chartStatsTotalByCategoryBar.setData(data);
        binding.chartStatsTotalByCategoryBar.setAutoScaleMinMaxEnabled(true);
        return dataSet;
    }

    private PieDataSet setupTotalByCategoryPieChart(FragmentStatsBinding binding) {
        PieDataSet dataSet = new PieDataSet(new ArrayList<>(Arrays.asList(new PieEntry(0))), getResources().getString(R.string.total_by_category));
        PieData data = new PieData(dataSet);
        binding.chartStatsTotalByCategoryPie.setData(data);
        return dataSet;
    }

    private void updateChartData(List<CategoryTotal> categoryTotals, BarDataSet barDataSet, PieDataSet pieDataSet, FragmentStatsBinding binding) {
        List<BarEntry> barEntries = new ArrayList<>(categoryTotals.size());
        List<PieEntry> pieEntries = new ArrayList<>(categoryTotals.size());
        List<String> labels = new ArrayList<>(categoryTotals.size());
        List<Integer> colors = new ArrayList<>(categoryTotals.size());

        int i = 0;
        for (CategoryTotal ct : categoryTotals) {
            barEntries.add(new BarEntry(i, (float) ct.getTotalValue()));
            pieEntries.add(new PieEntry((float) ct.getTotalValue(), ct.getCategoryName()));
            labels.add(ct.getCategoryName());
            colors.add(ct.getCategoryColor());
            i++;
        }

        barDataSet.setValues(barEntries);
        barDataSet.setColors(colors);
        binding.chartStatsTotalByCategoryBar.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

        pieDataSet.setValues(pieEntries);
        pieDataSet.setColors(colors);

        binding.chartStatsTotalByCategoryBar.notifyDataSetChanged();
        binding.chartStatsTotalByCategoryPie.notifyDataSetChanged();
        binding.chartStatsTotalByCategoryBar.invalidate();
        binding.chartStatsTotalByCategoryPie.invalidate();
    }

    private void navigateToCategoryStats() {
        NavDirections action = TopLevelFragmentDirections.actionStatsToCategoryStats();
        NavHostFragment.findNavController(this).navigate(action);
    }

    private void navigateToRecipientStats() {

    }

}