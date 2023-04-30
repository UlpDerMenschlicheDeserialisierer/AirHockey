package com.example.airhockey;

public class Skin {
    private int id;
    private String name;
    private int price;

    private int purchased;
    private String imagePath;

    public Skin(int id, String name, int price, int purchased, String imagePath) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.purchased = purchased;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPurchased() {
        return purchased;
    }

    public void setPurchased(int purchased) {
        this.purchased = purchased;
    }


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}

