package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.model.Pic;

import java.util.List;

@Dao
public interface PicDao {
    @Query("SELECT * FROM Pic WHERE flatId = :flatId LIMIT 0,1")
    LiveData<Pic> getOnePicFromFlat(long flatId);

    @Query("SELECT * FROM Pic WHERE flatId = :flatId")
    LiveData<List<Pic>> getPicsFromFlat(long flatId);

    @Query("SELECT * FROM Pic WHERE id = :PicId")
    LiveData<Pic> getPicFromId(long PicId);

    @Insert
    long insertPic(Pic Pic);

    @Update
    int updatePic(Pic Pic);

    @Query("DELETE FROM Pic WHERE id = :PicId")
    int deletePic(long PicId);

    @Query("DELETE FROM Pic WHERE flatId = :flatId")
    int deletePicFromFlat(long flatId);
}
