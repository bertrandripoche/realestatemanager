package com.openclassrooms.realestatemanager.ui.view;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.stetho.Stetho;
import com.openclassrooms.realestatemanager.R;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    private static final String LIST = "List";
    @BindView(R.id.btn_back_to_list) Button mBackToListBtn;

    private FlatDetailFragment mFlatDetailFragment;
    private FlatListFragment mFlatListFragment;
    private static final String CHANNEL_ID = "1";
    private static final String QUERY = "Query";
    protected String mQuery = "";
    private boolean mIsFullListRequestedAfterSearchCleaning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        createNotificationChannel();

        mQuery = (mIsFullListRequestedAfterSearchCleaning) ? "" : getQuery();
        mFlatId = (mQuery == null || mQuery == "") ? getFlatId() : null;
        if (mIsFullListRequestedAfterSearchCleaning) mIsFullListRequestedAfterSearchCleaning = false;

        checkPreviousFlatSelection();
        configureFragment(new FlatListFragment(), R.id.container_fragment_flat_list, LIST);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       super.onCreateOptionsMenu(menu);
       if(mFlatId != null && mFlatId > 0 &&findViewById(R.id.container_fragment_flat_detail) != null) displayEditBtn();
       else hideEditBtn();
       return true;
    }

    /**
     * Detail action when "Back to List" button clicked
     */
    @OnClick(R.id.btn_back_to_list)
    public void onClickBackToListButton() {
        mIsFullListRequestedAfterSearchCleaning = true;
        mQuery = "";
        hideBackToListBtn();

        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }

    /**
     * Check if there is a previous flat selected
     */
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

    /**
     * Get the flatid from bundle
     * @return a Long representing the flatId
     */
    protected Long getFlatId() {
        Long mFlatId = -1L;
        final String FLATID = "flatId";
        Intent i = getIntent();
        if (i != null && i.getExtras() != null) {
            mFlatId = i.getExtras().getLong(FLATID);
        }
        return mFlatId;
    }

    /**
     * Get query from bundle
     * @return a string
     */
    protected String getQuery() {
        String query = "";
        Intent i = getIntent();
        if (i != null && i.getExtras() != null) {
            query = i.getExtras().getString(QUERY);
        }
        return query;
    }

    /**
     * Get the selected flat from bundle
     * @return an int
     */
    protected int getSelectedFlat() {
        int selectedFlat = -1;
        Intent i = getIntent();
        if (i != null && i.getExtras() != null && i.getExtras().getInt(SELECTEDFLAT) != -1) {
            selectedFlat = i.getExtras().getInt(SELECTEDFLAT);
        }
        return selectedFlat;
    }

    /**
     * Creating the notification channel is required to send notifications
     */
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

    /**
     * Hide the list button active
     */
    protected void hideBackToListBtn() {if (mBackToListBtn != null) mBackToListBtn.setVisibility(View.GONE);}
    /**
     * Display the list button active
     */
    protected void displayBackToListBtn() {if (mBackToListBtn != null) mBackToListBtn.setVisibility(View.VISIBLE);}
}
