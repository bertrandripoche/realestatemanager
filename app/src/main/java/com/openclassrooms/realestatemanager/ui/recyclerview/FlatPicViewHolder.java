package com.openclassrooms.realestatemanager.ui.recyclerview;

import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.Pic;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlatPicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.item_flat_pic_recycler_layout) ConstraintLayout mFlatPicConstraintLayout;
    @BindView(R.id.item_pic) AppCompatImageView mPic;
    @BindView(R.id.item_caption) TextView mCaption;
    @BindView(R.id.item_delete_flat_pic) ImageButton mBtnDelete;

    private WeakReference<FlatPicAdapter.Listener> mCallbackWeakRef;

    /**
     * This method describes a line of our RecyclerView
     * @param itemView represents one line of the list
     */
    public FlatPicViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithPic(Pic pic, FlatPicAdapter.Listener callback, boolean editionMode) {
        if (editionMode) this.mBtnDelete.setVisibility(View.VISIBLE);
        this.mCaption.setText(pic.getCaption());
        this.mPic.setImageBitmap(BitmapFactory.decodeFile(pic.getPicPath()));
        this.mBtnDelete.setOnClickListener(this);
        this.mCallbackWeakRef = new WeakReference<FlatPicAdapter.Listener>(callback);
    }

    @Override
    public void onClick(View view) {
        FlatPicAdapter.Listener callback = mCallbackWeakRef.get();
        if (callback != null) callback.onClickDeleteButton(getAdapterPosition());
    }
}
