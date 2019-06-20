package com.queerdevs.raj.food2u;

/**
 * Created by RAJ on 3/2/2017.
 */

public class Order {
    private String name;
    private String price;
    private String quantity;
    private String totalprice = "0";
    private long id;

    Order() {
    }


    Order(String name, String price, String quantity, String totalprice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.totalprice = totalprice;
    }

    Order(String name, String price, String quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    @Override
    public String toString() {
        return name;
    }
}
