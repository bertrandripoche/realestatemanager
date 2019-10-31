package com.openclassrooms.realestatemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    MenuItem mEditBtn;
    FlatDetailFragment mFlatDetailFragment;
    protected Long mFlatId = null;
    protected int mSelectedFlat = -1;
    final String FLATID = "flatId";
    final String SELECTEDFLAT = "selectedFlat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        this.configureToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Top menu creation
        getMenuInflater().inflate(R.menu.secondary_menu, menu);
        mEditBtn = menu.findItem(R.id.secondary_menu_edit);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Secondary menu item selection
        switch (item.getItemId()) {
            case R.id.secondary_menu_add:
                launchActivity("AddFlatActivity");
                return true;
            case R.id.secondary_menu_edit:
                launchActivity("EditFlatActivity");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void launchActivity(String activity) {
        Class myClass;

        switch(activity) {
            case "FlatDetail":
                myClass = FlatDetailActivity.class;
                break;
            case "AddFlatActivity":
                mFlatId = createBundleWithFlatId(mFlatId, FLATID);
                myClass = AddFlatActivity.class;
                break;
            case "EditFlatActivity":
                mFlatId = createBundleWithFlatId(mFlatId, FLATID);
                myClass = EditFlatActivity.class;
                break;
            default:
                myClass = MainActivity.class;
        }
        Intent myIntent = new Intent(this, myClass);
        if (mFlatId != null || mSelectedFlat != -1) {
            Bundle b = new Bundle();
            if (mFlatId != null) b.putLong(FLATID, mFlatId);
            if (mSelectedFlat != -1) b.putInt(SELECTEDFLAT, mSelectedFlat);
            myIntent.putExtras(b);
        }
        this.startActivity(myIntent);
    }

    protected void configureToolbar(){
        setSupportActionBar(mToolbar);
        mFlatDetailFragment = (FlatDetailFragment) getSupportFragmentManager().findFragmentById(R.id.container_fragment_flat_detail);
    }

    protected void configureAndShowFlatDetailFragment(){
        mFlatDetailFragment = (FlatDetailFragment) getSupportFragmentManager().findFragmentById(R.id.container_fragment_flat_detail);
        if (mFlatDetailFragment == null && findViewById(R.id.container_fragment_flat_detail) != null) {
            mFlatDetailFragment = new FlatDetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_fragment_flat_detail, mFlatDetailFragment)
                    .commit();
        }
    }

    protected void hideEditBtn() {
        if (mEditBtn != null) mEditBtn.setVisible(false);
    }

    protected void displayEditBtn() {
        if (mEditBtn != null) mEditBtn.setVisible(true);
    }

    protected Long createBundleWithFlatId(Long flatId, String FLATID) {
        if (mFlatDetailFragment != null) flatId = mFlatDetailFragment.getFlatId();
        else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment currentFragment = fragmentManager.findFragmentByTag("FlatDetail");
            Bundle args;
            if (currentFragment != null) {
                args = currentFragment.getArguments();
                flatId = args.getLong(FLATID);
            }
        }
        return flatId;
    }

}
