package com.openclassrooms.realestatemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.database.dao.AgentDao;
import com.openclassrooms.realestatemanager.database.dao.FlatDao;
import com.openclassrooms.realestatemanager.model.Agent;
import com.openclassrooms.realestatemanager.model.Flat;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlatListFragment extends Fragment {
    @BindView(R.id.fragment_flat_list_recycler_view)
    RecyclerView mRecyclerView;
    FlatAdapter mAdapter;
    List<Flat> mFlatList;
    FlatDetailFragment mFlatDetailFragment;

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
                        if (/*mFlatDetailFragment == null && */getActivity().findViewById(R.id.container_fragment_flat_detail) != null) {
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

    private void populategroceryList(){
        Flat paradis = new Flat(1, "Duplex au calme","Duplex", 237000, 31, 1 , 1, 1, 15, "rue du Paradis", 75010, "Paris",0);
        Flat ecuries = new Flat(2, "Urbain et stylé", "Appartement", 480000, 67, 3 , 2, 1, 5, "rue des Petites Écuries", 75010, "Paris",0);
        Flat rivoli = new Flat(3, "Familial de luxe", "Appartement", 1560000, 138, 6 , 4, 2, 121, "rue de Rivoli", 75001, "Paris",0);
        Flat trevise = new Flat(4, "Pour grande famille","Appartement", 980000, 108, 4 , 2, 2, 34, "rue de Trévise", 75009, "Paris",0);
        Flat montorgueil = new Flat(5, "Moderne et classieux","Loft", 1860000, 210, 5 , 3, 2, 56, "rue de Montorgueil", 75009, "Paris",0);
        Flat bac = new Flat(6, "Idéal pour cocooning","Appartement", 520000, 58, 3 , 2, 1, 87, "rue du Bac", 75009, "Paris",0);
        Flat paradis2 = new Flat(7, "Charmant pied à terre","Duplex", 237000, 31, 1 , 1, 1, 15, "rue du Paradis", 75010, "Paris",0);
        Flat ecuries2 = new Flat(8, "Un petit nid urbain","Appartement", 480000, 67, 3 , 2, 1, 5, "rue des Petites Écuries", 75010, "Paris",0);
        Flat rivoli2 = new Flat(9, "De l'espace","Appartement", 1560000, 138, 6 , 4, 2, 121, "rue de Rivoli", 75001, "Paris",0);
        Flat trevise2 = new Flat(10, "Pour ne plus se marcher dessus","Appartement", 980000, 108, 4 , 2, 2, 34, "rue de Trévise", 75009, "Paris",0);
        Flat montorgueil2 = new Flat(11, "En mode indus","Loft", 1860000, 210, 5 , 3, 2, 56, "rue de Montorgueil", 75009, "Paris",0);
        Flat bac2 = new Flat(12, "Charmant trois pièces","Appartement", 520000, 58, 3 , 2, 1, 87, "rue du Bac", 75009, "Paris",0);


        Agent agent = new Agent(1, "Pierre", "Paul", "a@gmail.com");

        AgentDao agentDao = new AgentDao() {
            @Override
            public void createAgent(Agent agent) {

            }

            @Override
            public LiveData<Agent> getAgent(long agentId) {
                return null;
            }
        };
        FlatDao flatDao = new FlatDao() {
            @Override
            public LiveData<List<Flat>> getFlats(long agentId) {
                return null;
            }

            @Override
            public long insertFlat(Flat flat) {
                return 0;
            }

            @Override
            public int updateFlat(Flat flat) {
                return 0;
            }

            @Override
            public int deleteFlat(long flatId) {
                return 0;
            }
        };

        agentDao.createAgent(agent);
        agentDao.getAgent(1);

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
        mAdapter.notifyDataSetChanged();
    }
}
