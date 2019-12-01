package com.openclassrooms.realestatemanager;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.openclassrooms.realestatemanager.addFlat.FlatViewModel;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
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
        configureAndShowSearchFragment();
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

    private void configureToolbar(){
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        Objects.requireNonNull(ab).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(ab).setTitle(R.string.search);
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
