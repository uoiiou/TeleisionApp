package com.androidapp.televisorapp.models;

public class Item {

    private final String imageURL;
    private final String brand;
    private final String model;
    private final String year;
    private final String price;

    public Item(String imageURL, String brand, String model, String year, String price) {
        this.imageURL = imageURL;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
    }


    public String getImageURL() {
        return imageURL;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getYear() {
        return year;
    }

    public String getPrice() {
        return price;
    }
}