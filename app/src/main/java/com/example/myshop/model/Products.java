package com.example.myshop.model;

public class Products {
    private String name, description, price, image, pid, date, time;

    public Products() {

    }

    public Products(String name, String description, String price, String image, String pid, String date, String time) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.pid = pid;
        this.date = date;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDate() {
        return date;
    }
}