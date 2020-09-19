package com.bayraktar.stockcontrol.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Stock {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private String description;

    @ColumnInfo(name = "image_url")
    private String imageUrl;

    private float price;

    public Stock(String name, String description, String imageUrl, float price) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public float getPrice() {
        return price;
    }
}
