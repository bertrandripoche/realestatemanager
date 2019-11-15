package com.openclassrooms.realestatemanager;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.model.Pic;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlatPicViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.item_flat_pic_recycler_layout) ConstraintLayout mFlatPicConstraintLayout;
    @BindView(R.id.item_pic) AppCompatImageView mPic;
    @BindView(R.id.item_caption) TextView mCaption;

    /**
     * This method describes a line of our RecyclerView
     * @param itemView represents one line of the list
     */
    public FlatPicViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithPic(Pic pic) {
        this.mCaption.setText(pic.getCaption());
        this.mPic.setImageURI(pic.getPicPath());
    }

}
