package com.example.myapplication.models;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "PRODUCTS")
public class Product {

    @SerializedName("id")
    @Expose
    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("title")
    @Expose
    @ColumnInfo(name = "title")
    private String title;

    @SerializedName("price")
    @Expose
    @ColumnInfo(name = "price")
    private String price;

    @SerializedName("description")
    @Expose
    @ColumnInfo(name = "description")
    // todo this is very large find solution
    private String description;

    @SerializedName("category")
    @Expose
    @ColumnInfo(name = "category")
    // todo this is very large find solution
    private String category;

    @SerializedName("image")
    @Expose
    @ColumnInfo(name = "image")
    private String imageUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String image) {
        this.title = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public String toString() {
        Log.d("Product", "toString: " + id + " " + title + " " + price + " " + description + " " + category + " " + imageUrl);
        return "Product{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", avatar='" + imageUrl + '\'' +
                '}';
    }

}
