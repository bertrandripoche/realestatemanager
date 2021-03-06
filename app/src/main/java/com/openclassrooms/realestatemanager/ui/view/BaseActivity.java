package com.openclassrooms.realestatemanager.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.openclassrooms.realestatemanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar mToolbar;

    private static final String LIST = "List";
    private static final String DETAIL = "Detail";
    private static final String SEARCH = "Search";
    final String FLATID = "flatId";
    final String SELECTEDFLAT = "selectedFlat";

    MenuItem mEditBtn;
    MenuItem mListBtn;
    MenuItem mMapBtn;
    MenuItem mSearchBtn;
    FlatListFragment mFlatListFragment;
    FlatDetailFragment mFlatDetailFragment;
    SearchFragment mSearchFragment;

    boolean mIsTablet;
    protected Long mFlatId = null;
    protected int mSelectedFlat = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mIsTablet = getResources().getBoolean(R.bool.isTablet);

        this.configureToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Top menu creation
        getMenuInflater().inflate(R.menu.secondary_menu, menu);
        mEditBtn = menu.findItem(R.id.secondary_menu_edit);
        mListBtn = menu.findItem(R.id.secondary_menu_list);
        mMapBtn = menu.findItem(R.id.secondary_menu_map);
        mSearchBtn = menu.findItem(R.id.secondary_menu_search);
        hideListBtn();
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
            case R.id.secondary_menu_map:
                launchActivity("MapActivity");
                return true;
            case R.id.secondary_menu_list:
                launchActivity("MainActivity");
                return true;
            case R.id.secondary_menu_search:
                if (mIsTablet) {
                    loadFragment(new SearchFragment(), R.id.container_fragment_flat_detail);
                    hideEditBtn();
                }
                else launchActivity("SearchActivity");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Start activities from button
     * @param activity is a string representing the activity
     */
    protected void launchActivity(String activity) {
        Class myClass;

        switch(activity) {
            case "FlatDetail":
                myClass = FlatDetailActivity.class;
                break;
            case "AddFlatActivity":
                mFlatId = createBundleWithFlatId(mFlatId);
                myClass = AddFlatActivity.class;
                break;
            case "EditFlatActivity":
                mFlatId = createBundleWithFlatId(mFlatId);
                myClass = EditFlatActivity.class;
                break;
            case "MapActivity":
                mFlatId = createBundleWithFlatId(mFlatId);
                myClass = MapActivity.class;
                break;
            case "MainActivity":
                mFlatId = createBundleWithFlatId(mFlatId);
                myClass = MainActivity.class;
                break;
            case "SearchActivity":
                mFlatId = createBundleWithFlatId(mFlatId);
                myClass = SearchActivity.class;
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

    /**
     * Load fragment
     * @param fragment the fragment to load
     * @param container the container where to load the fragment
     */
    protected void loadFragment(Fragment fragment, int container) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Configure fragment
     * @param fragment the fragment to configure
     * @param container the container where to load the fragment
     * @param type is the type of fragment to configure
     */
    protected void configureFragment(Fragment fragment, int container, String type) {
        fragment =  getSupportFragmentManager().findFragmentById(container);

        switch (type) {
            case DETAIL :
                mFlatDetailFragment = (FlatDetailFragment) getSupportFragmentManager().findFragmentById(container);
                if (mFlatDetailFragment == null) mFlatDetailFragment = new FlatDetailFragment();
                break;
            case SEARCH :
                mSearchFragment = (SearchFragment) getSupportFragmentManager().findFragmentById(container);
                if (mSearchFragment == null) mSearchFragment = new SearchFragment();
                break;
            default :
                break;
        }

        if (fragment == null) {
            switch (type) {
                case LIST :
                    fragment = new FlatListFragment();
                    break;
                case DETAIL :
                    fragment = new FlatDetailFragment();
                    break;
                case SEARCH :
                    fragment = new SearchFragment();
                    break;
                default :
                    break;
            }

            getSupportFragmentManager().beginTransaction()
                    .add(container, fragment)
                    .commit();
        }
    }

    /**
     * Configure the flatDetail fragment
     */
    protected void configureAndShowFlatDetailFragment(){
        mFlatDetailFragment = (FlatDetailFragment) getSupportFragmentManager().findFragmentById(R.id.container_fragment_flat_detail);
        if (mFlatDetailFragment == null && findViewById(R.id.container_fragment_flat_detail) != null) {
            mFlatDetailFragment = new FlatDetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_fragment_flat_detail, mFlatDetailFragment)
                    .commit();
        }
    }

    /**
     * Configure the searchDetail fragment
     */
    protected void configureAndShowSearchFragment(){
        mSearchFragment = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.container_fragment_search);
        if (mSearchFragment == null && findViewById(R.id.container_fragment_search) != null) {
            mSearchFragment = new SearchFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_fragment_search, mSearchFragment)
                    .commit();
        }
    }

    /**
     * Configure the toolbar
     */
    protected void configureToolbar(){
        setSupportActionBar(mToolbar);
    }

    /**
     * Hide the list button
     */
    protected void hideListBtn() {if (mListBtn != null) mListBtn.setVisible(false);}
    /**
     * Display the list button
     */
    protected void displayListBtn() {if (mListBtn != null) mListBtn.setVisible(true);}
    /**
     * Hide the map button
     */
    protected void hideMapBtn() {if (mMapBtn != null) mMapBtn.setVisible(false);}
    /**
     * Display the map button
     */
    protected void displayMapBtn() {if (mMapBtn != null) mMapBtn.setVisible(true);}
    /**
     * Hide the edit button
     */
    protected void hideEditBtn() {if (mEditBtn != null) mEditBtn.setVisible(false);}
    /**
     * Display the edit button
     */
    protected void displayEditBtn() {if (mEditBtn != null) mEditBtn.setVisible(true);}
    /**
     * Hide the search button
     */
    protected void hideSearchBtn() {if (mSearchBtn != null) mSearchBtn.setVisible(false);}
    /**
     * Display the search button
     */
    protected void displaySearchBtn() {if (mSearchBtn != null) mSearchBtn.setVisible(true);}

    /**
     * Create a bundle with the flatId
     * @param flatId is the flatId
     * @return
     */
    protected Long createBundleWithFlatId(Long flatId) {
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
