package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.model.Flat;

import java.util.List;

@Dao
public interface FlatDao {
    @Query("SELECT * FROM Flat WHERE agentId = :agentId")
    LiveData<List<Flat>> getFlats(long agentId);

    @Insert
    long insertFlat(Flat flat);

    @Update
    int updateFlat(Flat flat);

    @Query("DELETE FROM Flat WHERE id = :flatId")
    int deleteFlat(long flatId);
}
