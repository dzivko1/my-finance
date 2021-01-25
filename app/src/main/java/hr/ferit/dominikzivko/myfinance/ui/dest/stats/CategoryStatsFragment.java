package hr.ferit.dominikzivko.myfinance.ui.dest.stats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import com.github.mikephil.charting.data.LineData;

import java.util.ArrayList;
import java.util.List;

import hr.ferit.dominikzivko.myfinance.data.BillLineDataProvider;
import hr.ferit.dominikzivko.myfinance.data.CategoryDetails;
import hr.ferit.dominikzivko.myfinance.databinding.FragmentCategoryStatsBinding;
import hr.ferit.dominikzivko.myfinance.ui.ViewModelProviders;

public class CategoryStatsFragment extends Fragment {

    private CategoryStatsViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.withBillRepository(this)
                .get(CategoryStatsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentCategoryStatsBinding binding = FragmentCategoryStatsBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        LineChartAdapter adapter = new LineChartAdapter();
        binding.rvCategoryStats.setAdapter(adapter);

        LiveData<List<CategoryDetails>> categoriesLiveData = viewModel.getCategories();
        BillLineDataProvider dataProvider = new BillLineDataProvider(getViewLifecycleOwner(),
                categoryID -> viewModel.getDayBillValues(categoryID));
        dataProvider.setOnDayBillValuesChanged(dayBillValues -> adapter.notifyDataSetChanged());

        categoriesLiveData.observe(getViewLifecycleOwner(), categories -> {
            List<Integer> ids = new ArrayList<>(categories.size());
            List<String> names = new ArrayList<>(categories.size());
            for (CategoryDetails cd : categories) {
                ids.add(cd.getCategory().getId());
                names.add(cd.getCategory().getName());
            }
            List<LineData> data = dataProvider.constructData(ids, names);
            adapter.submitList(data);
        });

        return binding.getRoot();
    }

}