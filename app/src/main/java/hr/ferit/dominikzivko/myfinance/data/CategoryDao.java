package hr.ferit.dominikzivko.myfinance.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert
    void insert(Category category);

    @Update
    void update(Category category);

    @Query("DELETE FROM Category WHERE id = :id")
    void delete(int id);

    @Query("SELECT COUNT(*) FROM category")
    LiveData<Integer> count();

    @Query("SELECT * FROM Category")
    LiveData<List<Category>> getAll();

    @Transaction
    @Query("SELECT * FROM category WHERE id = :categoryID")
    LiveData<CategoryDetails> getByID(int categoryID);

}
