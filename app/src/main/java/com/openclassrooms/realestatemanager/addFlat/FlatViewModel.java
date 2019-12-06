package com.openclassrooms.realestatemanager.addFlat;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.openclassrooms.realestatemanager.model.Agent;
import com.openclassrooms.realestatemanager.model.Flat;
import com.openclassrooms.realestatemanager.model.Pic;
import com.openclassrooms.realestatemanager.notifications.NotificationService;
import com.openclassrooms.realestatemanager.repositories.AgentDataRepository;
import com.openclassrooms.realestatemanager.repositories.FlatDataRepository;
import com.openclassrooms.realestatemanager.repositories.PicDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class FlatViewModel extends ViewModel {

    // REPOSITORIES
    private final FlatDataRepository mFlatDataSource;
    private final AgentDataRepository mAgentDataSource;
    private final PicDataRepository mPicDataSource;
    private final Executor executor;

    private long flatId;

    // DATA
    @Nullable
    private LiveData<Agent> mCurrentUser;

    public FlatViewModel(FlatDataRepository flatDataSource, AgentDataRepository agentDataSource, PicDataRepository picDataSource, Executor executor) {
        this.mFlatDataSource = flatDataSource;
        this.mAgentDataSource = agentDataSource;
        this.mPicDataSource = picDataSource;
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

    public LiveData<Integer> getLastFlatId() {
        return mFlatDataSource.getLastFlatId();
    }

    public LiveData<List<Flat>> getFlats() {
        return mFlatDataSource.getFlats();
    }

    public LiveData<List<Flat>> getFlatsFromQuery(SupportSQLiteQuery query) {return mFlatDataSource.getFlatsFromQuery(query);}

    public LiveData<List<Flat>> getFlatsPerAgent(long agentId) {return mFlatDataSource.getFlatsPerAgent(agentId);}

    public LiveData<Flat> getFlatFromDescription(String description) {
        return mFlatDataSource.getFlatFromDescription(description);
    }

    public LiveData<Flat> getFlatFromId(long flatId) {
        return mFlatDataSource.getFlatFromId(flatId);
    }

    public long createFlat(Context context, Flat flat, List<Pic> flatPicList, boolean isTablet) {
        executor.execute(() -> {
            long flatId = mFlatDataSource.createFlat(flat);

            for (Pic pic : flatPicList) {
                pic.setFlatId((int)flatId);
                mPicDataSource.createPic(pic);
            }
            NotificationService notificationService = new NotificationService();
            notificationService.createNotification(context, flat.getSummary(), flatId, isTablet);

        });
        return flatId;
    }

    public void deleteFlat(long flatId) {
        executor.execute(() -> {
            mFlatDataSource.deleteFlat(flatId);
        });
    }

    public void updateFlat(Flat flat) {
        executor.execute(() -> {
            long flatId = mFlatDataSource.updateFlat(flat);
        });
    }

    public void updateFlat(Flat flat, List<Pic> flatPicList) {
        executor.execute(() -> {
            mPicDataSource.deletePicFromFlat(flat.getId());
            if (flatPicList.size() == 0) flat.setPicPath("");
            else {
                boolean isFirst = true;
                for (Pic pic : flatPicList) {
                    if (isFirst) flat.setPicPath(pic.getPicPath());
                    pic.setFlatId((int) flat.getId());
                    mPicDataSource.createPic(pic);
                    isFirst = false;
                }
            }
            mFlatDataSource.updateFlat(flat);
        });
    }

    public void updateSoldFlat(long flatId, String soldDate) {
        executor.execute(() -> {
            mFlatDataSource.updateSoldFlat(flatId, soldDate);
        });
    }

    public void updateNotSoldFlat(long flatId) {
        executor.execute(() -> {
            mFlatDataSource.updateNotSoldFlat(flatId);
        });
    }

    // -------------
    // FOR PIC
    // -------------

    public LiveData<Pic> getOnePicFromFlat(long flatId) {
        return mPicDataSource.getOnePicFromFlat(flatId);
    }

    public LiveData<List<Pic>> getPicsFromFlat(long flatId) {
        return mPicDataSource.getPicsFromFlat(flatId);
    }

    public LiveData<List<Pic>> getPicFromId(long picId) {
        return mPicDataSource.getPicsFromFlat(picId);
    }

    public void createPic(Pic pic) {
        executor.execute(() -> {
            mPicDataSource.createPic(pic);
        });
    }

    public void deletePic(long picId) {
        executor.execute(() -> {
            mPicDataSource.deletePic(picId);
        });
    }

    public void deletePicFromFlat(long flatId) {
        executor.execute(() -> {
            mPicDataSource.deletePicFromFlat(flatId);
        });
    }

    public void updatePic(Pic pic) {
        executor.execute(() -> {
            mPicDataSource.updatePic(pic);
        });
    }

}
