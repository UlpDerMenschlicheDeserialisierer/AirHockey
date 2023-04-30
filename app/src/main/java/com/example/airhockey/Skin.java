package com.example.airhockey;

public class Skin {
    private int id;
    private String name;
    private int price;
    private int purchased;
    private int selected;

    public Skin(int id, String name, int price, int purchased, int selected) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.purchased = purchased;
        this.selected = selected;
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

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}

