package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.PicDao;
import com.openclassrooms.realestatemanager.model.Pic;

import java.util.List;

public class PicDataRepository {
    private final PicDao mPicDao;

    public PicDataRepository(PicDao picDao) { this.mPicDao = picDao; }

    // --- GET ---
    public LiveData<List<Pic>> getOnePicFromFlat(long flatId){ return this.mPicDao.getOnePicFromFlat(flatId); }
    public LiveData<List<Pic>> getPicsFromFlat(long flatId){ return this.mPicDao.getPicsFromFlat(flatId); }
    public LiveData<Pic> getPicFromId(long flatId){ return this.mPicDao.getPicFromId(flatId); }

    // --- CREATE ---
    public void createPic(Pic pic){ mPicDao.insertPic(pic); }

    // --- DELETE ---
    public void deletePic(long picId){ mPicDao.deletePic(picId); }

    // --- UPDATE ---
    public void updatePic(Pic pic){ mPicDao.updatePic(pic); }
}
