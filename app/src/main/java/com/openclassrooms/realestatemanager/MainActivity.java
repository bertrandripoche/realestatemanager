package com.openclassrooms.realestatemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.facebook.stetho.Stetho;

public class MainActivity extends BaseActivity {

    FlatDetailFragment mFlatDetailFragment;
    FlatListFragment mFlatListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        configureAndShowFlatListFragment();

        mFlatId = getFlatId();

        if(mFlatId != null && mFlatId != 0 && findViewById(R.id.container_fragment_flat_detail) != null)
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
        } else if (mFlatId != null && mFlatId != 0) {
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
       if(mFlatId != null && findViewById(R.id.container_fragment_flat_detail) != null) displayEditBtn();
       else hideEditBtn();
       return true;
    }

    protected Long getFlatId() {
        Long mFlatId = null;
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
        if (i != null && i.getExtras().getInt(SELECTEDFLAT) != -1) {
            selectedFlat = i.getExtras().getInt(SELECTEDFLAT);
        }
        return selectedFlat;
    }

}
