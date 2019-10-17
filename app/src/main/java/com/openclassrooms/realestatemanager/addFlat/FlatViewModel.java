package com.openclassrooms.realestatemanager.addFlat;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.model.Agent;
import com.openclassrooms.realestatemanager.model.Flat;
import com.openclassrooms.realestatemanager.repositories.AgentDataRepository;
import com.openclassrooms.realestatemanager.repositories.FlatDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class FlatViewModel extends ViewModel {

    // REPOSITORIES
    private final FlatDataRepository mFlatDataSource;
    private final AgentDataRepository mAgentDataSource;
    private final Executor executor;

    // DATA
    @Nullable
    private LiveData<Agent> mCurrentUser;

    public FlatViewModel(FlatDataRepository flatDataSource, AgentDataRepository agentDataSource, Executor executor) {
        this.mFlatDataSource = flatDataSource;
        this.mAgentDataSource = agentDataSource;
        this.executor = executor;
    }

    public void init(long agentId) {
        if (this.mCurrentUser != null) {
            return;
        }
        mCurrentUser = mAgentDataSource.getAgent(agentId);
    }

    // -------------
    // FOR AGENT
    // -------------

    public LiveData<Agent> getAgent(long agentId) { return this.mCurrentUser;  }

    public LiveData<List<Agent>> getAgents() { return this.mAgentDataSource.getAgents();  }


    // -------------
    // FOR FLAT
    // -------------

    public LiveData<List<Flat>> getFlats() {
        return mFlatDataSource.getFlats();
    }

    public LiveData<List<Flat>> getFlatsPerAgent(long agentId) {
        return mFlatDataSource.getFlatsPerAgent(agentId);
    }

    public void createFlat(Flat flat) {
        executor.execute(() -> {
            mFlatDataSource.createFlat(flat);
        });
    }

    public void deleteFlat(long flatId) {
        executor.execute(() -> {
            mFlatDataSource.deleteFlat(flatId);
        });
    }

    public void updateFlat(Flat flat) {
        executor.execute(() -> {
            mFlatDataSource.updateFlat(flat);
        });
    }
}
