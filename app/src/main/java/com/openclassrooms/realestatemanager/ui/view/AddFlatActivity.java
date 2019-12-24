package com.openclassrooms.realestatemanager.ui.view;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.realestatemanager.utils.UriTypeConverter;
import com.openclassrooms.realestatemanager.viewmodel.FlatViewModel;
import com.openclassrooms.realestatemanager.ui.recyclerview.FlatPicAdapter;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Flat;
import com.openclassrooms.realestatemanager.model.Pic;
import com.openclassrooms.realestatemanager.notifications.NotificationService;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class AddFlatActivity extends BaseEditionActivity implements FlatPicAdapter.Listener, FloatButtonActions {
    @BindView(R.id.btn_add_flat) FloatingActionButton mBtnAddFlat;

    private boolean mIsTablet;
    private NotificationService mNotificationService = new NotificationService();

    public AddFlatActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flat);
        ButterKnife.bind(this);
        mIsTablet = getResources().getBoolean(R.bool.isTablet);

        configureToolbar();
        configureViewModel();
        configureSpinners();
        configureTextWatchers();
        checkRecyclerView();

        mFlatId = getFlatId();
        mSelectedFlat = getSelectedFlat();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(AddFlatActivity.this, MainActivity.class);
        Bundle b = new Bundle();
        if (mFlatId != null) b.putLong(FLATID, mFlatId);
        b.putInt(SELECTEDFLAT, mSelectedFlat);
        intent.putExtras(b);

        startActivity(intent);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("flatPicList", mFlatPicList);
        outState.putString("photoTaken", mCurrentPhotoPath);
        if (mIsDialogDisplayed) {
            mAlertDialog.cancel();
            outState.putBoolean("dialogDisplayed", true);
            outState.putString("caption", mCaption.getText().toString());
        }
        if (mPhotoURI != null) {
            String uriInStringFormat = UriTypeConverter.toString(mPhotoURI);
            outState.putString("uri", uriInStringFormat);
        }
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mFlatPicList = savedInstanceState.getParcelableArrayList("flatPicList");
        if (mFlatPicList.size() != 0) checkRecyclerView();
        mCurrentPhotoPath = savedInstanceState.getString("photoTaken");
        if (savedInstanceState.getBoolean("dialogDisplayed")) {
            selectImage(savedInstanceState.getString("caption"));
        }
        mPhotoURI = savedInstanceState.getString("uri") == null ? null : UriTypeConverter.toUri(savedInstanceState.getString("uri"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponse(requestCode, resultCode, data);
    }

    @Override
    public void onClickDeleteButton(int position) {
        Pic pic = mAdapter.getFlatPic(position);
        Toast.makeText(this, R.string.pic_deleted, Toast.LENGTH_SHORT).show();
        mFlatPicList.remove(pic);
        mAdapter.notifyDataSetChanged();
        if (mFlatPicList.size() == 0) mFlatPhotosRecyclerView.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_add_flat)
    public void onClickAddButton() {
        if (isFormValid()) this.createFlat();
        else {
            Toast.makeText(getApplicationContext(), R.string.invalid_form, Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.btn_add_photo)
    @AfterPermissionGranted(RC_IMAGE_PERMS)
    public void onClickPhotoButton() {
        if (!mCaption.getText().toString().equals("")) {
            String caption = mCaption.getText().toString();
            if (!EasyPermissions.hasPermissions(this, PERMS)) {
                EasyPermissions.requestPermissions(this, getString(R.string.permissions_issue), RC_IMAGE_PERMS, PERMS);
                return;
            }
            selectImage(caption);
        }
        else {
            Toast.makeText(getApplicationContext(), R.string.invalid_caption, Toast.LENGTH_LONG).show();
        }
    }

    private void handleResponse(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_SELECT_PIC_GALLERY || requestCode == REQUEST_CAMERA_TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                mSelectedImagePath = "";
                if (requestCode == REQUEST_SELECT_PIC_GALLERY) {
                    mUriImageSelected = data.getData();
                    mSelectedImagePath = getRealPathFromURI(mUriImageSelected);
                } else {
                    mUriImageSelected = mPhotoURI;
                    mSelectedImagePath = mCurrentPhotoPath;
                    if (mUriImageSelected != null) try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mUriImageSelected);
                        bitmap = Utils.handleSamplingAndRotationBitmap(this, mUriImageSelected);
                        OutputStream os= this.getContentResolver().openOutputStream(mUriImageSelected);
                        bitmap.compress(Bitmap.CompressFormat.PNG,100,os);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Pic pic = new Pic(mUriImageSelected, mSelectedImagePath, mCaption.getText().toString(), 0);
                mFlatPicList.add(pic);

                Toast.makeText(this, R.string.picture_saved, Toast.LENGTH_SHORT).show();
                mCaption.setText("");
                checkRecyclerView();
            } else {
                Toast.makeText(this, R.string.no_picture_selected, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkRecyclerView() {
        if (mFlatPicList == null || mFlatPicList.size() == 0) mFlatPhotosRecyclerView.setVisibility(View.GONE);
        else {
            mFlatPhotosRecyclerView.setVisibility(View.VISIBLE);
            if (mAdapter == null) this.configureRecyclerView();
            else mAdapter.notifyDataSetChanged();
        }
    }

    private void configureRecyclerView() {
        mAdapter = new FlatPicAdapter(mFlatPicList, this, true);

        mFlatPhotosRecyclerView.setHasFixedSize(true);
        mFlatPhotosRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        mFlatPhotosRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mFlatViewModel = ViewModelProviders.of(this, mViewModelFactory).get(FlatViewModel.class);
        this.mFlatViewModel.init(AGENT_ID);
    }

    private void configureTextWatchers() {
        mCaption.addTextChangedListener(textWatcher);
        mSummary.addTextChangedListener(textWatcher);
        mDescription.addTextChangedListener(textWatcher);
        mSurface.addTextChangedListener(textWatcher);
        mPrice.addTextChangedListener(textWatcher);
        mCity.addTextChangedListener(textWatcher);
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
            if (isFormValid()) enableFloatButton();
            else disableFloatButton();

            // Enable-disable Photo Button
            if (mCaption.getText().toString().equals("")) disablePhotoButton();
            else enablePhotoButton();
        }
    };

    public void enableFloatButton() {
        mBtnAddFlat.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.pink)));
    }

    public void disableFloatButton() {
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

    private Address getAddressFromSearchString(String address) {
        Geocoder geocoder = new Geocoder(this, Locale.FRANCE);
        try {
            List<Address> addressList = geocoder.getFromLocationName(address, 1);

            if (addressList.size() > 0) {
                return addressList.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Long getFlatId() {
        Long flatId = -1L;
        Intent i = getIntent();
        if (i != null && i.getExtras() != null) {
            flatId = i.getExtras().getLong(FLATID);
        }
        return flatId;
    }

    protected int getSelectedFlat() {
        int selectedFlat = -1;
        Intent i = getIntent();
        if (i != null && i.getExtras() != null) {
            selectedFlat = i.getExtras().getInt(SELECTEDFLAT);
        }
        return selectedFlat;
    }

    private void cleanForm() {
        Toast.makeText(getApplicationContext(), R.string.flat_saved, Toast.LENGTH_LONG).show();
        emptyFields();
        disableFloatButton();
        mFlatPicList = new ArrayList();
        checkRecyclerView();
    }

    private void emptyFields() {
        mFlatType.setSelection(0,true);
        mCaption.setText("");
        mSummary.setText("");
        mDescription.setText("");
        mSurface.setText("");
        mRoomNb.setText("");
        mBedroomNb.setText("");
        mBathroomNb.setText("");
        mPrice.setText("");
        mStreetNb.setText("");
        mStreet.setText("");
        mPostalCode.setText("");
        mCity.setText("");
        mSchool.setChecked(false);
        mPostOffice.setChecked(false);
        mRestaurant.setChecked(false);
        mTheater.setChecked(false);
        mShop.setChecked(false);
        mFlatAgent.setSelection(0,true);
    }

    private void createFlat(){
        String picPath;
        if (mFlatPicList.size() != 0) picPath = mFlatPicList.get(0).getPicPath();
        else picPath = "";
        String summary = this.mSummary.getText().toString();
        String description = this.mDescription.getText().toString();
        Integer flatType = this.mFlatType.getSelectedItemPosition();
        Integer price = getNumber(this.mPrice.getText().toString());
        Integer surface = getNumber(this.mSurface.getText().toString());
        Integer room = getNumber(this.mRoomNb.getText().toString());
        Integer bedroom = getNumber(this.mBedroomNb.getText().toString());
        Integer bathroom = getNumber(this.mBathroomNb.getText().toString());
        String street = this.mStreet.getText().toString();
        String city = Utils.capitalizeFirstLetterOfASingleWord(this.mCity.getText().toString());
        Integer streetNb = getNumber(this.mStreetNb.getText().toString());
        Integer postalCode = getNumber(this.mPostalCode.getText().toString());

        String address = Utils.buildAddress(streetNb, this.mStreet.getText().toString(), postalCode, this.mCity.getText().toString());
        Address flatAddress = getAddressFromSearchString(address);

        Double latitude = (flatAddress == null) ? 0 : flatAddress.getLatitude();
        Double longitude = (flatAddress == null) ? 0 : flatAddress.getLongitude();

        Flat flat = new Flat(
                picPath,
                summary,
                description,
                flatType,
                price,
                surface,
                room,
                bedroom,
                bathroom,
                streetNb,
                street,
                postalCode,
                city,
                latitude,
                longitude,
                mSchool.isChecked(),
                mPostOffice.isChecked(),
                mRestaurant.isChecked(),
                mTheater.isChecked(),
                mShop.isChecked(),
                AGENT_ID);

        this.mFlatViewModel.createFlat(this, flat, mFlatPicList, mIsTablet);
        cleanForm();
    }

}
