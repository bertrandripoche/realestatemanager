package com.openclassrooms.realestatemanager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.realestatemanager.addFlat.FlatViewModel;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Flat;
import com.openclassrooms.realestatemanager.model.Pic;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class AddFlatActivity extends AppCompatActivity  {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.flat_photos_list_recycler_view) RecyclerView mFlatPhotosRecyclerView;
    @BindView(R.id.btn_add_flat) FloatingActionButton mBtnAddFlat;
    @BindView(R.id.edit_photo_caption) EditText mCaption;
    @BindView(R.id.btn_add_photo) ImageButton mBtnCaption;
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

    private FlatViewModel mFlatViewModel;
    private Long mFlatId;
    private Uri uriImageSelected;
    private Uri mPhotoURI;
    String mCurrentPhotoPath;
    private String mUserChoosenTask;
    private ArrayList<Pic> mFlatPicList = new ArrayList();
    private FlatPicAdapter mAdapter;

    private int mSelectedFlat = -1;
    private static int AGENT_ID = 0;
    final String FLATID = "flatId";
    final String SELECTEDFLAT = "selectedFlat";
    private static final int REQUEST_CAMERA_TAKE_PICTURE = 0;
    private static final int REQUEST_SELECT_PIC_GALLERY = 1;
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int RC_IMAGE_PERMS = 100;

    public AddFlatActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flat);
        ButterKnife.bind(this);

        this.configureToolbar();
        this.configureViewModel();
        this.configureSpinners();
        this.configureTextWatchers();
        this.checkRecyclerView();

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
        Intent intent = new Intent(AddFlatActivity.this,MainActivity.class);
        Bundle b = new Bundle();
        if (mFlatId != null) b.putLong(FLATID, mFlatId);
        b.putInt(SELECTEDFLAT, mSelectedFlat);
        intent.putExtras(b);

        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("La liste avant rotation : " + mFlatPicList);
        outState.putParcelableArrayList("flatPicList", mFlatPicList);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mFlatPicList = savedInstanceState.getParcelableArrayList("flatPicList");
        System.out.println("La liste après rotation : " +mFlatPicList + " - " + mFlatPicList.size() +" éléments");
        if (mFlatPicList.size() != 0) checkRecyclerView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponse(requestCode, resultCode, data);
    }

    private void handleResponse(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_SELECT_PIC_GALLERY || requestCode == REQUEST_CAMERA_TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {

                if (requestCode == REQUEST_SELECT_PIC_GALLERY) {
                    uriImageSelected = data.getData();
                    String mSelectedImagePath = getRealPathFromURI(uriImageSelected);
                }

                if (requestCode == REQUEST_CAMERA_TAKE_PICTURE) {
                    uriImageSelected = mPhotoURI;
                    String mSelectedImagePath = mCurrentPhotoPath;
                }

                Pic pic = new Pic(uriImageSelected, mCaption.getText().toString(), 0);
                mFlatPicList.add(pic);

                Toast.makeText(this, R.string.picture_saved, Toast.LENGTH_SHORT).show();
                mCaption.setText("");
                checkRecyclerView();
            } else {
                Toast.makeText(this, R.string.no_picture_selected, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getRealPathFromURI (Uri contentUri) {
        String path = null;
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = this.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    private void checkRecyclerView() {
        if (mFlatPicList == null || mFlatPicList.size() == 0) mFlatPhotosRecyclerView.setVisibility(View.GONE);
        else {
            mFlatPhotosRecyclerView.setVisibility(View.VISIBLE);
            this.configureRecyclerView();
        }
    }

    private void configureRecyclerView() {
        mAdapter = new FlatPicAdapter(mFlatPicList);

        mFlatPhotosRecyclerView.setHasFixedSize(true);
        mFlatPhotosRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        mFlatPhotosRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void configureSpinners() {
        ArrayAdapter adapterSpinnerFlatType = ArrayAdapter.createFromResource(this, R.array.flat_type, R.layout.spinner_item);
        adapterSpinnerFlatType.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mFlatType.setAdapter(adapterSpinnerFlatType);
        ArrayAdapter adapterSpinnerFlatAgent = ArrayAdapter.createFromResource(this, R.array.flat_agent, R.layout.spinner_item);
        adapterSpinnerFlatAgent.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mFlatAgent.setAdapter(adapterSpinnerFlatAgent);
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
            if (isFormValid()) enableAddFlatButton();
            else disableAddFlatButton();

            // Enable-disable Photo Button
            if (mCaption.getText().toString().equals("")) disablePhotoButton();
            else enablePhotoButton();
        }
    };

    private void configureToolbar(){
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        Objects.requireNonNull(ab).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(ab).setTitle(R.string.addFlat);
    }

    private void configureTextWatchers() {
        mCaption.addTextChangedListener(textWatcher);
        mSummary.addTextChangedListener(textWatcher);
        mDescription.addTextChangedListener(textWatcher);
        mSurface.addTextChangedListener(textWatcher);
        mPrice.addTextChangedListener(textWatcher);
        mCity.addTextChangedListener(textWatcher);
    }

    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mFlatViewModel = ViewModelProviders.of(this, mViewModelFactory).get(FlatViewModel.class);
        this.mFlatViewModel.init(AGENT_ID);
    }

    private void createFlat(){
        Integer price = getNumber(this.mPrice.getText().toString());
        Integer surface = getNumber(this.mSurface.getText().toString());
        Integer room = getNumber(this.mRoomNb.getText().toString());
        Integer bedroom = getNumber(this.mBedroomNb.getText().toString());
        Integer bathroom = getNumber(this.mBathroomNb.getText().toString());
        Integer streetNb = getNumber(this.mStreetNb.getText().toString());
        Integer postalCode = getNumber(this.mPostalCode.getText().toString());

        String address = Utils.buildAddress(streetNb, this.mStreet.getText().toString(), postalCode, this.mCity.getText().toString());
        Address flatAddress = getAddressFromSearchString(address);

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
                flatAddress.getLatitude(),
                flatAddress.getLongitude(),
                mSchool.isChecked(),
                mPostOffice.isChecked(),
                mRestaurant.isChecked(),
                mTheater.isChecked(),
                mShop.isChecked(),
                AGENT_ID);

        this.mFlatViewModel.createFlat(flat);

        if (mFlatPicList.size() == 0) cleanForm();
        else getLastFlatId();
    }

    private void getLastFlatId(){
        this.mFlatViewModel.getLastFlatId().observe(this, this::savePics);
    }

    private void savePics(Integer flatId) {
        for (Pic pic : mFlatPicList) {
            pic.setFlatId(flatId);
            this.mFlatViewModel.createPic(pic);
        }
        cleanForm();
    }

    private void cleanForm() {
        Toast.makeText(getApplicationContext(), R.string.flat_saved, Toast.LENGTH_LONG).show();
        emptyFields();
        disableAddFlatButton();
        mFlatPicList = new ArrayList();
        checkRecyclerView();
    }

    private void emptyFields() {
        mFlatType.setSelection(0,true);
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

    private void enableAddFlatButton() {
        mBtnAddFlat.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.pink)));
    }

    private void disableAddFlatButton() {
        mBtnAddFlat.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_grey)));
    }

    private void enablePhotoButton() {
        mBtnCaption.setImageResource(R.drawable.ic_add_photo);
    }

    private void disablePhotoButton() {
        mBtnCaption.setImageResource(R.drawable.ic_add_photo_off);
    }

    private boolean isFormValid() {
        return !mSummary.getText().toString().equals("") && !mDescription.getText().toString().equals("")  && !mSurface.getText().toString().equals("")  && !mPrice.getText().toString().equals("")  && !mCity.getText().toString().equals("");
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

    private void selectImage(String caption) {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(AddFlatActivity.this, R.style.AlertDialogTheme);
        builder.setTitle(getString(R.string.add_photo, caption));

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    mUserChoosenTask = "Take Photo";
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    mUserChoosenTask = "Choose from Library";
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                System.out.println("Error in cameraIntent");
            }

            if (photoFile != null) {
                mPhotoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoURI);
                startActivityForResult(intent, REQUEST_CAMERA_TAKE_PICTURE);
            }
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_SELECT_PIC_GALLERY);
    }

    private File createImageFile() throws IOException {
        // Create an image file name


        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
