package com.openclassrooms.realestatemanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.model.Flat;

import java.util.List;

public class FlatAdapter extends RecyclerView.Adapter<FlatViewHolder>{

    private List<Flat> mFlatList;

    public FlatAdapter(List<Flat> mFlatList) {
        this.mFlatList = mFlatList;
    }

    /**
     * This method creates the ViewHolder
     * @param parent is the parent view which contains the RecyclerView
     * @param viewType
     * @returnan RestaurantViewHolder object
     */
    @NonNull
    @Override
    public FlatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_flat_item,parent,false);

        return new FlatViewHolder(v);
    }

    /**
     * This method allows to bind the data from a Flat object on one line
     * @param holder the ViewHolder which allows to display one element of the list
     * @param position is an int representing the position in the list
     */
    @Override
    public void onBindViewHolder(@NonNull FlatViewHolder holder, int position) {
        holder.updateWithFlat(this.mFlatList.get(position));

//        Flat flat = mFlatList.get(position);
//        holder.mFlatSummary.setText(flat.getSummary());
//        holder.mFlatCity.setText(flat.getCityAddress());
//        holder.mFlatPrice.setText(holder.itemView.getResources().getString(R.string.euro, flat.getPrice()));
//        holder.mFlatType.setText(flat.getType());
//        holder.mFlatConstraintLayout.setTag(flat.getId());
    }

    @Override
    public int getItemCount() {
        return mFlatList.size();
    }

    public Flat getFlat(int position){
        return this.mFlatList.get(position);
    }

    public void updateData(List<Flat> flatList){
        this.mFlatList = flatList;
        this.notifyDataSetChanged();
    }
}
