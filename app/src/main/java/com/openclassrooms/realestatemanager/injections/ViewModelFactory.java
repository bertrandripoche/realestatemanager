package com.openclassrooms.realestatemanager.injections;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.addFlat.FlatViewModel;
import com.openclassrooms.realestatemanager.repositories.AgentDataRepository;
import com.openclassrooms.realestatemanager.repositories.FlatDataRepository;
import com.openclassrooms.realestatemanager.repositories.PicDataRepository;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final FlatDataRepository mFlatDataSource;
    private final AgentDataRepository mAgentDataSource;
    private final PicDataRepository mPicDataSource;
    private final Executor executor;

    public ViewModelFactory(FlatDataRepository flatDataSource, AgentDataRepository agentDataSource, PicDataRepository picDataSource, Executor executor) {
        this.mFlatDataSource = flatDataSource;
        this.mAgentDataSource = agentDataSource;
        this.mPicDataSource = picDataSource;
        this.executor = executor;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FlatViewModel.class)) {
            return (T) new FlatViewModel(mFlatDataSource, mAgentDataSource, mPicDataSource, executor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

