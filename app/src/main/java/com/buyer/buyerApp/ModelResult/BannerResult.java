package com.buyer.buyerApp.ModelResult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BannerResult {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("image")
    @Expose
    public String image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
