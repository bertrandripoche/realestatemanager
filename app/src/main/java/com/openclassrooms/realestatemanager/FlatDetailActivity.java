package com.openclassrooms.realestatemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlatDetailActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar mTtoolbar;

    final String FLATID = "flatId";
    public  String mFlatId;
    private Bundle mExtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);
        configureToolbar();

        mExtras = getBundle();
        mFlatId = getTagIdFromBundle();

        configureAndShowFlatDetailFragment();
    }

    private void configureAndShowFlatDetailFragment() {
        FlatDetailFragment flatDetailFragment;
        flatDetailFragment = (FlatDetailFragment) getSupportFragmentManager().findFragmentById(R.id.container_fragment_flat_detail);

        if (flatDetailFragment == null) {
            flatDetailFragment = new FlatDetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_fragment_flat_detail, flatDetailFragment)
                    .commit();
        }
    }

    private Bundle getBundle() {
        return this.getIntent().getExtras();
    }

    private String getTagIdFromBundle() {
        if (mExtras.get(FLATID) != null) return mExtras.get(FLATID).toString();
        else return null;
    }

    private void configureToolbar(){
        setSupportActionBar(mTtoolbar);
    }

}
