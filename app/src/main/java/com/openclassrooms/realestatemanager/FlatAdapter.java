package com.openclassrooms.realestatemanager;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FlatAdapter extends RecyclerView.Adapter<FlatViewHolder>{

    private static final String TAG = "RestaurantAdapter";
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_flat_item_horizontal,parent,false);

        return new FlatViewHolder(v);
    }

    /**
     * This method allows to bind the data from an Attendee object on one line
     * @param holder the ViewHolder which allows to display one element of the list
     * @param position is an int representing the position in the list
     */
    @Override
    public void onBindViewHolder(@NonNull FlatViewHolder holder, int position) {
        Flat flat = mFlatList.get(position);

        holder.mFlatCity.setText(flat.getCityAddress());
        holder.mFlatPrice.setText(flat.getPrice());
        holder.mFlatType.setText(flat.getType());

    }

    @Override
    public int getItemCount() {
        return mFlatList.size();
    }


}
