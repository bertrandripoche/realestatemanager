package com.openclassrooms.realestatemanager.model;

import android.content.ContentValues;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.openclassrooms.realestatemanager.utils.Utils;

@Entity(foreignKeys = @ForeignKey(entity = Agent.class,
        parentColumns = "id",
        childColumns = "agentId"),
        indices = {@Index("agentId")})
public class Flat {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String picPath;
    private String summary;
    private String description;
    private Integer type;
    private Integer price;
    private Integer surface;
    private Integer room;
    private Integer bedroom;
    private Integer bathroom;
    private Integer numberAddress;
    private String streetAddress;
    private Integer postalCodeAddress;
    private String cityAddress;
    private Double latitude;
    private Double longitude;
    private boolean isSchool;
    private boolean isPostOffice;
    private boolean isRestaurant;
    private boolean isTheater;
    private boolean isShop;
    private boolean isSold;
    private String soldDate;
    private String availableDate;
    private int agentId;

    @Ignore
    public Flat() {
    }

    public Flat(String picPath, String summary, String description, Integer type, Integer price, Integer surface, Integer room, Integer bedroom, Integer bathroom, Integer numberAddress, String streetAddress, Integer postalCodeAddress, String cityAddress, Double latitude, Double longitude, boolean isSchool, boolean isPostOffice, boolean isRestaurant, boolean isTheater, boolean isShop, int agentId) {
        this.picPath = picPath;
        this.summary = summary;
        this.description = description;
        this.type = type;
        this.price = price;
        this.surface = surface;
        this.room = room;
        this.bedroom = bedroom;
        this.bathroom = bathroom;
        this.numberAddress = numberAddress;
        this.streetAddress = streetAddress;
        this.postalCodeAddress = postalCodeAddress;
        this.cityAddress = cityAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isSchool = isSchool;
        this.isPostOffice = isPostOffice;
        this.isRestaurant = isRestaurant;
        this.isTheater = isTheater;
        this.isShop = isShop;
        this.isSold = false;
        this.availableDate = Utils.getTodayDate();
        this.agentId = agentId;
    }

    // GETTERS
    public int getId() {return id;}
    public String getPicPath() {return picPath;}
    public String getSummary() {return summary;}
    public String getDescription() {return description;}
    public Integer getType() {return type;}
    public Integer getPrice() {return price;}
    public Integer getSurface() {return surface;}
    public Integer getRoom() {return room;}
    public Integer getBedroom() {return bedroom;}
    public Integer getBathroom() {return bathroom;}
    public Integer getNumberAddress() {return numberAddress;}
    public String getStreetAddress() {return streetAddress;}
    public Integer getPostalCodeAddress() {return postalCodeAddress;}
    public String getCityAddress() {return cityAddress;}
    public Double getLatitude() {return latitude;}
    public Double getLongitude() {return longitude;}
    public boolean isSchool() {return isSchool;}
    public boolean isPostOffice() {return isPostOffice;}
    public boolean isRestaurant() {return isRestaurant;}
    public boolean isTheater() {return isTheater;}
    public boolean isShop() {return isShop;}
    public boolean isSold() {return isSold;}
    public String getSoldDate() {return soldDate;}
    public String getAvailableDate() {return availableDate;}
    public int getAgentId() {return agentId;}

    // SETTERS
    public void setId(int id) {this.id = id;}
    public void setPicPath(String picPath) {this.picPath = picPath;}
    public void setSummary(String summary) {this.summary = summary;}
    public void setDescription(String description) {this.description = description;}
    public void setType(Integer type) {this.type = type;}
    public void setPrice(Integer price) {this.price = price;}
    public void setSurface(Integer surface) {this.surface = surface;}
    public void setRoom(Integer room) {this.room = room;}
    public void setBedroom(Integer bedroom) {this.bedroom = bedroom;}
    public void setBathroom(Integer bathroom) {this.bathroom = bathroom;}
    public void setNumberAddress(Integer number_address) {this.numberAddress = number_address;}
    public void setStreetAddress(String street_address) {this.streetAddress = street_address;}
    public void setPostalCodeAddress(Integer postal_code_address) {this.postalCodeAddress = postal_code_address;}
    public void setCityAddress(String city_address) {this.cityAddress = city_address;}
    public void setLatitude(Double latitude) {this.latitude = latitude;}
    public void setLongitude(Double longitude) {this.longitude = longitude;}
    public void setSchool(boolean school) {this.isSchool = school;}
    public void setPostOffice(boolean postOffice) {this.isPostOffice = postOffice;}
    public void setRestaurant(boolean restaurant) {this.isRestaurant = restaurant;}
    public void setTheater(boolean theater) {this.isTheater = theater;}
    public void setShop(boolean shop) {this.isShop = shop;}
    public void setSold(boolean isSold) {this.isSold = isSold;}
    public void setSoldDate(String soldDate) {this.soldDate = soldDate;}
    public void setAvailableDate(String availableDate) {this.availableDate = availableDate;}
    public void setAgentId(int agentId) {this.agentId = agentId;}

    // For contentProvider data
    public static Flat fromContentValues(ContentValues values) {
        final Flat flat = new Flat();

        if (values.containsKey("picPath")) flat.setPicPath(values.getAsString("picPath"));
        if (values.containsKey("summary")) flat.setSummary(values.getAsString("summary"));
        if (values.containsKey("description")) flat.setDescription(values.getAsString("description"));
        if (values.containsKey("type")) flat.setType(values.getAsInteger("type"));
        if (values.containsKey("price")) flat.setPrice(values.getAsInteger("price"));
        if (values.containsKey("surface")) flat.setSurface(values.getAsInteger("surface"));
        if (values.containsKey("room")) flat.setRoom(values.getAsInteger("room"));
        if (values.containsKey("bedroom")) flat.setBedroom(values.getAsInteger("bedroom"));
        if (values.containsKey("bathroom")) flat.setBathroom(values.getAsInteger("bathroom"));
        if (values.containsKey("numberAddress")) flat.setNumberAddress(values.getAsInteger("numberAddress"));
        if (values.containsKey("streetAddress")) flat.setStreetAddress(values.getAsString("streetAddress"));
        if (values.containsKey("postalCodeAddress")) flat.setPostalCodeAddress(values.getAsInteger("postalCodeAddress"));
        if (values.containsKey("cityAddress")) flat.setCityAddress(values.getAsString("cityAddress"));
        if (values.containsKey("latitude")) flat.setLatitude(values.getAsDouble("latitude"));
        if (values.containsKey("longitude")) flat.setLatitude(values.getAsDouble("longitude"));
        if (values.containsKey("isSchool")) flat.setSchool(values.getAsBoolean("isSchool"));
        if (values.containsKey("isPostOffice")) flat.setPostOffice(values.getAsBoolean("isPostOffice"));
        if (values.containsKey("isRestaurant")) flat.setRestaurant(values.getAsBoolean("isRestaurant"));
        if (values.containsKey("isTheater")) flat.setTheater(values.getAsBoolean("isTheater"));
        if (values.containsKey("isShop")) flat.setShop(values.getAsBoolean("isShop"));
        if (values.containsKey("isSold")) flat.setSold(values.getAsBoolean("isSold"));
        if (values.containsKey("soldDate")) flat.setSoldDate(values.getAsString("soldDate"));
        if (values.containsKey("availableDate")) flat.setAvailableDate(values.getAsString("availableDate"));
        if (values.containsKey("agentId")) flat.setAgentId(values.getAsInteger("agentId"));

        return flat;
    }
}
