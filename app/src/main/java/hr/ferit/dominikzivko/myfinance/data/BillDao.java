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

    @Transaction
    @Query("SELECT * FROM bill")
    LiveData<List<BillDetails>> getAll();

    @Transaction
    @Query("SELECT * FROM bill WHERE id = :billID")
    LiveData<BillDetails> getByID(int billID);
}
