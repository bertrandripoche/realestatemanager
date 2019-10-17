package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.AgentDao;
import com.openclassrooms.realestatemanager.model.Agent;

import java.util.List;

public class AgentDataRepository {
    private final AgentDao mAgentDao;

    public AgentDataRepository(AgentDao agentDao) { this.mAgentDao = agentDao; }

    // --- GET USER ---
    public LiveData<Agent> getAgent(long agentId) { return this.mAgentDao.getAgent(agentId); }
    public LiveData<List<Agent>> getAgents() { return this.mAgentDao.getAgents(); }
}
