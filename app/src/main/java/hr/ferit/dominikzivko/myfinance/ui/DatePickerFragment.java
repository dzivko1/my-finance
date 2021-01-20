package hr.ferit.dominikzivko.myfinance.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    private final DatePickerDialog.OnDateSetListener dateSetListener;
    private int defaultYear;
    private int defaultMonth;
    private int defaultDay;

    public DatePickerFragment(DatePickerDialog.OnDateSetListener dateSetListener) {
        this.dateSetListener = dateSetListener;

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        setDefaultDate(year, month, day);
    }

    public DatePickerFragment(DatePickerDialog.OnDateSetListener dateSetListener, int year, int month, int day) {
        this.dateSetListener = dateSetListener;
        setDefaultDate(year, month, day);
    }

    private void setDefaultDate(int year, int month, int day) {
        this.defaultYear = year;
        this.defaultMonth = month;
        this.defaultDay = day;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new DatePickerDialog(getContext(), dateSetListener, defaultYear, defaultMonth, defaultDay);
    }
}
