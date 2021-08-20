package com.example.firestore;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

// want pass an object in intent first implements Serializable to class
public class Product implements Serializable {    //   this is model class

    //  attribute

    @Exclude private String id;
    private String name, email, password;


    // constructor
    public Product(){

    }

    public Product(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}