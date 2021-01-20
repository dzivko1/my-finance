package hr.ferit.dominikzivko.myfinance.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(
        entities = {Bill.class, Category.class, Party.class},
        version = 1
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final Object lock = new Object();
    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (lock) {
                instance = buildDatabase(context);
            }
        }
        return instance;
    }

    private static AppDatabase buildDatabase(Context context) {
        return instance = Room.databaseBuilder(
                context.getApplicationContext(), AppDatabase.class, "app-database.db")
                .build();
    }

    public abstract BillDao billDao();

    public abstract CategoryDao categoryDao();

    public abstract PartyDao partyDao();
}
