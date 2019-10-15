package com.openclassrooms.realestatemanager.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Agent {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String firstname;
    private String lastname;
    private String mail;

    public Agent(String firstname, String lastname, String mail) {
//        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.mail = mail;
    }

    // GETTERS
    public int getId() {return id;}
    public String getFirstname() {return firstname;}
    public String getLastname() {return lastname;}
    public String getMail() {return mail;}

    // SETTERS
    public void setId(int id) {this.id = id;}
    public void setFirstname(String firstname) {this.firstname = firstname;}
    public void setLastname(String lastname) {this.lastname = lastname;}
    public void setMail(String mail) {this.mail = mail;}

}
