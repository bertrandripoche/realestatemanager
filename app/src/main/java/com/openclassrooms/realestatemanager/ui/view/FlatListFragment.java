package com.openclassrooms.realestatemanager.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.viewmodel.FlatViewModel;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Flat;
import com.openclassrooms.realestatemanager.ui.recyclerview.FlatAdapter;
import com.openclassrooms.realestatemanager.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlatListFragment extends Fragment {
    @BindView(R.id.fragment_flat_list_recycler_view) RecyclerView mRecyclerView;

    private MainActivity mMainActivity;
    private FlatAdapter mAdapter;
    private List<Flat> mFlatList;
    private FlatDetailFragment mFlatDetailFragment;

    private FlatViewModel mFlatViewModel;
    private int mSelectedFlat = -1;
    private static int AGENT_ID = 0;
    final String FLATID = "flatId";
    final String SELECTEDFLAT = "selectedFlat";
    private boolean mIsTablet;

    public FlatListFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_flat_list, container, false);
        ButterKnife.bind(this, view);
        mIsTablet = getResources().getBoolean(R.bool.isTablet);
        mMainActivity = (MainActivity) getActivity();

        configureRecyclerView();
        configureViewModel();
        configureOnClickRecyclerView();
        if (mMainActivity.mQuery == null || mMainActivity.mQuery.equals("") ) {
            getFlats();
            mMainActivity.hideBackToListBtn();
        }
        else {
            getFlatsFromQuery(mMainActivity.mQuery);
            mSelectedFlat = -1;
            Toast.makeText(getContext(), R.string.search_result, Toast.LENGTH_SHORT).show();
            mMainActivity.displayBackToListBtn();
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTEDFLAT, mSelectedFlat);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mSelectedFlat = savedInstanceState.getInt(SELECTEDFLAT, -1);

            if (mSelectedFlat != -1) {
                mMainActivity.displayEditBtn();
                mAdapter.index = mSelectedFlat;
            }
            else mMainActivity.hideEditBtn();
        }
    }

    private void getFlatsFromQuery(String queryString) {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(queryString);
        this.mFlatViewModel.getFlatsFromQuery(query).observe(this, this::updateFlatsList);
    }

    public void getFlats() {
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
        MainActivity mainActivity = (MainActivity) getActivity();

        mFlatList = new ArrayList<>();

        mAdapter = new FlatAdapter(mFlatList, mIsTablet, getContext());
        mAdapter.index = mainActivity.mSelectedFlat;

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), orientation, false));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(mRecyclerView, R.layout.recycler_view_flat_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Long flatId = (v.getTag() == null) ? 1 : Long.parseLong(v.getTag().toString());
                        Bundle args = new Bundle();
                        args.putLong(FLATID, flatId);
                        args.putInt(SELECTEDFLAT, position);
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.mSelectedFlat = flatId.intValue();
                        mSelectedFlat = flatId.intValue();

                        // Tablet
                         if (mIsTablet) {
                            mAdapter.index = flatId.intValue();
                            mAdapter.notifyDataSetChanged();

                            mFlatDetailFragment = new FlatDetailFragment();
                            mFlatDetailFragment.setArguments(args);
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container_fragment_flat_detail, mFlatDetailFragment, "FlatDetail")
                                    .commit();
                            MainActivity mMainActivity = (MainActivity) getActivity();
                            mMainActivity.displayEditBtn();
                        // Smartphone
                        } else {
                            Intent intent = new Intent(v.getContext(), FlatDetailActivity.class);
                            intent.putExtras(args);
                            startActivity(intent);
                        }
                    }
                });
    }

}
