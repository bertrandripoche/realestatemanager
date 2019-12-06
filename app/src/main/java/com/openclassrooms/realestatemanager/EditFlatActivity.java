package com.openclassrooms.realestatemanager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

public class EditFlatActivity extends AppCompatActivity implements FlatPicAdapter.Listener {
    @BindView(R.id.flat_photos_list_recycler_view) RecyclerView mFlatPhotosRecyclerView;
    @BindView(R.id.edit_photo_caption) EditText mCaption;
    @BindView(R.id.btn_add_photo) ImageButton mBtnCaption;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.btn_save_flat) FloatingActionButton mBtnEditFlat;
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
    private ArrayList<Pic> mFlatPicList = new ArrayList();
    private FlatPicAdapter mAdapter;
    private Uri uriImageSelected;
    private Uri mPhotoURI;
    private String mCurrentPhotoPath;
    private String mSelectedImagePath;
    private boolean mIsModified = false;
    private boolean isRotated = false;

    private static int AGENT_ID = 0;
    private static final int REQUEST_CAMERA_TAKE_PICTURE = 0;
    private static final int REQUEST_SELECT_PIC_GALLERY = 1;
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int RC_IMAGE_PERMS = 100;
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
        isRotated = true;
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

    @Override
    public void onClickDeleteButton(int position) {
        Pic pic = mAdapter.getFlatPic(position);
        Toast.makeText(this, R.string.warning_removal_not_saved, Toast.LENGTH_SHORT).show();
        mFlatPicList.remove(pic);
        mIsModified = true;
        mAdapter.notifyDataSetChanged();
        if (mFlatPicList.size() == 0) mFlatPhotosRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponse(requestCode, resultCode, data);
    }

    private void handleResponse(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_SELECT_PIC_GALLERY || requestCode == REQUEST_CAMERA_TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                mSelectedImagePath = "";
                if (requestCode == REQUEST_SELECT_PIC_GALLERY) {
                    uriImageSelected = data.getData();
                    mSelectedImagePath = getRealPathFromURI(uriImageSelected);
                } else {
                    uriImageSelected = mPhotoURI;
                    mSelectedImagePath = mCurrentPhotoPath;
                }

                Pic pic = new Pic(uriImageSelected, mSelectedImagePath, mCaption.getText().toString(), 0);
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

    private void selectImage(String caption) {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(EditFlatActivity.this, R.style.AlertDialogTheme);
        builder.setTitle(getString(R.string.add_photo, caption));

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_SELECT_PIC_GALLERY);
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
                        "com.openclassrooms.realestatemanager.fileprovider",
                        photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoURI);
                startActivityForResult(intent, REQUEST_CAMERA_TAKE_PICTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
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
            if (isFormValid()) enableAddFlatButton();
            else disableAddFlatButton();

            // Enable-disable Photo Button
            if (mCaption.getText().toString().equals("")) disablePhotoButton();
            else enablePhotoButton();
        }
    };

    private void enableAddFlatButton() {
        mBtnEditFlat.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.pink)));
    }

    private void disableAddFlatButton() {
        mBtnEditFlat.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_grey)));
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
        Long flatId = -1L;
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
        if (mFlat != null && !isRotated) {
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

        if (!isRotated) getPics(mFlat.getId());
    }

    private void getPics(int flatId) {
        this.mFlatViewModel.getPicsFromFlat(flatId).observe(this, this::displayPics);
    }

    private void displayPics(List<Pic> pics) {
        if (pics.size() != 0) {
            mFlatPicList = (ArrayList) pics;
            mFlatPhotosRecyclerView.setVisibility(View.VISIBLE);
            this.configureRecyclerView();
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

        mFlat.setSummary(Utils.capitalizeFirstLetterOfASingleWord(this.mSummary.getText().toString()));
        mFlat.setDescription(Utils.capitalizeFirstLetterOfASingleWord(this.mDescription.getText().toString()));
        mFlat.setType(this.mFlatType.getSelectedItem().toString());
        mFlat.setPrice(price);
        mFlat.setSurface(surface);
        mFlat.setRoom(room);
        mFlat.setBedroom(bedroom);
        mFlat.setBathroom(bathroom);
        mFlat.setNumberAddress(streetNb);
        mFlat.setStreetAddress(this.mStreet.getText().toString());
        mFlat.setPostalCodeAddress(postalCode);
        mFlat.setCityAddress(Utils.capitalizeFirstLetterOfASingleWord(this.mCity.getText().toString()));
        mFlat.setLatitude(flatAddress.getLatitude());
        mFlat.setLongitude(flatAddress.getLongitude());
        mFlat.setSchool(mSchool.isChecked());
        mFlat.setPostOffice(mPostOffice.isChecked());
        mFlat.setRestaurant(mRestaurant.isChecked());
        mFlat.setTheater(mTheater.isChecked());
        mFlat.setShop(mShop.isChecked());

        if (mIsModified) this.mFlatViewModel.updateFlat(mFlat, mFlatPicList);
        else this.mFlatViewModel.updateFlat(mFlat);

        mIsModified = false;
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
