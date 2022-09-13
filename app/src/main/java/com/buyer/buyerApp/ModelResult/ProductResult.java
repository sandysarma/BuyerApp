package com.buyer.buyerApp.ModelResult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductResult
{

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("category_id")
    @Expose
    public Integer categoryId;
    @SerializedName("category_name")
    @Expose
    public String categoryName;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("price")
    @Expose
    public String price;
    @SerializedName("quantity")
    @Expose
    public String quantity;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("remaning_quantity")
    @Expose
    public String remaningQuantity;
    @SerializedName("added_to_cart")
    @Expose
    public Integer addedToCart;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemaningQuantity() {
        return remaningQuantity;
    }

    public void setRemaningQuantity(String remaningQuantity) {
        this.remaningQuantity = remaningQuantity;
    }

    public Integer getAddedToCart() {
        return addedToCart;
    }

    public void setAddedToCart(Integer addedToCart) {
        this.addedToCart = addedToCart;
    }
}
