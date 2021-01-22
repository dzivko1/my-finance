package hr.ferit.dominikzivko.myfinance.ui.dest.stats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.List;

import hr.ferit.dominikzivko.myfinance.data.CategoryTotal;
import hr.ferit.dominikzivko.myfinance.databinding.FragmentStatsBinding;
import hr.ferit.dominikzivko.myfinance.ui.ViewModelProviders;

public class StatsFragment extends Fragment {

    private StatsViewModel viewModel;

    private Cartesian chart_totalByCategoryBar;
    private Pie chart_totalByCategoryPie;

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

        // Apparently when charts are in a scroll view we have to call this setActiveAnyChartView
        // any time we want to change a chart in order for them to show correctly
        APIlib.getInstance().setActiveAnyChartView(binding.chartStatsTotalByCategoryBar);
        chart_totalByCategoryBar = AnyChart.bar();
        binding.chartStatsTotalByCategoryBar.setChart(chart_totalByCategoryBar);

        APIlib.getInstance().setActiveAnyChartView(binding.chartStatsTotalByCategoryPie);
        chart_totalByCategoryPie = AnyChart.pie();
        binding.chartStatsTotalByCategoryPie.setChart(chart_totalByCategoryPie);

        LiveData<List<CategoryTotal>> categoryTotals = viewModel.getTotalByCategory();
        categoryTotals.observe(getViewLifecycleOwner(), value -> {
            List<DataEntry> data = convertToDataEntries(value);

            APIlib.getInstance().setActiveAnyChartView(binding.chartStatsTotalByCategoryBar);
            chart_totalByCategoryBar.data(data);

            APIlib.getInstance().setActiveAnyChartView(binding.chartStatsTotalByCategoryPie);
            chart_totalByCategoryPie.data(data);
            setPiePalette(chart_totalByCategoryPie, value);
        });

        return binding.getRoot();
    }

    private List<DataEntry> convertToDataEntries(List<CategoryTotal> categoryTotals) {
        List<DataEntry> data = new ArrayList<>(categoryTotals.size());
        for (CategoryTotal ct : categoryTotals) {
            data.add(new ValueDataEntry(ct.getCategoryName(), ct.getTotalValue()));
        }
        return data;
    }

    private void setPiePalette(Pie pieChart, List<CategoryTotal> categoryTotals) {
        List<String> colors = new ArrayList<>(categoryTotals.size());
        for (CategoryTotal ct : categoryTotals) {
            colors.add("#" + Integer.toHexString(ct.getCategoryColor()).substring(2));
        }
        pieChart.palette(colors.toArray(new String[colors.size()]));
    }

}