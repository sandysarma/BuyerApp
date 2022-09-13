package com.buyer.buyerApp.Model;

import com.buyer.buyerApp.ModelResult.CartListResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartListModel
{
    @SerializedName("success")
    @Expose
    public String success;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("record")
    @Expose
        public List<CartListResponse> CartListResponse = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CartListResponse> getCartListResponse() {
        return CartListResponse;
    }

    public void setCartListResponse(List<CartListResponse> cartListResponse) {
        CartListResponse = cartListResponse;
    }
}
