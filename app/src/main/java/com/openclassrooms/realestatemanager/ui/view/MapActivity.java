package com.openclassrooms.realestatemanager.ui.view;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.viewmodel.FlatViewModel;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Flat;
import com.openclassrooms.realestatemanager.ui.customview.CustomInfoAdapter;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import java.util.List;

import butterknife.ButterKnife;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapActivity extends BaseActivity implements GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener, OnMapReadyCallback {
    private GoogleMap mMap;
    private List<Flat> mFlatList;
    private FlatViewModel mFlatViewModel;
    private static final String TAG = "Map Activity";

    private SupportMapFragment mMapFragment;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private LatLng mLatLngLastKnownLocation;
    protected FusedLocationProviderClient mFusedLocationProviderClient;

    private static int AGENT_ID = 0;
    private static final int DEFAULT_ZOOM = 11;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String PERMS = ACCESS_FINE_LOCATION;
    private static final int RC_ACCESS_PERMS = 100;
    private boolean mIsTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        mIsTablet = getResources().getBoolean(R.bool.isTablet);

        this.configureToolbar();
        this.configureViewModel();
        this.getFlats();

        SupportMapFragment mMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        moveCompassButton(mMapFragment.getView());

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMap != null) {
            mMap.clear();
            this.getFlats();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        hideEditBtn();
        hideMapBtn();
        hideSearchBtn();
        displayListBtn();
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);

        setMyLocationEnabled();
        getDeviceLocation();
        mMap.setOnMyLocationClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_ACCESS_PERMS)
    private void setMyLocationEnabled() {
        if (EasyPermissions.hasPermissions(this, PERMS)) {
            try {
                if (mMap != null)
                    mMap.setMyLocationEnabled(true);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.permissions_issue), RC_ACCESS_PERMS, PERMS);
        }
    }

    @AfterPermissionGranted(RC_ACCESS_PERMS)
    private void getDeviceLocation() {
        if (EasyPermissions.hasPermissions(this, PERMS)) {
            try {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Location successful.");
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mLatLngLastKnownLocation = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLngLastKnownLocation, DEFAULT_ZOOM));
                            }
                        }
                    }
                });
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.permissions_issue), RC_ACCESS_PERMS, PERMS);
        }
    }

    /**
     * Configure the viewModel
     */
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mFlatViewModel = ViewModelProviders.of(this, mViewModelFactory).get(FlatViewModel.class);
        this.mFlatViewModel.init(AGENT_ID);
    }

    /**
     * Get the flats from the database
     */
    private void getFlats() {
        this.mFlatViewModel.getFlats().observe(this, this::updateFlatsList);
    }

    /**
     * Update flat list
     * @param flats the new list of flats
     */
    private void updateFlatsList(List<Flat> flats){
        mFlatList = flats;

        boolean isFirst = true;
        for (Flat flat: mFlatList) {
            if (!flat.isSold() && flat.getLatitude() != null && flat.getLongitude() != null) {
                LatLng latLng = new LatLng(flat.getLatitude(), flat.getLongitude());
                String price = getResources().getString(R.string.euro, flat.getPrice());
                Marker myMarker = mMap.addMarker(new MarkerOptions().position(latLng).snippet(price).title(flat.getSummary()));
                myMarker.setTag(flat);
                if (isFirst && mLastKnownLocation == null)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
                isFirst = false;
            }
        }

        mMap.setInfoWindowAdapter(new CustomInfoAdapter(this));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                // Retrieve the data from the marker.
                Flat flat = (Flat) marker.getTag();

                if (mIsTablet) {
                    Bundle args = new Bundle();
                    args.putLong(FLATID, flat.getId());
                    args.putInt(SELECTEDFLAT, flat.getId());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtras(args);
                    startActivity(intent);
                } else {
                    Bundle args = new Bundle();
                    args.putLong(FLATID, flat.getId());
                    Intent intent = new Intent(getApplicationContext(), FlatDetailActivity.class);
                    intent.putExtras(args);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Move the Compass button
     * @param mapView is the view on which the button needs to be moved
     */
    private void moveCompassButton(View mapView) {
        try {
            assert mapView != null; // skip this if the mapView has not been set yet
            View view = mapView.findViewWithTag("GoogleMapMyLocationButton");

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            layoutParams.setMargins(0, 150, 20, 0);

            view.setLayoutParams(layoutParams);
            view.setBackgroundColor(getResources().getColor(R.color.white));
        } catch (Exception ex) {
            Log.e(TAG, "MoveCompassButton() - failed: " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }
}
