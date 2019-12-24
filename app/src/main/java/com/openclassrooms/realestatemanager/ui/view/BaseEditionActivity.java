package com.openclassrooms.realestatemanager.ui.view;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.Pic;
import com.openclassrooms.realestatemanager.ui.recyclerview.FlatPicAdapter;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodel.FlatViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class BaseEditionActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.flat_photos_list_recycler_view) RecyclerView mFlatPhotosRecyclerView;
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

    protected FlatViewModel mFlatViewModel;
    protected Long mFlatId;

    protected Uri mUriImageSelected;
    protected Uri mPhotoURI;
    protected String mCurrentPhotoPath;
    protected String mSelectedImagePath;
    protected ArrayList<Pic> mFlatPicList = new ArrayList();
    protected FlatPicAdapter mAdapter;
    protected View mPopupPhotoChoiceView = null;
    protected TextView mPopupPhotoChoiceTitle;
    protected Button mBtnGallery;
    protected Button mBtnTakePicture;
    protected Button mBtnCancel;

    protected AlertDialog mAlertDialog;
    protected boolean mIsDialogDisplayed = false;

    protected int mSelectedFlat = -1;
    protected static int AGENT_ID = 0;
    protected final String FLATID = "flatId";
    protected final String SELECTEDFLAT = "selectedFlat";
    protected static final int REQUEST_CAMERA_TAKE_PICTURE = 0;
    protected static final int REQUEST_SELECT_PIC_GALLERY = 1;
    protected static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    protected static final int RC_IMAGE_PERMS = 100;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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

    protected void configureSpinners() {
        String[] dataForSpinner = Utils.createDataForFlatTypeSpinners(this);
        ArrayAdapter adapterSpinnerFlatType = new ArrayAdapter<String>
                (this, R.layout.spinner_item, dataForSpinner);
        adapterSpinnerFlatType.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mFlatType.setAdapter(adapterSpinnerFlatType);
        ArrayAdapter adapterSpinnerFlatAgent = ArrayAdapter.createFromResource(this, R.array.flat_agent, R.layout.spinner_item);
        adapterSpinnerFlatAgent.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mFlatAgent.setAdapter(adapterSpinnerFlatAgent);
    }

    protected void configureToolbar(){
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        Objects.requireNonNull(ab).setDisplayHomeAsUpEnabled(true);
    }

    protected void enablePhotoButton() {
        mBtnCaption.setImageResource(R.drawable.ic_add_photo);
    }

    protected void disablePhotoButton() {
        mBtnCaption.setImageResource(R.drawable.ic_add_photo_off);
    }

    protected boolean isFormValid() {
        return !mSummary.getText().toString().equals("") && !mDescription.getText().toString().equals("")  && !mSurface.getText().toString().equals("")  && !mPrice.getText().toString().equals("")  && !mCity.getText().toString().equals("");
    }

    protected void selectImage(String caption) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);

        initPopupViewControls();
        alertDialogBuilder.setView(mPopupPhotoChoiceView);
        mPopupPhotoChoiceTitle.setText(getString(R.string.add_photo, caption));

        mAlertDialog = alertDialogBuilder.create();
        mAlertDialog.show();
        mIsDialogDisplayed = true;

        mBtnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog.cancel();
                mIsDialogDisplayed = false;

                galleryIntent();
            }
        });

        mBtnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog.cancel();
                mIsDialogDisplayed = false;

                cameraIntent();
            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsDialogDisplayed = false;
                mAlertDialog.cancel();
            }
        });
    }

    protected void initPopupViewControls() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);

        mPopupPhotoChoiceView = layoutInflater.inflate(R.layout.popup_photo_choice, null);

        mPopupPhotoChoiceTitle = mPopupPhotoChoiceView.findViewById(R.id.popup_photo_choice_title);
        mBtnGallery  = mPopupPhotoChoiceView.findViewById(R.id.btn_gallery);
        mBtnTakePicture = mPopupPhotoChoiceView.findViewById(R.id.btn_take_picture);
        mBtnCancel= mPopupPhotoChoiceView.findViewById(R.id.btn_cancel);
    }

    protected void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_SELECT_PIC_GALLERY);
    }

    protected void cameraIntent() {

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
                        "com.openclassrooms.realestatemanager.fileprovider",
                        photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoURI);
                startActivityForResult(intent, REQUEST_CAMERA_TAKE_PICTURE);
            }
        }
    }

    protected File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }
}
