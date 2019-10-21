package com.openclassrooms.realestatemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.Menu;

import com.openclassrooms.realestatemanager.addFlat.FlatViewModel;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlatDetailActivity extends BaseActivity {

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
        mFlatId = getTagIdFromBundle();

        configureAndShowFlatDetailFragment();
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
