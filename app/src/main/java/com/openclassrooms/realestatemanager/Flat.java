package com.openclassrooms.realestatemanager;

class Flat {
    private int id;
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

    public Flat(int id, String type, int price, int surface, int room, int bedroom, int bathroom, int numberAddress, String streetAddress, int postalCodeAddress, String cityAddress) {
        this.id = id;
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
    }

    // GETTERS
    public int getId() {return id;}
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

    // SETTERS
    public void setId(int id) {this.id = id;}
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

}
