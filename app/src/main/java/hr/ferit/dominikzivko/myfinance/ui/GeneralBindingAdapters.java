package hr.ferit.dominikzivko.myfinance.ui;

import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;

import java.text.DateFormat;
import java.util.Date;

public class GeneralBindingAdapters {

    @BindingAdapter("android:text")
    public static void setText(TextView textView, Date date) {
        textView.setText(date != null ? DateFormat.getDateInstance().format(date) : null);
    }

    @BindingAdapter("android:text")
    public static void setText(EditText editText, double doubleVal) {
        editText.setText(doubleVal != 0 ? String.valueOf(doubleVal) : "");
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static double getText(EditText editText) {
        try {
            return Double.parseDouble(editText.getText().toString());
        } catch (Exception e) {
            return 0;
        }
    }
}
