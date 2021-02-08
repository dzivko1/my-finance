package hr.ferit.dominikzivko.myfinance.ui;

import java.text.DateFormat;
import java.util.Calendar;

public class UIUtils {

    public static String getDateString(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return DateFormat.getDateInstance().format(calendar.getTime());
    }
}
