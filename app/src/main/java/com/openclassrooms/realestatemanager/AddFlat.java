package com.openclassrooms.realestatemanager;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.realestatemanager.addFlat.FlatViewModel;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Flat;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddFlat extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.btn_add_flat) FloatingActionButton mBtnAddFlat;
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

    // 1 - FOR DATA
    private FlatViewModel mFlatViewModel;
    private static int AGENT_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flat);
        ButterKnife.bind(this);

        this.configureToolbar();
        this.configureTextWatchers();
        this.configureViewModel();
    }

    private void configureToolbar(){
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        Objects.requireNonNull(ab).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(ab).setTitle(R.string.addFlat);
    }

    private void configureTextWatchers() {
        mSummary.addTextChangedListener(textWatcher);
        mDescription.addTextChangedListener(textWatcher);
        mSurface.addTextChangedListener(textWatcher);
        mPrice.addTextChangedListener(textWatcher);
        mCity.addTextChangedListener(textWatcher);
    }

    @OnClick(R.id.btn_add_flat)
    public void onClickAddButton() {
        if (!this.mSummary.getText().toString().equals("") && !this.mDescription.getText().toString().equals("")  && !this.mSurface.getText().toString().equals("")  && !this.mPrice.getText().toString().equals("")  && !this.mCity.getText().toString().equals("")) {
            this.createFlat();
        }

    }

    // DATA

    // 2 - Configuring ViewModel
    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mFlatViewModel = ViewModelProviders.of(this, mViewModelFactory).get(FlatViewModel.class);
        this.mFlatViewModel.init(AGENT_ID);
    }

    // 3 - Create a new item
    private void createFlat(){
        Integer price = getNumber(this.mPrice.getText().toString());
        Integer surface = getNumber(this.mSurface.getText().toString());
        Integer room = getNumber(this.mRoomNb.getText().toString());
        Integer bedroom = getNumber(this.mBedroomNb.getText().toString());
        Integer bathroom = getNumber(this.mBathroomNb.getText().toString());
        Integer streetNb = getNumber(this.mStreetNb.getText().toString());
        Integer postalCode = getNumber(this.mPostalCode.getText().toString());

        Flat flat = new Flat(
                this.mSummary.getText().toString(),
                this.mDescription.getText().toString(),
                this.mFlatType.getSelectedItem().toString(),
                price,
                surface,
                room,
                bedroom,
                bathroom,
                streetNb,
                this.mStreet.getText().toString(),
                postalCode,
                this.mCity.getText().toString(),
                AGENT_ID);

                this.mFlatViewModel.createFlat(flat);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Enable-disable Floating Action Button
            if (!mSummary.getText().toString().equals("") && !mDescription.getText().toString().equals("")  && !mSurface.getText().toString().equals("")  && !mPrice.getText().toString().equals("")  && !mCity.getText().toString().equals("")) {
                enableAddFlatButton();
            } else {
                disableAddFlatButton();
            }
        }

    };

    private void enableAddFlatButton() {
        mBtnAddFlat.setClickable(true);
        mBtnAddFlat.setFocusable(true);
        mBtnAddFlat.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.pink)));
    }

    private void disableAddFlatButton() {
        mBtnAddFlat.setClickable(false);
        mBtnAddFlat.setFocusable(false);
        mBtnAddFlat.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_grey)));
    }

    private Integer getNumber(String str) {
        Integer number;
        try
            {number = Integer.parseInt(str);}
        catch (NumberFormatException e)
            {number = null;}
        return number;
    }
}
