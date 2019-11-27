package com.openclassrooms.realestatemanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;

import com.facebook.stetho.Stetho;

public class MainActivity extends BaseActivity {

    FlatDetailFragment mFlatDetailFragment;
    FlatListFragment mFlatListFragment;
    private static final String CHANNEL_ID = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        createNotificationChannel();

        mFlatId = getFlatId();
        checkPreviousFlatSelection();
        configureAndShowFlatListFragment();
    }

    private void checkPreviousFlatSelection() {
        if(mFlatId != null && mFlatId >= 0 && findViewById(R.id.container_fragment_flat_detail) != null)
        {
            Bundle args = new Bundle();
            mSelectedFlat = getSelectedFlat();
            if (mFlatId != null) args.putLong(FLATID, mFlatId);
            if (mSelectedFlat != -1) args.putInt(SELECTEDFLAT, mSelectedFlat);

            mFlatDetailFragment = new FlatDetailFragment();
            mFlatDetailFragment.setArguments(args);
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_fragment_flat_detail, mFlatDetailFragment, "FlatDetail")
                    .commit();
            displayEditBtn();
        } else if (mFlatId != null && mFlatId >= 0) {
            Bundle args = new Bundle();
            args.putLong(FLATID, mFlatId);
            Intent intent = new Intent(this, FlatDetailActivity.class);
            intent.putExtras(args);
            startActivity(intent);
        }
    }

    private void configureAndShowFlatListFragment() {
        mFlatListFragment = (FlatListFragment) getSupportFragmentManager().findFragmentById(R.id.container_fragment_flat_list);

        if (mFlatListFragment == null) {
            mFlatListFragment = new FlatListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_fragment_flat_list, mFlatListFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       super.onCreateOptionsMenu(menu);
       if(mFlatId != null && mFlatId > 0 &&findViewById(R.id.container_fragment_flat_detail) != null) displayEditBtn();
       else hideEditBtn();
       return true;
    }

    protected Long getFlatId() {
        Long mFlatId = -1L;
        final String FLATID = "flatId";
        Intent i = getIntent();
        if (i != null && i.getExtras() != null) {
            mFlatId = i.getExtras().getLong(FLATID);
        }
        return mFlatId;
    }

    protected int getSelectedFlat() {
        int selectedFlat = -1;
        Intent i = getIntent();
        if (i != null && i.getExtras() != null && i.getExtras().getInt(SELECTEDFLAT) != -1) {
            selectedFlat = i.getExtras().getInt(SELECTEDFLAT);
        }
        return selectedFlat;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
