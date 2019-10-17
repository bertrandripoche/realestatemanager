package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.model.Agent;

import java.util.List;

@Dao
public interface AgentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createAgent(Agent agent);

    @Query("SELECT * FROM Agent WHERE id = :agentId")
    LiveData<Agent> getAgent(long agentId);

    @Query("SELECT * FROM Agent")
    LiveData<List<Agent>> getAgents();
}
