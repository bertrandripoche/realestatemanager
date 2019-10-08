package com.openclassrooms.realestatemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlatDetailActivity extends BaseActivity {

    final String FLATID = "flatId";
    public  String mFlatId;
    private Bundle mExtras;
    FlatDetailFragment mFlatDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_detail);
        ButterKnife.bind(this);
        configureToolbar();

        mExtras = getBundle();
        mFlatId = getTagIdFromBundle();

        configureAndShowFlatDetailFragment();
    }

    private Bundle getBundle() {
        return this.getIntent().getExtras();
    }

    private String getTagIdFromBundle() {
        if (mExtras.get(FLATID) != null) return mExtras.get(FLATID).toString();
        else return null;
    }

}
