package hr.ferit.dominikzivko.myfinance.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BillDao {
    @Insert
    void insert(Bill bill);

    @Update
    void update(Bill bill);

    @Delete
    void delete(Bill bill);

    @Query("DELETE FROM bill WHERE id = :id")
    void delete(int id);

    @Query("SELECT COUNT(*) FROM bill")
    LiveData<Integer> count();

    @Transaction
    @Query("SELECT * FROM bill")
    LiveData<List<BillDetails>> getAll();

    @Transaction
    @Query("SELECT * FROM bill WHERE id = :billID")
    LiveData<BillDetails> getByID(int billID);

    @Query("SELECT SUM(value) FROM bill")
    LiveData<Double> summedValue();

    // converting 'date' from timestamp to date with DATE(date, 'unixepoch') doesn't seem to work, so we're converting everything to timestamps
    @Query("SELECT SUM(value) FROM bill WHERE date BETWEEN STRFTIME('%s', 'now', 'start of month')*1000 AND STRFTIME('%s', 'now', 'start of month', '+1 month')*1000")
    LiveData<Double> summedValueThisMonth();

    @Query("SELECT category.name AS categoryName, SUM(bill.value) AS totalValue, category.color AS categoryColor " +
            "FROM bill INNER JOIN category ON bill.categoryID == category.id GROUP BY category.name ")
    LiveData<List<CategoryTotal>> totalByCategory();

}
