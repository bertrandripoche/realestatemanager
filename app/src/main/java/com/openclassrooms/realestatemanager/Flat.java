package com.openclassrooms.realestatemanager;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Agent.class,
        parentColumns = "id",
        childColumns = "agentId"))

public class Flat {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String summary;
    private String type;
    private int price;
    private int surface;
    private int room;
    private int bedroom;
    private int bathroom;
    private int numberAddress;
    private String streetAddress;
    private int postalCodeAddress;
    private String cityAddress;
    private boolean isSold;
    private int agentId;

    public Flat(int id, String summary, String type, int price, int surface, int room, int bedroom, int bathroom, int numberAddress, String streetAddress, int postalCodeAddress, String cityAddress) {
        this.id = id;
        this.summary = summary;
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
        this.isSold = false;
    }

    // GETTERS
    public int getId() {return id;}
    public String getSummary() {return summary;}
    public String getType() {return type;}
    public int getPrice() {return price;}
    public int getSurface() {return surface;}
    public int getRoom() {return room;}
    public int getBedroom() {return bedroom;}
    public int getBathroom() {return bathroom;}
    public int getNumberAddress() {return numberAddress;}
    public String getStreetAddress() {return streetAddress;}
    public int getPostalCodeAddress() {return postalCodeAddress;}
    public String getCityAddress() {return cityAddress;}
    public boolean getIsSold() {return isSold;}
    public int getAgentId() {return agentId;}

    // SETTERS
    public void setId(int id) {this.id = id;}
    public void setSummary(String summary) {this.summary = summary;}
    public void setType(String type) {this.type = type;}
    public void setPrice(int price) {this.price = price;}
    public void setSurface(int surface) {this.surface = surface;}
    public void setRoom(int room) {this.room = room;}
    public void setBedroom(int bedroom) {this.bedroom = bedroom;}
    public void setBathroom(int bathroom) {this.bathroom = bathroom;}
    public void setNumber_address(int number_address) {this.numberAddress = number_address;}
    public void setStreet_address(String street_address) {this.streetAddress = street_address;}
    public void setPostal_code_address(int postal_code_address) {this.postalCodeAddress = postal_code_address;}
    public void setCity_address(String city_address) {this.cityAddress = city_address;}
    public void setIsSold(boolean isSold) {this.isSold = isSold;}
    public void setAgentId(int agentId) {this.agentId = agentId;}

}
