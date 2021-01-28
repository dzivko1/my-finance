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

    @Query("WITH subtree AS (SELECT c.id, c.parentCategoryID FROM category c WHERE c.id = :categoryID " +
            "UNION ALL SELECT cc.id, cc.parentCategoryID FROM category cc JOIN subtree on cc.parentCategoryID = subtree.id) " +
            "SELECT B.date, B.value FROM bill B JOIN subtree C ON B.categoryID = C.id")
    LiveData<List<DayBillValue>> getDayValuesForCategory(int categoryID);

    @Transaction
    @Query("SELECT * FROM bill WHERE id = :billID")
    LiveData<BillDetails> getByID(int billID);

    @Query("SELECT SUM(value) FROM bill")
    LiveData<Double> summedValue();

    // converting 'date' from timestamp to date with DATE(date, 'unixepoch') doesn't seem to work, so we're converting everything to timestamps
    @Query("SELECT SUM(value) FROM bill WHERE date BETWEEN STRFTIME('%s', 'now', 'start of month')*1000 AND STRFTIME('%s', 'now', 'start of month', '+1 month')*1000")
    LiveData<Double> summedValueThisMonth();

    @Query("WITH subtree AS (SELECT c.id, c.parentCategoryID, c.name, c.color FROM category c WHERE c.id = :categoryID " +
            "UNION ALL SELECT cc.id, cc.parentCategoryID, (SELECT name FROM category WHERE id = :categoryID), (SELECT color FROM category WHERE id = :categoryID) " +
            "FROM category cc join subtree on cc.parentCategoryID = subtree.id) " +
            "SELECT c.name AS categoryName, c.color AS categoryColor, SUM(b.value) AS totalValue FROM bill b JOIN subtree c ON b.categoryID = c.id GROUP BY c.name, c.color")
    LiveData<CategoryTotal> totalByCategory(int categoryID);

    @Query("WITH children AS (SELECT c.id, c.parentCategoryID, c.name, c.color FROM category c WHERE c.parentCategoryID = :categoryID), " +
            "subtrees AS (SELECT ch.id, ch.name, ch.color, c.id as cid, c.parentCategoryID FROM children ch join category c on c.id = ch.id " +
            "UNION ALL SELECT s.id, s.name, s.color, cc.id AS ccid, cc.parentCategoryID FROM subtrees s join category cc on cc.parentCategoryID = s.cid) " +
            "SELECT s.name AS categoryName, s.color AS categoryColor, SUM(b.value) AS totalValue FROM subtrees s join bill b on b.categoryID = s.cid GROUP BY s.id, s.name, s.color")
    LiveData<List<CategoryTotal>> totalBySubcategories(int categoryID);

}
