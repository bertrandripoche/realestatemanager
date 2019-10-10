package com.openclassrooms.realestatemanager.dao;

import android.content.ClipData;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.Agent;
import com.openclassrooms.realestatemanager.Flat;

import java.util.List;

@Dao
public interface AgentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createUser(Agent agent);

    @Query("SELECT * FROM Agent WHERE id = :agentId")
    LiveData<Agent> getUser(long agentId);
}
