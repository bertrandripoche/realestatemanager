package com.openclassrooms.realestatemanager.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.openclassrooms.realestatemanager.utils.UriTypeConverter;

@Entity(foreignKeys = @ForeignKey(entity = Flat.class,
        parentColumns = "id",
        childColumns = "flatId"),
        indices = {@Index("flatId")})
@TypeConverters({UriTypeConverter.class})

public class Pic implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private Uri picUri;
    private String picPath;
    private String caption;
    private int flatId;

    @Ignore
    public Pic(Uri picUri, String picPath, String caption) {
        this.picUri = picUri;
        this.picPath = picPath;
        this.caption = caption;
        this.flatId = 0;
    }

    public Pic(Uri picUri, String picPath, String caption, int flatId) {
        this.picUri = picUri;
        this.picPath = picPath;
        this.caption = caption;
        this.flatId = flatId;
    }

    public int getId() {return id;}
    public Uri getPicUri() {return picUri;}
    public String getPicPath() {return picPath;}
    public String getCaption() {return caption;}
    public int getFlatId() {return flatId;}

    public void setId(int id) {this.id = id;}
    public void setPicUri(Uri picUri) {this.picUri = picUri;}
    public void setPicPath(String picPath) {this.picPath = picPath;}
    public void setCaption(String caption) {this.caption = caption;}
    public void setFlatId(int flatId) {this.flatId = flatId;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeParcelable(this.picUri, flags);
        dest.writeString(this.picPath);
        dest.writeString(this.caption);
        dest.writeInt(this.flatId);
    }

    protected Pic(Parcel in) {
        this.id = in.readInt();
        this.picUri = in.readParcelable(Uri.class.getClassLoader());
        this.picPath = in.readString();
        this.caption = in.readString();
        this.flatId = in.readInt();
    }

    public static final Parcelable.Creator<Pic> CREATOR = new Parcelable.Creator<Pic>() {
        @Override
        public Pic createFromParcel(Parcel source) {
            return new Pic(source);
        }

        @Override
        public Pic[] newArray(int size) {
            return new Pic[size];
        }
    };
}
