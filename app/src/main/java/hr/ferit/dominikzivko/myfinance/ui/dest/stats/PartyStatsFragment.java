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
import hr.ferit.dominikzivko.myfinance.data.Party;
import hr.ferit.dominikzivko.myfinance.databinding.FragmentPartyStatsBinding;
import hr.ferit.dominikzivko.myfinance.ui.ViewModelProviders;

public class PartyStatsFragment extends Fragment {

    private PartyStatsViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.withBillRepository(this)
                .get(PartyStatsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentPartyStatsBinding binding = FragmentPartyStatsBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        LineChartAdapter adapter = new LineChartAdapter();
        binding.rvPartyStats.setAdapter(adapter);

        LiveData<List<Party>> partiesLiveData = viewModel.getParties();
        BillLineDataProvider dataProvider = new BillLineDataProvider(getViewLifecycleOwner(),
                recipientID -> viewModel.getDayBillValues(recipientID));
        dataProvider.setOnDayBillValuesChanged(dayBillValues -> adapter.notifyDataSetChanged());

        partiesLiveData.observe(getViewLifecycleOwner(), parties -> {
            List<Integer> ids = new ArrayList<>(parties.size());
            List<String> names = new ArrayList<>(parties.size());
            for (Party party : parties) {
                ids.add(party.getId());
                names.add(party.getName());
            }
            List<LineData> data = dataProvider.constructData(ids, names);
            adapter.submitList(data);
        });

        return binding.getRoot();
    }

}