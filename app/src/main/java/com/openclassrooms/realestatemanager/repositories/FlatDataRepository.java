package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.openclassrooms.realestatemanager.database.dao.FlatDao;
import com.openclassrooms.realestatemanager.model.Flat;

import java.util.List;

public class FlatDataRepository {
    private final FlatDao mFlatDao;

    public FlatDataRepository(FlatDao flatDao) { this.mFlatDao = flatDao; }

    // --- GET ---
    public LiveData<List<Flat>> getFlats(){ return this.mFlatDao.getFlats(); }
    public LiveData<List<Flat>> getFlatsPerAgent(long agentId){ return this.mFlatDao.getFlatsPerAgent(agentId); }
    public LiveData<List<Flat>> getFlatsFromQuery(SupportSQLiteQuery query){ return this.mFlatDao.getFlatsFromQuery(query); }
    public LiveData<Flat> getFlatFromId(long flatId){ return this.mFlatDao.getFlatFromId(flatId); }
    public LiveData<Flat> getFlatFromDescription(String description){ return this.mFlatDao.getFlatFromDescription(description); }
    public LiveData<Integer> getLastFlatId(){ return this.mFlatDao.getLastFlatId(); }

    // --- CREATE ---
    public long createFlat(Flat flat){return mFlatDao.insertFlat(flat);}

    // --- DELETE ---
    public void deleteFlat(long flatId){ mFlatDao.deleteFlat(flatId); }

    // --- UPDATE ---
    public long updateFlat(Flat flat){return mFlatDao.updateFlat(flat); }
    public void updateSoldFlat(long flatId, String soldDate){ mFlatDao.updateSold(flatId, soldDate); }
    public void updateNotSoldFlat(long flatId){ mFlatDao.updateNotSold(flatId); }

}
