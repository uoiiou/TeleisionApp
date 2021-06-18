package com.androidapp.televisorapp.models;

import java.io.Serializable;
import java.util.List;

public class Television implements Serializable {

    private String brand;
    private String model;

    private String year;
    private String price;

    private String diagonal;
    private String resolution;
    private String frequency;

    private String wifi;
    private String hdmi;
    private String usb;
    private String color;

    private String firstPoint;
    private String secondPoint;

    private String videoLink;
    private String imageLinks;

    public Television() {}

    public Television(
            String brand, String model, String year, String price, String diagonal,
            String resolution, String frequency, String wifi, String hdmi, String usb, String color,
            String firstPoint, String secondPoint, String videoLink, List<String> imageLinks) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.diagonal = diagonal;
        this.resolution = resolution;
        this.frequency = frequency;
        this.wifi = wifi;
        this.hdmi = hdmi;
        this.usb = usb;
        this.color = color;
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
        this.videoLink = videoLink;
        this.imageLinks = convertImages(imageLinks);
    }

    private String convertImages(List<String> list) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            result.append(list.get(i)).append(" ");
        }

        return String.valueOf(result);
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiagonal() {
        return diagonal;
    }

    public void setDiagonal(String diagonal) {
        this.diagonal = diagonal;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getWifi() {
        return wifi;
    }

    public void setWifi(String wifi) {
        this.wifi = wifi;
    }

    public String getHdmi() {
        return hdmi;
    }

    public void setHdmi(String hdmi) {
        this.hdmi = hdmi;
    }

    public String getUsb() {
        return usb;
    }

    public void setUsb(String usb) {
        this.usb = usb;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFirstPoint() {
        return firstPoint;
    }

    public void setFirstPoint(String firstPoint) {
        this.firstPoint = firstPoint;
    }

    public String getSecondPoint() {
        return secondPoint;
    }

    public void setSecondPoint(String secondPoint) {
        this.secondPoint = secondPoint;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(String imageLinks) {
        this.imageLinks = imageLinks;
    }
}