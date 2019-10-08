package com.openclassrooms.realestatemanager;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddFlat extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.btn_add_flat) FloatingActionButton mBtnAddFlat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flat);
        ButterKnife.bind(this);

        this.configureToolbar();
    }

    private void configureToolbar(){
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        Objects.requireNonNull(ab).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(ab).setTitle(R.string.addFlat);
    }

    @Override
    public void onClick(View v) {

    }
}
