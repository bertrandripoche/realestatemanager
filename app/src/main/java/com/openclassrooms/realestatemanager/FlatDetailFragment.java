package com.openclassrooms.realestatemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.openclassrooms.realestatemanager.addFlat.FlatViewModel;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Flat;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.biubiubiu.justifytext.library.JustifyTextView;


public class FlatDetailFragment extends Fragment {

    private List<Flat> mFlatList;
    private Flat mFlat;
    private Long mFlatId;
    private FlatDetailActivity mFlatDetailActivity;
    private FlatViewModel mFlatViewModel;
    private static int AGENT_ID = 0;

    @BindView(R.id.summary) AppCompatTextView mSummary;
    @BindView(R.id.flat_description) JustifyTextView mDescription;
    @BindView(R.id.price) AppCompatTextView mPrice;
    @BindView(R.id.city) AppCompatTextView mCity;
    @BindView(R.id.type) AppCompatTextView mType;
    @BindView(R.id.surface) AppCompatTextView mSurface;
    @BindView(R.id.room_nb) AppCompatTextView mRoomNb;
    @BindView(R.id.bathroom_nb) AppCompatTextView mBathroomNb;
    @BindView(R.id.bedroom_nb) AppCompatTextView mBedroomNb;
    @BindView(R.id.sale_agent) AppCompatTextView mSaleAgent;
    @BindView(R.id.address) AppCompatTextView mAddress;
    @BindView(R.id.school) AppCompatImageView mSchool;
    @BindView(R.id.post) AppCompatImageView mPost;
    @BindView(R.id.restaurant) AppCompatImageView mRestaurant;
    @BindView(R.id.theater) AppCompatImageView mTheater;
    @BindView(R.id.shop) AppCompatImageView mShop;
    @BindView(R.id.available_from) AppCompatTextView mAvailable_from;

    public FlatDetailFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flat_detail, container, false);
        ButterKnife.bind(this, view);
        mFlatId = getFlatId();

        configureViewModel();
        getFlat(mFlatId);

        return view;
    }

    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(getContext());
        this.mFlatViewModel = ViewModelProviders.of(this, mViewModelFactory).get(FlatViewModel.class);
        this.mFlatViewModel.init(AGENT_ID);
    }

    private Long getFlatId() {
        Long flatId;
        Bundle b = getArguments();
        if (b == null) {
            mFlatDetailActivity = (FlatDetailActivity) getActivity();
            flatId = mFlatDetailActivity.mFlatId;
        } else {
            flatId = b.getLong("flatId");
        }
        return flatId;
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

            String address = "";
            if (mFlat.getNumberAddress() != null && mFlat.getStreetAddress() != null) address = String.valueOf(mFlat.getNumberAddress()) + ", " + mFlat.getStreetAddress();
            if (mFlat.getNumberAddress() == null && mFlat.getStreetAddress() != null) address = mFlat.getStreetAddress();
            if (mFlat.getPostalCodeAddress() != null && address != null) address = address + "\n" + mFlat.getPostalCodeAddress() + " " + mFlat.getCityAddress();
            else if (mFlat.getPostalCodeAddress() == null && address != null) address = address + "\n" + mFlat.getCityAddress();
            else address = mFlat.getCityAddress();
            mAddress.setText(address);

            mAvailable_from.setText(getString(R.string.available_date, mFlat.getAvailableDate()));
        }
    }

    private void getFlat(long flatId) {
        this.mFlatViewModel.getFlatFromId(flatId).observe(this, this::populateData);
    }

}
