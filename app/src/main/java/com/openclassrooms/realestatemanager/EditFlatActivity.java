package com.openclassrooms.realestatemanager;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditFlatActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.btn_save_flat) FloatingActionButton mBtnSaveFlat;
    @BindView(R.id.edit_flat_type) Spinner mFlatType;
    @BindView(R.id.edit_summary) EditText mSummary;
    @BindView(R.id.edit_description) EditText mDescription;
    @BindView(R.id.edit_surface) EditText mSurface;
    @BindView(R.id.edit_room) EditText mRoomNb;
    @BindView(R.id.edit_bedroom) EditText mBedroomNb;
    @BindView(R.id.edit_bathroom) EditText mBathroomNb;
    @BindView(R.id.edit_price) EditText mPrice;
    @BindView(R.id.edit_street_nb) EditText mStreetNb;
    @BindView(R.id.edit_street) EditText mStreet;
    @BindView(R.id.edit_postal_code) EditText mPostalCode;
    @BindView(R.id.edit_city) EditText mCity;
    @BindView(R.id.edit_school_switch) SwitchCompat mSchool;
    @BindView(R.id.edit_post_office_switch) SwitchCompat mPostOffice;
    @BindView(R.id.edit_restaurant_switch) SwitchCompat mRestaurant;
    @BindView(R.id.edit_theater_switch) SwitchCompat mTheater;
    @BindView(R.id.edit_shop_switch) SwitchCompat mShop;
    @BindView(R.id.edit_flat_agent) Spinner mFlatAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_flat);
        ButterKnife.bind(this);

        this.configureToolbar();
    }

    private void configureToolbar(){
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        Objects.requireNonNull(ab).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(ab).setTitle(R.string.editFlat);
    }
}
