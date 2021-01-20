package hr.ferit.dominikzivko.myfinance.data;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converters {
    @TypeConverter
    public static Date TimestampToDate(long timestamp) {
        return new Date(timestamp);
    }

    @TypeConverter
    public static long DateToTimestamp(Date date) {
        return date.getTime();
    }
}
