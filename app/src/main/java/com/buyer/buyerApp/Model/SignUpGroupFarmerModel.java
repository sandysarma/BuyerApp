package com.buyer.buyerApp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpGroupFarmerModel
{
    @SerializedName("success")
    @Expose
    public String success;
    @SerializedName("message")
    @Expose
    public String message;


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
}
