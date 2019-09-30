package com.openclassrooms.realestatemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlatListFragment extends Fragment {
    @BindView(R.id.fragment_flat_list_recycler_view)
    RecyclerView mRecyclerView;
    FlatAdapter mAdapter;
    List<Flat> mFlatList;

    public FlatListFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_flat_list, container, false);
        ButterKnife.bind(this, view);

        configureRecyclerView();
        configureOnClickRecyclerView();
        populategroceryList();

        return view;
    }

    private void configureRecyclerView() {
        mFlatList = new ArrayList<>();

        mAdapter = new FlatAdapter(mFlatList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(mRecyclerView, R.layout.recycler_view_flat_item_horizontal)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    }
                });
    }

    private void populategroceryList(){
        Flat paradis = new Flat(1, "Duplex", 237000, 31, 1 , 1, 1, 15, "rue du Paradis", 75010, "Paris");
        Flat ecuries = new Flat(2, "Appartement", 480000, 67, 3 , 2, 1, 5, "rue des Petites Écuries", 75010, "Paris");
        Flat rivoli = new Flat(3, "Appartement", 1560000, 138, 6 , 4, 2, 121, "rue de Rivoli", 75001, "Paris");
        Flat trevise = new Flat(4, "Appartement", 980000, 108, 4 , 2, 2, 34, "rue de Trévise", 75009, "Paris");
        Flat montorgueil = new Flat(5, "Loft", 1860000, 210, 5 , 3, 2, 56, "rue de Montorgueil", 75009, "Paris");
        Flat bac = new Flat(6, "Appartement", 520000, 58, 3 , 2, 1, 87, "rue du Bac", 75009, "Paris");

        mFlatList.add(paradis);
        mFlatList.add(ecuries);
        mFlatList.add(rivoli);
        mFlatList.add(trevise);
        mFlatList.add(montorgueil);
        mFlatList.add(bac);
        mAdapter.notifyDataSetChanged();
    }
}
