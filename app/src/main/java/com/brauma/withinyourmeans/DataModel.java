package com.brauma.withinyourmeans;

public class DataModel {

    int amount;
    String description;
    int id_;
    Integer image;

    public DataModel(int amount, String description, int id_, int image) {
        this.amount = amount;
        this.description = description;
        this.id_ = id_;
        this.image=image;
    }

    public int getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public int getCategoryIcon() {
        return image;
    }

    public int getId() {
        return id_;
    }
}