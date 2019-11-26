package com.openclassrooms.realestatemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openclassrooms.realestatemanager.addFlat.FlatViewModel;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Flat;

import java.util.List;

import butterknife.ButterKnife;

public class MapActivity extends BaseActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {
    private GoogleMap mMap;
    private List<Flat> mFlatList;
    private FlatViewModel mFlatViewModel;
    private static int AGENT_ID = 0;
    private static final int DEFAULT_ZOOM = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        this.configureToolbar();
        this.configureViewModel();
        this.getFlats();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Flat flat = (Flat) marker.getTag();

        Bundle args = new Bundle();
        args.putLong(FLATID, flat.getId());
        Intent intent = new Intent(this, FlatDetailActivity.class);
        intent.putExtras(args);
        startActivity(intent);

        return false;
    }


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
                if (isFirst)
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

}
