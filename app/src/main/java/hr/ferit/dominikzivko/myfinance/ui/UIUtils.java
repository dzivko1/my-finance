package hr.ferit.dominikzivko.myfinance.ui;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class UIUtils {

    private static final String TAG = UIUtils.class.getSimpleName();

    public static String getDateString(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return DateFormat.getDateInstance().format(calendar.getTime());
    }

    public static Date getDateFromString(String dateString) {
        if (dateString == null || dateString.isEmpty()) return null;

        try {
            return DateFormat.getDateInstance().parse(dateString);
        } catch (ParseException e) {
            Log.e(TAG, "Could not parse Date from String", e);
            return null;
        }
    }
}
