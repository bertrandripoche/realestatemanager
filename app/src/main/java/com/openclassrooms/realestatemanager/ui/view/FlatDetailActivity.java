package com.openclassrooms.realestatemanager.ui.view;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.Menu;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.viewmodel.FlatViewModel;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;

import butterknife.ButterKnife;

public class FlatDetailActivity extends BaseActivity {

    private static final String DETAIL = "Detail";
    final String FLATID = "flatId";
    public Long mFlatId;
    private Bundle mExtras;
    private FlatViewModel mFlatViewModel;
    private static int AGENT_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_detail);
        ButterKnife.bind(this);
        configureToolbar();
        displayEditBtn();

        configureViewModel();

        mExtras = getBundle();
        mFlatId = getFlatIdFromBundle();

        configureAndShowFlatDetailFragment();
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

    /**
     * Get the bundle
     * @return Bundle
     */
    private Bundle getBundle() {
        return this.getIntent().getExtras();
    }

    /**
     * Get flatId from bundle
     * @return a Long which is the flatId
     */
    private Long getFlatIdFromBundle() {
        if (mExtras.get(FLATID) != null) return (Long) mExtras.get(FLATID);
        else return null;
    }

    /**
     * Configure the viewModel
     */
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mFlatViewModel = ViewModelProviders.of(this, mViewModelFactory).get(FlatViewModel.class);
        this.mFlatViewModel.init(AGENT_ID);
    }

}
