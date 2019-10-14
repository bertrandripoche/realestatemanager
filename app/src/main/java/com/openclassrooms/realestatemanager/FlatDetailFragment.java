package com.openclassrooms.realestatemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.openclassrooms.realestatemanager.model.Flat;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.Integer.parseInt;

public class FlatDetailFragment extends Fragment {

    List<Flat> mFlatList;
    Flat mFlat;
    FlatDetailActivity mFlatDetailActivity;

    @BindView(R.id.summary) AppCompatTextView mSummary;
    @BindView(R.id.price) AppCompatTextView mPrice;
    @BindView(R.id.city) AppCompatTextView mCity;
    @BindView(R.id.type) AppCompatTextView mType;
    @BindView(R.id.surface) AppCompatTextView mSurface;
    @BindView(R.id.room_nb) AppCompatTextView mRoomNb;
    @BindView(R.id.bathroom_nb) AppCompatTextView mBathroomNb;
    @BindView(R.id.bedroom_nb) AppCompatTextView mBedroomNb;
    @BindView(R.id.sale_status) AppCompatTextView mSaleStatus;
    @BindView(R.id.sale_agent) AppCompatTextView mSaleAgent;
    @BindView(R.id.address) AppCompatTextView mAddress;

    public FlatDetailFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flat_detail, container, false);
        ButterKnife.bind(this, view);
        String flatId = getFlatId();

        populategroceryList();
        populateData(flatId);

        return view;
    }

    private String getFlatId() {
        String flatId;
        Bundle b = getArguments();
        if (b == null) {
            mFlatDetailActivity = (FlatDetailActivity) getActivity();
            flatId = mFlatDetailActivity.mFlatId;
        } else {
            flatId = String.valueOf(b.getInt("flatId"));
        }
        return flatId;
    }

    private void populateData(String flatId) {
        mFlat = mFlatList.get(parseInt(flatId)-1);
        if (mFlat != null) {
            mSummary.setText(mFlat.getSummary());
            mPrice.setText(getString(R.string.euro, mFlat.getPrice()));
            mType.setText(mFlat.getType());
            mCity.setText(mFlat.getCityAddress());
            mSurface.setText(getString(R.string.metre_carre, mFlat.getSurface()));
            mRoomNb.setText(String.valueOf(mFlat.getRoom()));
            mBathroomNb.setText(String.valueOf(mFlat.getBathroom()));
            mBedroomNb.setText(String.valueOf(mFlat.getBedroom()));
        }
    }

    private void populategroceryList(){
        mFlatList = new ArrayList<>();
        Flat paradis = new Flat("Duplex au calme","Blablabla", "Duplex", 237000, 31, 1 , 1, 1, 15, "rue du Paradis", 75010, "Paris",0);
        Flat ecuries = new Flat("Urbain et stylé", "Blablabla", "Appartement", 480000, 67, 3 , 2, 1, 5, "rue des Petites Écuries", 75010, "Paris",0);
        Flat rivoli = new Flat( "Familial de luxe", "BLublublu", "Appartement", 1560000, 138, 6 , 4, 2, 121, "rue de Rivoli", 75001, "Paris",0);
        Flat trevise = new Flat( "Pour grande famille","Blibliblbi", "Appartement", 980000, 108, 4 , 2, 2, 34, "rue de Trévise", 75009, "Paris",0);
        Flat montorgueil = new Flat( "Moderne et classieux", "Tralala","Loft", 1860000, 210, 5 , 3, 2, 56, "rue de Montorgueil", 75009, "Paris",0);
        Flat bac = new Flat( "Idéal pour cocooning", "Tralala","Appartement", 520000, 58, 3 , 2, 1, 87, "rue du Bac", 75009, "Paris",0);
        Flat paradis2 = new Flat( "Charmant pied à terre", "Tralulul","Duplex", 237000, 31, 1 , 1, 1, 15, "rue du Paradis", 75010, "Paris",0);
        Flat ecuries2 = new Flat( "Un petit nid urbain", "Tralolola","Appartement", 480000, 67, 3 , 2, 1, 5, "rue des Petites Écuries", 75010, "Paris",0);
        Flat rivoli2 = new Flat( "De l'espace", "Tralala","Appartement", 1560000, 138, 6 , 4, 2, 121, "rue de Rivoli", 75001, "Paris",0);
        Flat trevise2 = new Flat("Pour ne plus se marcher dessus", "Tralala","Appartement", 980000, 108, 4 , 2, 2, 34, "rue de Trévise", 75009, "Paris",0);
        Flat montorgueil2 = new Flat( "En mode indus", "Dibidibu","Loft", 1860000, 210, 5 , 3, 2, 56, "rue de Montorgueil", 75009, "Paris",0);
        Flat bac2 = new Flat( "Charmant trois pièces", "Tatitotou","Appartement", 520000, 58, 3 , 2, 1, 87, "rue du Bac", 75009, "Paris",0);

        mFlatList.add(paradis);
        mFlatList.add(ecuries);
        mFlatList.add(rivoli);
        mFlatList.add(trevise);
        mFlatList.add(montorgueil);
        mFlatList.add(bac);
        mFlatList.add(paradis2);
        mFlatList.add(ecuries2);
        mFlatList.add(rivoli2);
        mFlatList.add(trevise2);
        mFlatList.add(montorgueil2);
        mFlatList.add(bac2);
    }

}
