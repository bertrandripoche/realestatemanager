package com.openclassrooms.realestatemanager;

import android.os.Bundle;

import com.facebook.stetho.Stetho;

public class MainActivity extends BaseActivity {

    FlatDetailFragment mFlatDetailFragment;
    FlatListFragment mFlatListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        configureAndShowFlatListFragment();
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
}
