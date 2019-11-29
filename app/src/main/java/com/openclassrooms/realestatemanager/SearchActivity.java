package com.openclassrooms.realestatemanager;

import android.os.Bundle;
import android.view.Menu;

import androidx.lifecycle.ViewModelProviders;

import com.openclassrooms.realestatemanager.addFlat.FlatViewModel;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;

import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity {
    final String FLATID = "flatId";
    public Long mFlatId;
    private Bundle mExtras;
    private FlatViewModel mFlatViewModel;
    private static int AGENT_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        configureToolbar();

        configureAndShowSearchFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Top menu creation
        getMenuInflater().inflate(R.menu.secondary_menu, menu);
        mEditBtn = menu.findItem(R.id.secondary_menu_edit);
        mListBtn = menu.findItem(R.id.secondary_menu_list);
        mMapBtn = menu.findItem(R.id.secondary_menu_map);
        hideListBtn();
        hideMapBtn();
        return true;
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
