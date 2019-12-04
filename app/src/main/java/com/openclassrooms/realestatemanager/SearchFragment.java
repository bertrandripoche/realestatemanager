package com.openclassrooms.realestatemanager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.lang.String.valueOf;

public class SearchFragment extends Fragment {
    private static final String PRICE_MAX = "2000000";
    private static final String SURFACE_MAX = "500";
    private static final String ROOM_MAX = "10";
    private static final String BEDROOM_MAX = "10";
    private static final String BATHROOM_MAX = "10";
    @BindView(R.id.btn_search) FloatingActionButton mSearchBtn;
    @BindView(R.id.filter_city) EditText mCity;
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

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if ((!mSoldDateMin.getText().toString().equals("") || !mSoldDateMax.getText().toString().equals(""))&& mSoldOnly.isChecked()) {
                mSoldOnly.setChecked(false);
            }
        }
    };

    private void configureTextWatchers() {
        mAvailableDateMin.addTextChangedListener(textWatcher);
        mAvailableDateMax.addTextChangedListener(textWatcher);
        mSoldDateMin.addTextChangedListener(textWatcher);
        mSoldDateMax.addTextChangedListener(textWatcher);
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
        if (checkFormValidity()) {
            String mySQLQuery = createSQLQuery();
            System.out.println("Ma query : "+mySQLQuery);
        }
        else Toast.makeText(getContext(), R.string.warning_wrong_form,Toast.LENGTH_LONG).show();
    }

    private String createSQLQuery() {
        boolean isFirstFilter = true;
        String mySQLQuery = "SELECT * FROM Flat";

        // For the city
        if (!mCity.getText().toString().equals("")) {
            mySQLQuery = mySQLQuery + " WHERE (city LIKE " + mCity.getText().toString() + ") ";
            isFirstFilter = false;
        }

        // For the "range" filters
        String[] priceFilter = {"price", mPriceMin.getText().toString(), mPriceMax.getText().toString(),  valueOf(getResources().getInteger(R.integer.price_max_filter))};
        String[] surfaceFilter = {"surface", mSurfaceMin.getText().toString(), mSurfaceMax.getText().toString(), valueOf(getResources().getInteger(R.integer.surface_max_filter))};
        String[] room = {"room", mRoomMin.getText().toString(), mRoomMax.getText().toString(), valueOf(getResources().getInteger(R.integer.room_max_filter))};
        String[] bedroom = {"bedroom", mBedroomMin.getText().toString(), mBedroomMax.getText().toString(), valueOf(getResources().getInteger(R.integer.bedroom_max_filter))};
        String[] bathroom = {"bathroom", mBathroomMin.getText().toString(), mBathroomMax.getText().toString(), valueOf(getResources().getInteger(R.integer.bathroom_max_filter))};
        String[] availableDate = {"availableDate", mAvailableDateMin.getText().toString(), mAvailableDateMax.getText().toString(), "date"};
        String[] soldDate = {"soldDate", mSoldDateMin.getText().toString(), mSoldDateMax.getText().toString(), "date"};
        String[][] myFilters = {priceFilter, surfaceFilter, room, bedroom, bathroom, availableDate, soldDate};

        for (String[] filter : myFilters) {
            String field = filter[0];
            String maxLimit = filter[3];
            String min, max;
            if (filter[3].equals("date")) {
                min = "'" + filter[1] + "'";
                max = "'" + filter[2] + "'";
            } else {
                min = filter[1];
                max = filter[2];
            }

            if ((!min.equals("0") && !min.equals("''"))  || (!max.equals(maxLimit) && !max.equals("''"))) {
                if (isFirstFilter) {
                    if (min.equals("0") || min.equals("''")) mySQLQuery = mySQLQuery + " WHERE (" + field + " <= " + max + ") ";
                    else if (max.equals(maxLimit) || max.equals("''")) mySQLQuery = mySQLQuery + " WHERE (" + field + " >= " + min + ") ";
                    else mySQLQuery = mySQLQuery + " WHERE (" + field + " BETWEEN " + min + " AND " + max + ") ";
                    isFirstFilter = false;
                } else {
                    if (min.equals("0") || min.equals("''")) mySQLQuery = mySQLQuery + " AND (" + field + " <= " + max + ") ";
                    else if (max.equals(maxLimit) || max.equals("''")) mySQLQuery = mySQLQuery + " AND (" + field + " >= " + min + ") ";
                    else mySQLQuery = mySQLQuery + " AND (" + field + " BETWEEN " + min + " AND " + max + ") ";
                }
            }
        }

        // For the switch filters
        Map<String, Boolean> switches =  new HashMap<String, Boolean>();
        switches.put("isSchool", mSchool.isChecked());
        switches.put("isPostOffice", mPostOffice.isChecked());
        switches.put("isRestaurant", mRestaurant.isChecked());
        switches.put("isTheater", mTheater.isChecked());
        switches.put("isShop", mShop.isChecked());
        switches.put("isSold", mSoldOnly.isChecked());

        for (Map.Entry<String, Boolean> pair: switches.entrySet()) {
            String field = pair.getKey();
            boolean isChecked = pair.getValue();
            if (isChecked) {
                if (isFirstFilter) {
                    mySQLQuery = mySQLQuery + " WHERE (" + field + " = 'true') ";
                    isFirstFilter = false;
                }
                else mySQLQuery = mySQLQuery + " AND (" + field + " = 'true') ";
            }
        }

        // For the sale status
        if (mOnSaleOnly.isChecked()) {
            if (isFirstFilter) {
                mySQLQuery = mySQLQuery + " WHERE (isSold = 'false') ";
                isFirstFilter = false;
            }
            else mySQLQuery = mySQLQuery + " AND (isSold = 'false') ";
        }

        return mySQLQuery;
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
            if (!mSoldOnly.isChecked()) {mSoldOnly.setChecked(true);mOnSaleOnly.setChecked(false);}
            if (!Utils.isBeforeOrEqualTo(soldDateMax, availableDateMin)) {mSoldDateMax.setText(""); isValid=false;}
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
                mPriceMin.setText(valueOf(minValue));
                mPriceMax.setText(valueOf(maxValue));
            }
        });
        mSurfaceRangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                mSurfaceMin.setText(valueOf(minValue));
                mSurfaceMax.setText(valueOf(maxValue));
            }
        });
        mRoomRangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                mRoomMin.setText(valueOf(minValue));
                mRoomMax.setText(valueOf(maxValue));
            }
        });
        mBedroomRangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                mBedroomMin.setText(valueOf(minValue));
                mBedroomMax.setText(valueOf(maxValue));
            }
        });
        mBathroomRangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                mBathroomMin.setText(valueOf(minValue));
                mBathroomMax.setText(valueOf(maxValue));
            }
        });
    }
}
