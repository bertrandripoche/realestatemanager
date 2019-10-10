package com.openclassrooms.realestatemanager.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.Flat;

import java.util.List;

@Dao
public interface FlatDao {
    @Query("SELECT * FROM Flat WHERE agentId = :agentId")
    LiveData<List<Flat>> getFlats(long agentId);

    @Insert
    long insertItem(Flat flat);

    @Update
    int updateItem(Flat flat);

    @Delete
    int deleteItem(Flat flat);
}
