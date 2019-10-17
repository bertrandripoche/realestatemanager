package com.openclassrooms.realestatemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.addFlat.FlatViewModel;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Agent;
import com.openclassrooms.realestatemanager.model.Flat;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlatListFragment extends Fragment {
    @BindView(R.id.fragment_flat_list_recycler_view)
    RecyclerView mRecyclerView;
    private FlatAdapter mAdapter;
    private List<Flat> mFlatList;
    private FlatDetailFragment mFlatDetailFragment;

    private FlatViewModel mFlatViewModel;
    private static int AGENT_ID = 0;

    public FlatListFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_flat_list, container, false);
        ButterKnife.bind(this, view);

        configureRecyclerView();
        configureViewModel();
        configureOnClickRecyclerView();
        getFlats();

        return view;
    }

    private void getFlats() {
        this.mFlatViewModel.getFlats().observe(this, this::updateFlatsList);
    }

    private void updateFlatsList(List<Flat> flats){
        this.mAdapter.updateData(flats);
    }

    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(getContext());
        this.mFlatViewModel = ViewModelProviders.of(this, mViewModelFactory).get(FlatViewModel.class);
        this.mFlatViewModel.init(AGENT_ID);
    }

    private void configureRecyclerView() {
        final int orientation = getResources().getInteger(R.integer.gallery_orientation);

        mFlatList = new ArrayList<>();

        mAdapter = new FlatAdapter(mFlatList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), orientation, false));
        mRecyclerView.setAdapter(mAdapter);
    }

    void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(mRecyclerView, R.layout.recycler_view_flat_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        final String FLATID = "flatId";
                        int itemTag = (v.getTag() == null) ? 1 : Integer.parseInt(v.getTag().toString());
                        Bundle args = new Bundle();
                        args.putInt(FLATID, itemTag);

                        mFlatDetailFragment = (FlatDetailFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.container_fragment_flat_detail);
                        if (getActivity().findViewById(R.id.container_fragment_flat_detail) != null) {
                            mFlatDetailFragment = new FlatDetailFragment();
                            mFlatDetailFragment.setArguments(args);
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container_fragment_flat_detail, mFlatDetailFragment)
                                    .commit();
                            v.setSelected(true);
                        } else{
                            Intent intent = new Intent(v.getContext(), FlatDetailActivity.class);
                            intent.putExtras(args);
                            startActivity(intent);
                        }
                    }
                });
    }

}
