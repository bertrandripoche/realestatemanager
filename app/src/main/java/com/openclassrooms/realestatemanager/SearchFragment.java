package com.openclassrooms.realestatemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.realestatemanager.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchFragment extends Fragment {
    @BindView(R.id.btn_search) FloatingActionButton mSearchBtn;
    @BindView(R.id.priceRangeSeekbar) CrystalRangeSeekbar mPriceRangeSeekbar;
    @BindView(R.id.priceMin) TextView mPriceMin;
    @BindView(R.id.priceMax) TextView mPriceMax;
    @BindView(R.id.surfaceRangeSeekbar) CrystalRangeSeekbar mSurfaceRangeSeekbar;
    @BindView(R.id.surfaceMin) TextView mSurfaceMin;
    @BindView(R.id.surfaceMax) TextView mSurfaceMax;
    @BindView(R.id.roomRangeSeekbar) CrystalRangeSeekbar mRoomRangeSeekbar;
    @BindView(R.id.roomMin) TextView mRoomMin;
    @BindView(R.id.roomMax) TextView mRoomMax;
    @BindView(R.id.bedroomRangeSeekbar) CrystalRangeSeekbar mBedroomRangeSeekbar;
    @BindView(R.id.bedroomMin) TextView mBedroomMin;
    @BindView(R.id.bedroomMax) TextView mBedroomMax;
    @BindView(R.id.bathroomRangeSeekbar) CrystalRangeSeekbar mBathroomRangeSeekbar;
    @BindView(R.id.bathroomMin) TextView mBathroomMin;
    @BindView(R.id.bathroomMax) TextView mBathroomMax;
    @BindView(R.id.filter_school_switch) SwitchCompat mSchool;
    @BindView(R.id.filter_post_office_switch) SwitchCompat mPostOffice;
    @BindView(R.id.filter_restaurant_switch) SwitchCompat mRestaurant;
    @BindView(R.id.filter_theater_switch) SwitchCompat mTheater;
    @BindView(R.id.filter_shop_switch) SwitchCompat mShop;
    @BindView(R.id.filter_sold_switch) SwitchCompat mSoldOnly;
    @BindView(R.id.filter_on_sale_switch) SwitchCompat mOnSaleOnly;
    @BindView(R.id.filter_sold_min) EditText mSoldDateMin;
    @BindView(R.id.filter_sold_max) EditText mSoldDateMax;
    @BindView(R.id.filter_available_min) EditText mAvailableDateMin;
    @BindView(R.id.filter_available_max) EditText mAvailableDateMax;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        configureSeekBar();

        return view;
    }

    @OnClick(R.id.filter_on_sale_switch)
    public void onClickOnSaleOnlyButton() {
        if (mSoldOnly.isChecked()) {
            mSoldOnly.setChecked(false);
            mSoldDateMin.setText("");
            mSoldDateMax.setText("");
        }
    }

    @OnClick(R.id.filter_sold_switch)
    public void onClickSoldOnlyButton() {
        if (mOnSaleOnly.isChecked()) mOnSaleOnly.setChecked(false);
    }

    @OnClick(R.id.btn_search)
    public void onClickSearchButton() {
        if (checkFormValidity()) System.out.println("Looks good");
        else System.out.println("So baaad");
    }

    private boolean checkFormValidity() {
        boolean isValid = true;

        String soldDateMin = mSoldDateMin.getText().toString();
        String soldDateMax = mSoldDateMax.getText().toString();
        String availableDateMin = mAvailableDateMin.getText().toString();
        String availableDateMax = mAvailableDateMax.getText().toString();

        if (!Utils.isCorrectDate(soldDateMin)) {mSoldDateMin.setText(""); isValid=false;}
        if (!Utils.isCorrectDate(soldDateMax)) {mSoldDateMax.setText(""); isValid=false;}
        if (!Utils.isCorrectDate(availableDateMin)) {mAvailableDateMin.setText(""); isValid=false;}
        if (!Utils.isCorrectDate(availableDateMax)) {mAvailableDateMax.setText(""); isValid=false;}

        if (!soldDateMin.equals("") && !soldDateMax.equals("")) {
            if (!Utils.isBeforeOrEqualTo(soldDateMin, soldDateMax)) {mSoldDateMax.setText(""); isValid=false;}
        }
        if (!availableDateMin.equals("") && !availableDateMax.equals("")) {
            if (!Utils.isBeforeOrEqualTo(availableDateMin, availableDateMax)) {mAvailableDateMax.setText(""); isValid=false;}
        }

        if (mOnSaleOnly.isChecked()) {mSoldDateMin.setText(""); mSoldDateMax.setText(""); isValid=false;}

        return isValid;
    }

    private void configureSeekBar() {
        mPriceRangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                mPriceMin.setText(String.valueOf(minValue));
                mPriceMax.setText(String.valueOf(maxValue));
            }
        });
        mSurfaceRangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                mSurfaceMin.setText(String.valueOf(minValue));
                mSurfaceMax.setText(String.valueOf(maxValue));
            }
        });
        mRoomRangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                mRoomMin.setText(String.valueOf(minValue));
                mRoomMax.setText(String.valueOf(maxValue));
            }
        });
        mBedroomRangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                mBedroomMin.setText(String.valueOf(minValue));
                mBedroomMax.setText(String.valueOf(maxValue));
            }
        });
        mBathroomRangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                mBathroomMin.setText(String.valueOf(minValue));
                mBathroomMax.setText(String.valueOf(maxValue));
            }
        });
    }
}
