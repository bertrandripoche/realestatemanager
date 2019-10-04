package com.openclassrooms.realestatemanager;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

class FlatViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_flat_recycler_layout) ConstraintLayout mFlatConstraintLayout;
    @BindView(R.id.item_flat_pic) AppCompatImageView mFlatPic;
    @BindView(R.id.item_flat_price) TextView mFlatPrice;
    @BindView(R.id.item_flat_location) TextView mFlatCity;
    @BindView(R.id.item_flat_type) TextView mFlatType;

    /**
     * This method describes a line of our RecyclerView
     * @param itemView represents one line of the list
     */
    public FlatViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
