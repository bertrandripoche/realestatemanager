package com.openclassrooms.realestatemanager;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

class FlatViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "RestaurantViewHolder";

    AppCompatImageView mFlatPic;
    TextView mFlatPrice;
    TextView mFlatCity;
    TextView mFlatType;

    /**
     * This method describes a line of our RecyclerView
     * @param itemView represents one line of the list
     */
    public FlatViewHolder(@NonNull View itemView) {
        super(itemView);

        mFlatPic = itemView.findViewById(R.id.item_flat_pic);
        mFlatPrice = itemView.findViewById(R.id.item_flat_price);
        mFlatCity = itemView.findViewById(R.id.item_flat_location);
        mFlatType = itemView.findViewById(R.id.item_flat_type);

    }
}
