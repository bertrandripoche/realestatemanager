package com.openclassrooms.realestatemanager.ui.view;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.location.Address;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.utils.UriTypeConverter;
import com.openclassrooms.realestatemanager.viewmodel.FlatViewModel;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Flat;
import com.openclassrooms.realestatemanager.model.Pic;
import com.openclassrooms.realestatemanager.ui.recyclerview.FlatPicAdapter;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class EditFlatActivity extends BaseEditionActivity implements FlatPicAdapter.Listener, FloatButtonActions {
    @BindView(R.id.btn_save_flat) FloatingActionButton mBtnEditFlat;

    private Flat mFlat;
    private boolean mIsModified = false;
    private boolean isRotated = false;

    public EditFlatActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_flat);
        ButterKnife.bind(this);
        mFlatId = getFlatId();
        mSelectedFlat = getSelectedFlat();

        configureToolbar();
        configureTextWatchers();
        configureSpinners();
        configureViewModel();
        checkRecyclerView();

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

        Intent intent = new Intent(EditFlatActivity.this, MainActivity.class);
        Bundle b = new Bundle();
        b.putLong(FLATID, mFlatId);
        b.putInt(SELECTEDFLAT, mSelectedFlat);
        intent.putExtras(b);

        startActivity(intent);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("flatPicList", mFlatPicList);
        outState.putBoolean("picsHaveBeenModified", mIsModified);
        outState.putString("photoTaken", mCurrentPhotoPath);
        if (!this.mSummary.getText().toString().equals("")) outState.putString("summary", this.mSummary.getText().toString());
        if (!this.mDescription.getText().toString().equals("")) outState.putString("description", this.mDescription.getText().toString());
        if (!this.mFlatType.getSelectedItem().toString().equals("")) outState.putString("flatType", this.mFlatType.getSelectedItem().toString());
        if (!this.mPrice.getText().toString().equals("")) outState.putString("price", this.mPrice.getText().toString());
        if (!this.mSurface.getText().toString().equals("")) outState.putString("surface", this.mSurface.getText().toString());
        if (!this.mRoomNb.getText().toString().equals("")) outState.putString("room", this.mRoomNb.getText().toString());
        if (!this.mBedroomNb.getText().toString().equals("")) outState.putString("bedroom", this.mBedroomNb.getText().toString());
        if (!this.mBathroomNb.getText().toString().equals("")) outState.putString("bathroom", this.mBathroomNb.getText().toString());
        if (!this.mStreetNb.getText().toString().equals("")) outState.putString("streetNb", this.mStreetNb.getText().toString());
        if (!this.mStreet.getText().toString().equals("")) outState.putString("street", this.mStreet.getText().toString());
        if (!this.mPostalCode.getText().toString().equals("")) outState.putString("postalCode", this.mPostalCode.getText().toString());
        if (!this.mCity.getText().toString().equals("")) outState.putString("city", this.mCity.getText().toString());
        outState.putBoolean("school", mSchool.isChecked());
        outState.putBoolean("postOffice", mPostOffice.isChecked());
        outState.putBoolean("restaurant", mRestaurant.isChecked());
        outState.putBoolean("theater", mTheater.isChecked());
        outState.putBoolean("shop", mShop.isChecked());
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
        mIsModified = savedInstanceState.getBoolean("picsHaveBeenModified");
        mCurrentPhotoPath = savedInstanceState.getString("photoTaken");
        this.mSummary.setText(savedInstanceState.getString("summary"));
        this.mDescription.setText(savedInstanceState.getString("description"));
        setSpinner(mFlatType, savedInstanceState.getString("flatType"));
        this.mPrice.setText(savedInstanceState.getString("price"));
        this.mSurface.setText(savedInstanceState.getString("surface"));
        this.mRoomNb.setText(savedInstanceState.getString("room"));
        this.mBedroomNb.setText(savedInstanceState.getString("bedroom"));
        this.mBathroomNb.setText(savedInstanceState.getString("bathroom"));
        this.mStreetNb.setText(savedInstanceState.getString("streetNb"));
        this.mStreet.setText(savedInstanceState.getString("street"));
        this.mPostalCode.setText(savedInstanceState.getString("postalCode"));
        this.mCity.setText(savedInstanceState.getString("city"));
        this.mSchool.setChecked(savedInstanceState.getBoolean("school"));
        this.mPostOffice.setChecked(savedInstanceState.getBoolean("postOffice"));
        this.mRestaurant.setChecked(savedInstanceState.getBoolean("restaurant"));
        this.mTheater.setChecked(savedInstanceState.getBoolean("theater"));
        this.mShop.setChecked(savedInstanceState.getBoolean("shop"));

        if (mFlatPicList.size() != 0) checkRecyclerView();

        if (savedInstanceState.getBoolean("dialogDisplayed")) {
            selectImage(savedInstanceState.getString("caption"));
        }
        isRotated = true;

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

    /**
     * Delete a flat pic
     * @param position is the position of the item in the recyclerView
     */
    @Override
    public void onClickDeleteButton(int position) {
        Pic pic = mAdapter.getFlatPic(position);
        Toast.makeText(this, R.string.warning_removal_not_saved, Toast.LENGTH_SHORT).show();
        mFlatPicList.remove(pic);
        mIsModified = true;
        mAdapter.notifyDataSetChanged();
        if (mFlatPicList.size() == 0) mFlatPhotosRecyclerView.setVisibility(View.GONE);
    }

    /**
     * Determine action when save button clicked
     */
    @OnClick(R.id.btn_save_flat)
    public void onClickAddButton() {
        this.updateFlat();
        Toast.makeText(getApplicationContext(), R.string.flat_updated, Toast.LENGTH_LONG).show();
    }

    /**
     * Trigger the selection image action
     */
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

    /**
     * To handle result of picture/galery selection
     * @param requestCode is the code of the request to be handled
     * @param resultCode is the result code from the action
     * @param data is the data obtained by the action
     */
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
                mIsModified = true;

                Toast.makeText(this, R.string.picture_saved, Toast.LENGTH_SHORT).show();
                mCaption.setText("");
                checkRecyclerView();
            } else {
                Toast.makeText(this, R.string.no_picture_selected, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Method to check if there are some flat pics and display them if any
     */
    private void checkRecyclerView() {
        if (mFlatPicList == null || mFlatPicList.size() == 0) mFlatPhotosRecyclerView.setVisibility(View.GONE);
        else {
            mFlatPhotosRecyclerView.setVisibility(View.VISIBLE);
            if (mAdapter == null) this.configureRecyclerView();
            else mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Method to first configure the recyclerView
     */
    private void configureRecyclerView() {
        mAdapter = new FlatPicAdapter(mFlatPicList, this, true);

        mFlatPhotosRecyclerView.setHasFixedSize(true);
        mFlatPhotosRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        mFlatPhotosRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Method to configure the viewModel
     */
    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mFlatViewModel = ViewModelProviders.of(this, mViewModelFactory).get(FlatViewModel.class);
        this.mFlatViewModel.init(AGENT_ID);
    }

    /**
     * Method to configure the textWatchers on the fields which requires it
     */
    private void configureTextWatchers() {
        mCaption.addTextChangedListener(textWatcher);
        mSummary.addTextChangedListener(textWatcher);
        mDescription.addTextChangedListener(textWatcher);
        mSurface.addTextChangedListener(textWatcher);
        mPrice.addTextChangedListener(textWatcher);
        mCity.addTextChangedListener(textWatcher);
    }

    /**
     * Method to describe the actions to do on text writing
     */
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

    /**
     * Make the float button enabled
     */
    public void enableFloatButton() {
        mBtnEditFlat.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.pink)));
    }

    /**
     * Make the float button disabled
     */
    public void disableFloatButton() {
        mBtnEditFlat.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_grey)));
    }

    /**
     * Get flatid from bundle
     * @return a Long which is the flatId
     */
    protected Long getFlatId() {
        Long flatId = -1L;
        Intent i = getIntent();
        if (i != null) {
            flatId = i.getExtras().getLong(FLATID);
        }
        return flatId;
    }

    /**
     * Get the selected flat from bundle
     * @return an int representing the flat selected
     */
    protected int getSelectedFlat() {
        int selectedFlat = -1;
        Intent i = getIntent();
        if (i != null) {
            selectedFlat = i.getExtras().getInt(SELECTEDFLAT);
        }
        return selectedFlat;
    }

    /**
     * Get the data of flat from database
     * @param flatId
     */
    private void getFlat(long flatId) {
        this.mFlatViewModel.getFlatFromId(flatId).observe(this, this::populateData);
    }

    /**
     * Populate the Edit form with the data of the indicated flat
     * @param flat is the flat
     */
    private void populateData(Flat flat) {
        mFlat = flat;
        if (mFlat != null && !isRotated) {
            mSummary.setText(mFlat.getSummary());
            mDescription.setText(getString(R.string.description_text, mFlat.getDescription()));
            if (mFlat.getPrice() != null) mPrice.setText(String.valueOf(mFlat.getPrice()));

            mCity.setText(mFlat.getCityAddress());
            if (mFlat.getSurface() != null) mSurface.setText(String.valueOf(mFlat.getSurface()));

            String flatType = Utils.getStringFromFlatTypeSpinners(this, mFlat.getType());
            setSpinner(mFlatType, flatType);

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

            if (!isRotated) getPics(mFlat.getId());
        }
    }

    /**
     * Get pics from the database
     * @param flatId
     */
    private void getPics(int flatId) {
        this.mFlatViewModel.getPicsFromFlat(flatId).observe(this, this::displayPics);
    }

    /**
     * Displays the pics in a recyclerView
     * @param pics
     */
    private void displayPics(List<Pic> pics) {
        if (pics.size() != 0) {
            mFlatPicList = (ArrayList) pics;
            mFlatPhotosRecyclerView.setVisibility(View.VISIBLE);
            this.configureRecyclerView();
        }
    }

    /**
     * Set the spinner to a specified position
     * @param spinner is the spinner to set
     * @param value is the value to set
     */
    private void setSpinner(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        int spinnerPosition = adapter.getPosition(value);
        spinner.setSelection(spinnerPosition);
    }

    /**
     * Update the modified flat in database
     */
    private void updateFlat() {
        Integer price = getNumber(this.mPrice.getText().toString());
        Integer surface = getNumber(this.mSurface.getText().toString());
        Integer room = getNumber(this.mRoomNb.getText().toString());
        Integer bedroom = getNumber(this.mBedroomNb.getText().toString());
        Integer bathroom = getNumber(this.mBathroomNb.getText().toString());
        Integer streetNb = getNumber(this.mStreetNb.getText().toString());
        Integer postalCode = getNumber(this.mPostalCode.getText().toString());

        String address = Utils.buildAddress(streetNb, this.mStreet.getText().toString(), postalCode, this.mCity.getText().toString());
        Address flatAddress = getAddressFromAddressString(address);

        Double latitude = (flatAddress == null) ? 0 : flatAddress.getLatitude();
        Double longitude = (flatAddress == null) ? 0 : flatAddress.getLongitude();

        mFlat.setSummary(Utils.capitalizeFirstLetterOfASingleWord(this.mSummary.getText().toString()));
        mFlat.setDescription(Utils.capitalizeFirstLetterOfASingleWord(this.mDescription.getText().toString()));
        mFlat.setType(this.mFlatType.getSelectedItemPosition());
        mFlat.setPrice(price);
        mFlat.setSurface(surface);
        mFlat.setRoom(room);
        mFlat.setBedroom(bedroom);
        mFlat.setBathroom(bathroom);
        mFlat.setNumberAddress(streetNb);
        mFlat.setStreetAddress(this.mStreet.getText().toString());
        mFlat.setPostalCodeAddress(postalCode);
        mFlat.setCityAddress(Utils.capitalizeFirstLetterOfASingleWord(this.mCity.getText().toString()));
        mFlat.setLatitude(latitude);
        mFlat.setLongitude(longitude);
        mFlat.setSchool(mSchool.isChecked());
        mFlat.setPostOffice(mPostOffice.isChecked());
        mFlat.setRestaurant(mRestaurant.isChecked());
        mFlat.setTheater(mTheater.isChecked());
        mFlat.setShop(mShop.isChecked());

        if (mIsModified) this.mFlatViewModel.updateFlat(mFlat, mFlatPicList);
        else this.mFlatViewModel.updateFlat(mFlat);

        mIsModified = false;
    }

}
