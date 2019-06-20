package com.queerdevs.raj.food2u;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by RAJ on 2/22/2017.
 */

public class RestaurantModel {
    private String restaurant;

    private float rating;
    private String workingDay;
    @SerializedName("address")
    private List<Address> address;
    private String image;
    private String service;
    private String hourOfOperation;
    private String menuURL;
    private String price;
    private String item;
    private String imageMenu;

    public String getImageMenu() {
        return imageMenu;
    }

    public void setImageMenu(String imageMenu) {
        this.imageMenu = imageMenu;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getMenuURL() {
        return menuURL;
    }

    public void setMenuURL(String menuURL) {
        this.menuURL = menuURL;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }


    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }


    public String getWorkingDay() {
        return workingDay;
    }

    public void setWorkingDay(String workingDay) {
        this.workingDay = workingDay;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getHourOfOperation() {
        return hourOfOperation;
    }

    public void setHourOfOperation(String hourOfOperation) {
        this.hourOfOperation = hourOfOperation;
    }

    public static class Address {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
