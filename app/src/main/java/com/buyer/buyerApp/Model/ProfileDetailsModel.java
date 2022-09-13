package com.buyer.buyerApp.Model;


import com.buyer.buyerApp.ModelResult.ProfileDetailsResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileDetailsModel
{
    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("record")
    @Expose
    private ProfileDetailsResponse profileDetailsResponse;

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

    public ProfileDetailsResponse getProfileDetailsResponse() {
        return profileDetailsResponse;
    }
}
