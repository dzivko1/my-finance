package hr.ferit.dominikzivko.myfinance.data;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BillLineDataProvider {

    private final LifecycleOwner lifecycleOwner;
    private final DayBillValuesProvider dayBillValuesProvider;
    private DayBillValuesListener dayBillValuesListener;

    private Observer<List<DayBillValue>> observer;

    public BillLineDataProvider(LifecycleOwner lifecycleOwner, DayBillValuesProvider dayBillValuesProvider) {
        this.lifecycleOwner = lifecycleOwner;
        this.dayBillValuesProvider = dayBillValuesProvider;
    }

    public void setOnDayBillValuesChanged(DayBillValuesListener dayBillValuesListener) {
        this.dayBillValuesListener = dayBillValuesListener;
    }

    public List<LineData> constructData(List<Integer> groupingItemIDs, List<String> labels) {
        List<LineData> groupedData = new ArrayList<>();

        for (int i = 0; i < groupingItemIDs.size(); i++) {
            int id = groupingItemIDs.get(i);
            String label = labels.get(i);

            // if we send null or an empty list, chart doesn't update automatically when data is added... *in comes Entry(0,0)*
            LineDataSet dataSet = new LineDataSet(new ArrayList<Entry>(Arrays.asList(new Entry(0, 0))), label);
            LineData data = new LineData(dataSet);
            groupedData.add(data);

            LiveData<List<DayBillValue>> dayBillValuesLiveData = dayBillValuesProvider.getDayBillValues(id);
            if (observer != null)
                dayBillValuesLiveData.removeObserver(observer);

            observer = dayBillValues -> {
                List<Entry> values = makeDataEntries(dayBillValues);
                dataSet.setValues(values);
                notifyDayBillValuesChanged(dayBillValues);
            };
            dayBillValuesLiveData.observe(lifecycleOwner, observer);
        }
        return groupedData;
    }

    private List<Entry> makeDataEntries(List<DayBillValue> dayBillValues) {
        List<Entry> data = new ArrayList<>();
        for (int i = 0; i < dayBillValues.size(); i++) {
            DayBillValue element = dayBillValues.get(i);
            data.add(new Entry(i, (float) element.getValue()));
        }
        return data;
    }

    private void notifyDayBillValuesChanged(List<DayBillValue> dayBillValues) {
        dayBillValuesListener.onDayBillValuesChanged(dayBillValues);
    }

    public interface DayBillValuesProvider {
        LiveData<List<DayBillValue>> getDayBillValues(int groupingItemID);
    }

    public interface DayBillValuesListener {
        void onDayBillValuesChanged(List<DayBillValue> dayBillValues);
    }
}
