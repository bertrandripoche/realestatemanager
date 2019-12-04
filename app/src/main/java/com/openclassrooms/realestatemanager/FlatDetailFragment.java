package com.openclassrooms.realestatemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openclassrooms.realestatemanager.addFlat.FlatViewModel;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Flat;
import com.openclassrooms.realestatemanager.model.Pic;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.biubiubiu.justifytext.library.JustifyTextView;


public class FlatDetailFragment extends Fragment  implements OnMapReadyCallback {

    @BindView(R.id.fragment_flat_detail_images_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.summary) AppCompatTextView mSummary;
    @BindView(R.id.flat_description) JustifyTextView mDescription;
    @BindView(R.id.price) AppCompatTextView mPrice;
    @BindView(R.id.city) AppCompatTextView mCity;
    @BindView(R.id.type) AppCompatTextView mType;
    @BindView(R.id.surface) AppCompatTextView mSurface;
    @BindView(R.id.room_nb) AppCompatTextView mRoomNb;
    @BindView(R.id.bathroom_nb) AppCompatTextView mBathroomNb;
    @BindView(R.id.bedroom_nb) AppCompatTextView mBedroomNb;
    @BindView(R.id.sale_agent) AppCompatTextView mAgent;
    @BindView(R.id.address) AppCompatTextView mAddress;
    @BindView(R.id.school) AppCompatImageView mSchool;
    @BindView(R.id.post) AppCompatImageView mPost;
    @BindView(R.id.restaurant) AppCompatImageView mRestaurant;
    @BindView(R.id.theater) AppCompatImageView mTheater;
    @BindView(R.id.shop) AppCompatImageView mShop;
    @BindView(R.id.available_from) AppCompatTextView mAvailable_from;
    @BindView(R.id.btn_sold) Button mBtnIsSold;

    private List<Flat> mFlatList;
    private Flat mFlat;
    private Long mFlatId;
    private FlatDetailActivity mFlatDetailActivity;
    private FlatViewModel mFlatViewModel;
    private List<Pic> mFlatPicList;
    private FlatPicAdapter mAdapter;
    private GoogleMap mMap;

    private static int AGENT_ID = 0;
    private static final String TAG = "Detail Fragment";
    private static final int STREET_LEVEL_ZOOM = 16;
    private static final int CITY_LEVEL_ZOOM = 11;

    public FlatDetailFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flat_detail, container, false);
        ButterKnife.bind(this, view);

        mFlatId = getFlatId();
        configureViewModel();
        getFlat(mFlatId);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        map.getUiSettings().setMapToolbarEnabled(false);
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    @OnClick(R.id.btn_sold)
    public void onClickSoldButton() {
        if (mFlat.isSold()) flatNotSold();
        else flatSold();
    }

    private void flatSold() {
        String soldDate = Utils.getTodayDate();
        mFlatViewModel.updateSoldFlat(mFlat.getId(), soldDate);
        disableSoldBtn();
    }

    private void flatNotSold() {
        mFlatViewModel.updateNotSoldFlat(mFlat.getId());
        enableSoldBtn();
    }

    private void enableSoldBtn() {
        mBtnIsSold.setBackground(getResources().getDrawable(R.drawable.rounded_button));
        mBtnIsSold.setText(R.string.is_it_sold);
        mAvailable_from.setText(getString(R.string.available_date, mFlat.getAvailableDate()));
    }

    private void disableSoldBtn() {
        mBtnIsSold.setBackground(getResources().getDrawable(R.drawable.rounded_button_off));
        mBtnIsSold.setText(R.string.sold);
        mAvailable_from.setText(getString(R.string.sold_date, mFlat.getSoldDate()));
    }

    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(getContext());
        this.mFlatViewModel = ViewModelProviders.of(this, mViewModelFactory).get(FlatViewModel.class);
        this.mFlatViewModel.init(AGENT_ID);
    }

    protected Long getFlatId() {
        Long flatId;
        final String FLATID = "flatId";
        Bundle b = getArguments();
        if (b == null) {
            mFlatDetailActivity = (FlatDetailActivity) getActivity();
            flatId = mFlatDetailActivity.mFlatId;
        } else {
            flatId = b.getLong(FLATID);
        }return flatId;
    }

    private void getFlat(long flatId) {
        this.mFlatViewModel.getFlatFromId(flatId).observe(this, this::populateData);
    }

    private void populateData(Flat flat) {
        mFlat = flat;
        if (mFlat != null) {

            mSummary.setText(mFlat.getSummary());
            mDescription.setText(getString(R.string.description_text, mFlat.getDescription()));
            mPrice.setText(getString(R.string.euro, mFlat.getPrice()));
            mType.setText(mFlat.getType());
            mCity.setText(mFlat.getCityAddress());
            mSurface.setText(getString(R.string.metre_carre, mFlat.getSurface()));

            if (mFlat.getRoom() == null) mRoomNb.setText("NC");
            else mRoomNb.setText(String.valueOf(mFlat.getRoom()));
            if (mFlat.getBathroom() == null) mBathroomNb.setText("NC");
            else mBathroomNb.setText(String.valueOf(mFlat.getBathroom()));
            if (mFlat.getBedroom() == null) mBedroomNb.setText("NC");
            else mBedroomNb.setText(String.valueOf(mFlat.getBedroom()));

            if (mFlat.isSchool()) mSchool.setImageResource(R.drawable.ic_ok);
            else mSchool.setImageResource(R.drawable.ic_ko);
            if (mFlat.isPostOffice()) mPost.setImageResource(R.drawable.ic_ok);
            else mPost.setImageResource(R.drawable.ic_ko);
            if (mFlat.isRestaurant()) mRestaurant.setImageResource(R.drawable.ic_ok);
            else mRestaurant.setImageResource(R.drawable.ic_ko);
            if (mFlat.isTheater()) mTheater.setImageResource(R.drawable.ic_ok);
            else mTheater.setImageResource(R.drawable.ic_ko);
            if (mFlat.isShop()) mShop.setImageResource(R.drawable.ic_ok);
            else mShop.setImageResource(R.drawable.ic_ko);

            if (mFlat.isSold()) disableSoldBtn();
            else enableSoldBtn();

            String address = Utils.buildAddress(mFlat.getNumberAddress(), mFlat.getStreetAddress(), mFlat.getPostalCodeAddress(), mFlat.getCityAddress());
            mAddress.setText(address);

            mAgent.setText("Pierre Poljack");

            if (mFlat.getLatitude() != null) {
                LatLng flatLatLng =  new LatLng(mFlat.getLatitude(), mFlat.getLongitude());
                int zoom = (mFlat.getStreetAddress() == null || mFlat.getStreetAddress().equals("")) ?
                        CITY_LEVEL_ZOOM : STREET_LEVEL_ZOOM;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(flatLatLng, zoom));
                mMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(flatLatLng.latitude, flatLatLng.longitude))
                                .title(mFlat.getSummary())
                ).setTag(mFlatId);
            }

            getPics(flat.getId());

        }
    }

    private void getPics(int flatId) {
        this.mFlatViewModel.getPicsFromFlat(flatId).observe(this, this::displayPics);
    }

    private void displayPics(List<Pic> pics) {
        if (pics.size() != 0) {
            mFlatPicList = pics;
            mRecyclerView.setVisibility(View.VISIBLE);
            this.configureRecyclerView();
        }
    }

    private void configureRecyclerView() {
        final int orientation = getResources().getInteger(R.integer.gallery_pic_orientation);

        mAdapter = new FlatPicAdapter(mFlatPicList, this::getPics ,false);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), orientation, false));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
