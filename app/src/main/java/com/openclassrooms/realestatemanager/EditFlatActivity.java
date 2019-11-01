package com.openclassrooms.realestatemanager;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.openclassrooms.realestatemanager.utils.Utils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    private Flat mFlat;
    private Long mFlatId;
    private int mSelectedFlat = -1;
    private FlatViewModel mFlatViewModel;
    private static int AGENT_ID = 0;
    final String FLATID = "flatId";
    final String SELECTEDFLAT = "selectedFlat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_flat);
        ButterKnife.bind(this);
        mFlatId = getFlatId();
        mSelectedFlat = getSelectedFlat();

        configureToolbar();
        configureSpinners();
        configureViewModel();

        getFlat(mFlatId);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed()
    {
        final String FLATID = "flatId";

        Intent intent = new Intent(EditFlatActivity.this,MainActivity.class);
        Bundle b = new Bundle();
        b.putLong(FLATID, mFlatId);
        b.putInt(SELECTEDFLAT, mSelectedFlat);
        intent.putExtras(b);

        startActivity(intent);
    }

    @OnClick(R.id.btn_save_flat)
    public void onClickAddButton() {
        this.updateFlat();
        Toast.makeText(getApplicationContext(), R.string.flat_updated, Toast.LENGTH_LONG).show();
    }

    private void configureToolbar(){
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        Objects.requireNonNull(ab).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(ab).setTitle(R.string.editFlat);
    }

    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mFlatViewModel = ViewModelProviders.of(this, mViewModelFactory).get(FlatViewModel.class);
        this.mFlatViewModel.init(AGENT_ID);
    }

    private void configureSpinners() {
        ArrayAdapter adapterSpinnerFlatType = ArrayAdapter.createFromResource(this, R.array.flat_type, R.layout.spinner_item);
        adapterSpinnerFlatType.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mFlatType.setAdapter(adapterSpinnerFlatType);
        ArrayAdapter adapterSpinnerFlatAgent = ArrayAdapter.createFromResource(this, R.array.flat_agent, R.layout.spinner_item);
        adapterSpinnerFlatAgent.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mFlatAgent.setAdapter(adapterSpinnerFlatAgent);
    }

    protected Long getFlatId() {
        Long flatId = null;
        Intent i = getIntent();
        if (i != null) {
            flatId = i.getExtras().getLong(FLATID);
        }
        return flatId;
    }

    protected int getSelectedFlat() {
        int selectedFlat = -1;
        Intent i = getIntent();
        if (i != null) {
            selectedFlat = i.getExtras().getInt(SELECTEDFLAT);
        }
        return selectedFlat;
    }

    private void getFlat(long flatId) {
        this.mFlatViewModel.getFlatFromId(flatId).observe(this, this::populateData);
    }

    private void populateData(Flat flat) {
        mFlat = flat;
        if (mFlat != null) {
            mSummary.setText(mFlat.getSummary());
            mDescription.setText(getString(R.string.description_text, mFlat.getDescription()));
            if (mFlat.getPrice() != null) mPrice.setText(String.valueOf(mFlat.getPrice()));

            mCity.setText(mFlat.getCityAddress());
            if (mFlat.getSurface() != null) mSurface.setText(String.valueOf(mFlat.getSurface()));

            setSpinner(mFlatType, mFlat.getType());

            if (mFlat.getNumberAddress() == null) mStreetNb.setText("");
            else mStreetNb.setText(String.valueOf(mFlat.getNumberAddress()));
            if (mFlat.getStreetAddress() == null) mStreet.setText("");
            else mStreet.setText(mFlat.getStreetAddress());
            if (mFlat.getPostalCodeAddress() == null) mPostalCode.setText("");
            else mPostalCode.setText(String.valueOf(mFlat.getPostalCodeAddress()));
            if (mFlat.getRoom() == null) mRoomNb.setText("");
            else mRoomNb.setText(String.valueOf(mFlat.getRoom()));
            if (mFlat.getBathroom() == null) mBathroomNb.setText("");
            else mBathroomNb.setText(String.valueOf(mFlat.getBathroom()));
            if (mFlat.getBedroom() == null) mBedroomNb.setText("");
            else mBedroomNb.setText(String.valueOf(mFlat.getBedroom()));

            if (mFlat.isSchool()) mSchool.setChecked(true);
            if (mFlat.isPostOffice()) mPostOffice.setChecked(true);
            if (mFlat.isRestaurant()) mRestaurant.setChecked(true);
            if (mFlat.isTheater()) mTheater.setChecked(true);
            if (mFlat.isShop()) mShop.setChecked(true);

        }
    }

    private void setSpinner(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        int spinnerPosition = adapter.getPosition(value);
        spinner.setSelection(spinnerPosition);
    }

    private void updateFlat() {
        Integer price = getNumber(this.mPrice.getText().toString());
        Integer surface = getNumber(this.mSurface.getText().toString());
        Integer room = getNumber(this.mRoomNb.getText().toString());
        Integer bedroom = getNumber(this.mBedroomNb.getText().toString());
        Integer bathroom = getNumber(this.mBathroomNb.getText().toString());
        Integer streetNb = getNumber(this.mStreetNb.getText().toString());
        Integer postalCode = getNumber(this.mPostalCode.getText().toString());

        String address = Utils.buildAddress(streetNb, this.mStreet.getText().toString(), postalCode, this.mCity.getText().toString());
        Address flatAddress = getAddressFromSearchString(address);

        mFlat.setSummary(this.mSummary.getText().toString());
        mFlat.setDescription(this.mDescription.getText().toString());
        mFlat.setType(this.mFlatType.getSelectedItem().toString());
        mFlat.setPrice(price);
        mFlat.setSurface(surface);
        mFlat.setRoom(room);
        mFlat.setBedroom(bedroom);
        mFlat.setBathroom(bathroom);
        mFlat.setNumberAddress(streetNb);
        mFlat.setStreetAddress(this.mStreet.getText().toString());
        mFlat.setPostalCodeAddress(postalCode);
        mFlat.setCityAddress(this.mCity.getText().toString());
        mFlat.setLatitude(flatAddress.getLatitude());
        mFlat.setLongitude(flatAddress.getLongitude());
        mFlat.setSchool(mSchool.isChecked());
        mFlat.setPostOffice(mPostOffice.isChecked());
        mFlat.setRestaurant(mRestaurant.isChecked());
        mFlat.setTheater(mTheater.isChecked());
        mFlat.setShop(mShop.isChecked());

        this.mFlatViewModel.updateFlat(mFlat);
    }

    private Integer getNumber(String str) {
        Integer number;
        try
        {number = Integer.parseInt(str);}
        catch (NumberFormatException e)
        {number = null;}
        return number;
    }

    private Address getAddressFromSearchString(String address) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocationName(address, 1);

            if (addressList.size() > 0) {
                System.out.println("Depuis méthode "+addressList.get(0).getLatitude() + " - " + addressList.get(0).getLongitude());
                return addressList.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
