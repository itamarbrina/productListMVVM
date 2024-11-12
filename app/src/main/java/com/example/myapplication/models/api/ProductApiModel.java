package com.example.myapplication.models.api;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductApiModel {

    @SerializedName("id") // the name of the field in the JSON
    @Expose // mean this field will be serialized and deserialized
    private int id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("category")
    @Expose
    // todo this is very large find solution
    private String category;

    @SerializedName("image")
    @Expose
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

    public void setTitle(String title) {
        this.title = title;
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

}
