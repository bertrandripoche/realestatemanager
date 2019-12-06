package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.openclassrooms.realestatemanager.model.Flat;

import java.util.List;

@Dao
public interface FlatDao {
    @Query("SELECT * FROM Flat ORDER BY isSold, id DESC")
    LiveData<List<Flat>> getFlats();

    @Query("SELECT * FROM Flat WHERE agentId = :agentId")
    LiveData<List<Flat>> getFlatsPerAgent(long agentId);

    @Query("SELECT * FROM Flat WHERE id = :flatId")
    LiveData<Flat> getFlatFromId(long flatId);

    @Query("SELECT * FROM Flat WHERE description = :description ORDER BY id DESC LIMIT 0,1")
    LiveData<Flat> getFlatFromDescription(String description);

    @RawQuery(observedEntities = Flat.class)
    LiveData<List<Flat>> getFlatsFromQuery(SupportSQLiteQuery query);

    @Query("SELECT MAX(Id) FROM Flat")
    LiveData<Integer> getLastFlatId();

    @Insert
    long insertFlat(Flat flat);

    @Update
    int updateFlat(Flat flat);

    @Query("UPDATE Flat SET isSold = 1, soldDate = :soldDate WHERE id = :flatId")
    int updateSold(long flatId, String soldDate);

    @Query("UPDATE Flat SET isSold = 0, soldDate = null WHERE id = :flatId")
    int updateNotSold(long flatId);

    @Query("DELETE FROM Flat WHERE id = :flatId")
    int deleteFlat(long flatId);
}
