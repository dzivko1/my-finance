package hr.ferit.dominikzivko.myfinance.ui;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import java.text.DateFormat;
import java.util.Date;

public class GeneralBindingAdapters {

    @BindingAdapter("android:text")
    public static void setText(TextView textView, Date date) {
        textView.setText(date != null ? DateFormat.getDateInstance().format(date) : null);
    }
}
