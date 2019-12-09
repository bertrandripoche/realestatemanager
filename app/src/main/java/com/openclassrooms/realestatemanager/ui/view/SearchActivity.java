package com.openclassrooms.realestatemanager.ui.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProviders;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.viewmodel.FlatViewModel;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity {
    private static final String SEARCH = "Search";
    final String FLATID = "flatId";
    public Long mFlatId;
    private Bundle mExtras;
    private FlatViewModel mFlatViewModel;
    private static int AGENT_ID = 0;
    private SearchFragment mSearchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        configureToolbar();
//        loadFragment(new SearchFragment(), R.id.container_fragment_search);
        configureFragment(new SearchFragment(), R.id.container_fragment_search, SEARCH);
        //configureAndShowSearchFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void configureToolbar(){
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        Objects.requireNonNull(ab).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(ab).setTitle(R.string.search_title);
    }

    protected void configureAndShowSearchFragment(){
        mSearchFragment = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.container_fragment_search);
        if (mSearchFragment == null && findViewById(R.id.container_fragment_search) != null) {
            mSearchFragment = new SearchFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_fragment_search, mSearchFragment)
                    .commit();
        }
    }

    private Bundle getBundle() {
        return this.getIntent().getExtras();
    }

    private Long getTagIdFromBundle() {
        if (mExtras.get(FLATID) != null) return (Long) mExtras.get(FLATID);
        else return null;
    }

    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mFlatViewModel = ViewModelProviders.of(this, mViewModelFactory).get(FlatViewModel.class);
        this.mFlatViewModel.init(AGENT_ID);
    }
}
