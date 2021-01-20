package hr.ferit.dominikzivko.myfinance.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PartyDao {

    @Insert
    void insert(Party party);

    @Update
    void update(Party party);

    @Query("DELETE FROM party WHERE id = :id")
    void delete(int id);

    @Query("SELECT * FROM party")
    LiveData<List<Party>> getAll();

    @Query("SELECT * FROM party WHERE id = :partyID")
    LiveData<Party> getByID(int partyID);
}
