package com.openclassrooms.realestatemanager.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.Date;

@Entity(foreignKeys = @ForeignKey(entity = Agent.class,
        parentColumns = "id",
        childColumns = "agentId"),
        indices = {@Index("agentId")})
public class Flat {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String summary;
    private String description;
    private String type;
    private Integer price;
    private Integer surface;
    private Integer room;
    private Integer bedroom;
    private Integer bathroom;
    private Integer numberAddress;
    private String streetAddress;
    private Integer postalCodeAddress;
    private String cityAddress;
    private boolean isSchool;
    private boolean isPostOffice;
    private boolean isRestaurant;
    private boolean isTheater;
    private boolean isShop;
    private boolean isSold;
    private String soldDate;
    private String availableDate;
    private int agentId;

    public Flat() { }

    public Flat(String summary, String description, String type, Integer price, Integer surface, Integer room, Integer bedroom, Integer bathroom, Integer numberAddress, String streetAddress, Integer postalCodeAddress, String cityAddress, boolean isSchool, boolean isPostOffice, boolean isRestaurant, boolean isTheater, boolean isShop, int agentId) {
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
        this.isSchool = isSchool;
        this.isPostOffice = isPostOffice;
        this.isRestaurant = isRestaurant;
        this.isTheater = isTheater;
        this.isShop = isShop;
        this.isSold = false;
        this.availableDate = Utils.getTodayDate();
        this.agentId = agentId;
    }

    public Flat(int id, String summary, String description, String type, Integer price, Integer surface, Integer room, Integer bedroom, Integer bathroom, Integer numberAddress, String streetAddress, Integer postalCodeAddress, String cityAddress, boolean isSchool, boolean isPostOffice, boolean isRestaurant, boolean isTheater, boolean isShop, int agentId) {
        this.id = id;
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
    public String getSummary() {return summary;}
    public String getDescription() {return description;}
    public String getType() {return type;}
    public Integer getPrice() {return price;}
    public Integer getSurface() {return surface;}
    public Integer getRoom() {return room;}
    public Integer getBedroom() {return bedroom;}
    public Integer getBathroom() {return bathroom;}
    public Integer getNumberAddress() {return numberAddress;}
    public String getStreetAddress() {return streetAddress;}
    public Integer getPostalCodeAddress() {return postalCodeAddress;}
    public String getCityAddress() {return cityAddress;}
    public boolean isSchool() {return isSchool;}
    public boolean isPostOffice() {return isPostOffice;}
    public boolean isRestaurant() {return isRestaurant;}
    public boolean isTheater() {return isTheater;}
    public boolean isShop() {return isShop;}
    public boolean getIsSold() {return isSold;}
    public String getSoldDate() {return soldDate;}
    public String getAvailableDate() {return availableDate;}
    public int getAgentId() {return agentId;}

    // SETTERS
    public void setId(int id) {this.id = id;}
    public void setSummary(String summary) {this.summary = summary;}
    public void setDescription(String description) {this.description = description;}
    public void setType(String type) {this.type = type;}
    public void setPrice(Integer price) {this.price = price;}
    public void setSurface(Integer surface) {this.surface = surface;}
    public void setRoom(Integer room) {this.room = room;}
    public void setBedroom(Integer bedroom) {this.bedroom = bedroom;}
    public void setBathroom(Integer bathroom) {this.bathroom = bathroom;}
    public void setNumberAddress(Integer number_address) {this.numberAddress = number_address;}
    public void setStreetAddress(String street_address) {this.streetAddress = street_address;}
    public void setPostalCodeAddress(Integer postal_code_address) {this.postalCodeAddress = postal_code_address;}
    public void setCityAddress(String city_address) {this.cityAddress = city_address;}
    public void setSchool(boolean school) {this.isSchool = school;}
    public void setPostOffice(boolean postOffice) {this.isPostOffice = postOffice;}
    public void setRestaurant(boolean restaurant) {this.isRestaurant = restaurant;}
    public void setTheater(boolean theater) {this.isTheater = theater;}
    public void setShop(boolean shop) {this.isShop = shop;}
    public void setIsSold(boolean isSold) {this.isSold = isSold;}
    public void setSoldDate(String soldDate) {this.soldDate = soldDate;}
    public void setAvailableDate(String availableDate) {this.availableDate = availableDate;}
    public void setAgentId(int agentId) {this.agentId = agentId;}

}
