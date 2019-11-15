package com.openclassrooms.realestatemanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.model.Pic;

import java.util.List;

public class FlatPicAdapter extends RecyclerView.Adapter<FlatPicViewHolder> {
    private List<Pic> mFlatPicList;
    public int index;

    public FlatPicAdapter(List<Pic> mFlatPicList) {this.mFlatPicList = mFlatPicList;}

    /**
     * This method creates the ViewHolder
     * @param parent is the parent view which contains the RecyclerView
     * @param viewType
     * @returnan RestaurantViewHolder object
     */
    @NonNull
    @Override
    public FlatPicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_flat_pic_item,parent,false);

        return new FlatPicViewHolder(v);
    }

    /**
     * This method allows to bind the data from a Flat object on one line
     * @param holder the ViewHolder which allows to display one element of the list
     * @param position is an int representing the position in the list
     */
    @Override
    public void onBindViewHolder(@NonNull FlatPicViewHolder holder, int position) {
        holder.updateWithPic(this.mFlatPicList.get(position));
    }

    @Override
    public int getItemCount() {
        return mFlatPicList.size();
    }

    public Pic getFlat(int position){
        return this.mFlatPicList.get(position);
    }

    public void updateData(List<Pic> flatPicList){
        this.mFlatPicList = flatPicList;
        this.notifyDataSetChanged();
    }

}
