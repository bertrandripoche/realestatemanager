package com.openclassrooms.realestatemanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.openclassrooms.realestatemanager.addFlat.FlatViewModel;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Flat;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import java.util.List;

import butterknife.ButterKnife;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapActivity extends BaseActivity implements GoogleMap.OnMarkerClickListener, GoogleMap.OnMyLocationButtonClickListener, OnMapReadyCallback {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
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
        displayListBtn();
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);

//        updateLocationUI();
        setMyLocationEnabled();
        getDeviceLocation();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        Flat flat = (Flat) marker.getTag();

        Bundle args = new Bundle();
        args.putLong(FLATID, flat.getId());
        Intent intent = new Intent(this, FlatDetailActivity.class);
        intent.putExtras(args);
        startActivity(intent);

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

//    private void enableMyLocation() {
//        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{ACCESS_FINE_LOCATION},
//                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
//        } else if (mMap != null) {
//            mMap.setMyLocationEnabled(true);
//            mMap.getUiSettings().setMapToolbarEnabled(true);
//        }
//    }

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
                                mLatLngLastKnownLocation =  new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
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

//        try {
//            if (mLocationPermissionGranted) {
//                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
//                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Location> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "Location successful.");
//                            mLastKnownLocation = task.getResult();
//                            mLatLngLastKnownLocation =  new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLngLastKnownLocation, DEFAULT_ZOOM));
//                        }
//                    }
//                });
//            }
//        } catch (SecurityException e)  {
//            Log.e("Exception: %s", e.getMessage());
//        }
    }

//    private void updateLocationUI() {
//        if (mMap == null) {
//            return;
//        }
//        try {
//            if (mLocationPermissionGranted) {
//                mMap.setMyLocationEnabled(true);
//            } else {
//                mMap.setMyLocationEnabled(false);
//                mLastKnownLocation = null;
//                getLocationPermission();
//            }
//        } catch (SecurityException e)  {
//            Log.e("Exception: %s", e.getMessage());
//        }
//    }

//    private void getLocationPermission() {
//        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
//                ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            mLocationPermissionGranted = true;
//            mMap.setMyLocationEnabled(true);
//        } else {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{ACCESS_FINE_LOCATION},
//                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
//        }
//    }

    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mFlatViewModel = ViewModelProviders.of(this, mViewModelFactory).get(FlatViewModel.class);
        this.mFlatViewModel.init(AGENT_ID);
    }

    private void getFlats() {
        this.mFlatViewModel.getFlats().observe(this, this::updateFlatsList);
    }

    private void updateFlatsList(List<Flat> flats){
        mFlatList = flats;

        boolean isFirst = true;
        for (Flat flat: mFlatList) {
            if (!flat.getIsSold()) {
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

                Bundle args = new Bundle();
                args.putLong(FLATID, flat.getId());
                Intent intent = new Intent(getApplicationContext(), FlatDetailActivity.class);
                intent.putExtras(args);
                startActivity(intent);
            }
        });
    }

    private void moveCompassButton(View mapView) {
        try {
            assert mapView != null; // skip this if the mapView has not been set yet
            View view = mapView.findViewWithTag("GoogleMapMyLocationButton");

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            layoutParams.setMargins(0, 100, 20, 0);

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
}
