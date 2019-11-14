package com.openclassrooms.realestatemanager.model;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Flat.class,
        parentColumns = "id",
        childColumns = "flatId"),
        indices = {@Index("flatId")})
public class Pic {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private Uri picPath;
    private String caption;
    private int flatId;

    public Pic(Uri picPath, String caption, int flatId) {
        this.picPath = picPath;
        this.caption = caption;
        this.flatId = flatId;
    }

    public int getId() {return id;}
    public Uri getPicPath() {return picPath;}
    public String getCaption() {return caption;}
    public int getFlatId() {return flatId;}

    public void setId(int id) {this.id = id;}
    public void setPicPath(Uri picPath) {this.picPath = picPath;}
    public void setCaption(String caption) {this.caption = caption;}
    public void setFlatId(int flatId) {this.flatId = flatId;}
}
