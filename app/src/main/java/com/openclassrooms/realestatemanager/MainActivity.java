package com.openclassrooms.realestatemanager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import com.facebook.stetho.Stetho;

import butterknife.BindView;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       super.onCreateOptionsMenu(menu);
       hideEditBtn();
       return true;
    }
}
