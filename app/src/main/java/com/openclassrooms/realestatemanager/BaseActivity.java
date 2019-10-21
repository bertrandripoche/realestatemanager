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
                myClass = AddFlatActivity.class;
                break;
            case "EditFlatActivity":
                Long flatId;
                if (mFlatDetailFragment != null) flatId = mFlatDetailFragment.getFlatId();
                else {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment currentFragment = fragmentManager.findFragmentByTag("FlatDetail");
                    Bundle b = currentFragment.getArguments();
                    flatId = b.getLong("flatId");
                }
                System.out.println("My flatId is "+flatId);
                myClass = EditFlatActivity.class;
                break;
            default:
                myClass = MainActivity.class;
        }
        Intent myIntent = new Intent(this, myClass);
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

}
