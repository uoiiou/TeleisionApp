package com.androidapp.televisorapp.models;

import java.io.Serializable;

public class Image implements Serializable {

    private final String imageURL;

    public Image(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }
}