package com.openclassrooms.realestatemanager.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

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
    private boolean school;
    private boolean postOffice;
    private boolean restaurant;
    private boolean theater;
    private boolean shop;
    private boolean isSold;
    private int agentId;

    public Flat() { }

    public Flat(String summary, String description, String type, Integer price, Integer surface, Integer room, Integer bedroom, Integer bathroom, Integer numberAddress, String streetAddress, Integer postalCodeAddress, String cityAddress, boolean school, boolean postOffice, boolean restaurant, boolean theater, boolean shop, int agentId) {
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
        this.school = school;
        this.postOffice = postOffice;
        this.restaurant = restaurant;
        this.theater = theater;
        this.shop = shop;
        this.isSold = false;
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
    public boolean isSchool() {return school;}
    public boolean isPostOffice() {return postOffice;}
    public boolean isRestaurant() {return restaurant;}
    public boolean isTheater() {return theater;}
    public boolean isShop() {return shop;}
    public boolean getIsSold() {return isSold;}
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
    public void setSchool(boolean school) {this.school = school;}
    public void setPostOffice(boolean postOffice) {this.postOffice = postOffice;}
    public void setRestaurant(boolean restaurant) {this.restaurant = restaurant;}
    public void setTheater(boolean theater) {this.theater = theater;}
    public void setShop(boolean shop) {this.shop = shop;}
    public void setIsSold(boolean isSold) {this.isSold = isSold;}
    public void setAgentId(int agentId) {this.agentId = agentId;}

}
