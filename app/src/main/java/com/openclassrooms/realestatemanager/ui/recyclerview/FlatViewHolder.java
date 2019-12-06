package com.openclassrooms.realestatemanager.ui.recyclerview;

import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.Flat;

import butterknife.BindView;
import butterknife.ButterKnife;

class FlatViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_flat_recycler_layout) ConstraintLayout mFlatConstraintLayout;
    @BindView(R.id.item_flat_pic) AppCompatImageView mFlatPic;
    @BindView(R.id.item_flat_price) TextView mFlatPrice;
    @BindView(R.id.item_flat_location) TextView mFlatCity;
    @BindView(R.id.item_flat_type) TextView mFlatType;
    @BindView(R.id.item_flat_summary) TextView mFlatSummary;
    @BindView(R.id.sold_panel) TextView mFlatSold;

    /**
     * This method describes a line of our RecyclerView
     * @param itemView represents one line of the list
     */
    public FlatViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithFlat(Flat flat) {
        if (!flat.getPicPath().equals("")) this.mFlatPic.setImageBitmap(BitmapFactory.decodeFile(flat.getPicPath()));
        this.mFlatSummary.setText(flat.getSummary());
        this.mFlatCity.setText(flat.getCityAddress());
        this.mFlatPrice.setText(this.itemView.getResources().getString(R.string.euro, flat.getPrice()));
        this.mFlatType.setText(flat.getType());
        this.mFlatConstraintLayout.setTag(flat.getId());
        if (flat.isSold()) mFlatSold.setVisibility(View.VISIBLE);
        else mFlatSold.setVisibility(View.GONE);
    }

}
