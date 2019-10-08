package com.openclassrooms.realestatemanager;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    FlatDetailFragment mFlatDetailFragment;
    FlatListFragment mFlatListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        configureAndShowFlatListFragment();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Top menu creation
//        getMenuInflater().inflate(R.menu.secondary_menu, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Secondary menu item selection
//        switch (item.getItemId()) {
//            case R.id.secondary_menu_add:
//                launchActivity("AddFlat");
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

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
