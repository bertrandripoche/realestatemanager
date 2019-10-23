package com.openclassrooms.realestatemanager.injections;

import android.content.Context;

import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.repositories.AgentDataRepository;
import com.openclassrooms.realestatemanager.repositories.FlatDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {
    public static FlatDataRepository provideFlatDataSource(Context context) {
        RealEstateManagerDatabase database = RealEstateManagerDatabase.getInstance(context);
        return new FlatDataRepository(database.mFlatDao());
    }

    public static AgentDataRepository provideAgentDataSource(Context context) {
        RealEstateManagerDatabase database = RealEstateManagerDatabase.getInstance(context);
        return new AgentDataRepository(database.mAgentDao());
    }

    public static Executor provideExecutor(){ return Executors.newSingleThreadExecutor(); }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        FlatDataRepository dataSourceFlat = provideFlatDataSource(context);
        AgentDataRepository dataSourceAgent = provideAgentDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourceFlat, dataSourceAgent, executor);
    }
}