package com.openclassrooms.realestatemanager.ui.recyclerview;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.Flat;

import java.util.List;

public class FlatAdapter extends RecyclerView.Adapter<FlatViewHolder>{

    private List<Flat> mFlatList;
    public int index;

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

//        if (index != -1 && index==position) setSelected(holder);
        if (index != -1 && index==this.mFlatList.get(position).getId()) setSelected(holder);
        else unsetSelected(holder);
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

    public void setSelected(FlatViewHolder holder) {
        holder.mFlatConstraintLayout.setBackgroundColor(Color.parseColor("#C20044"));
        holder.mFlatSummary.setTextColor(Color.parseColor("#FFFFFF"));
        holder.mFlatCity.setTextColor(Color.parseColor("#DDF4CF"));
        holder.mFlatPrice.setTextColor(Color.parseColor("#FFFFFF"));
        holder.mFlatType.setTextColor(Color.parseColor("#FFFFFF"));
    }

    public void unsetSelected(FlatViewHolder holder) {
        holder.mFlatConstraintLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        holder.mFlatSummary.setTextColor(Color.parseColor("#000000"));
        holder.mFlatCity.setTextColor(Color.parseColor("#458039"));
        holder.mFlatPrice.setTextColor(Color.parseColor("#C20044"));
        holder.mFlatType.setTextColor(Color.parseColor("#000000"));
    }
}
