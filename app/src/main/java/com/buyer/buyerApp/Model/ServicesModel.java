package com.buyer.buyerApp.Model;

import com.buyer.buyerApp.ModelResult.ServicesResult;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServicesModel
{
    @SerializedName("success")
    @Expose
    public String success;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("record")
    @Expose
    public List<ServicesResult> servicesResults = null;

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

    public List<ServicesResult> getServicesResults() {
        return servicesResults;
    }

    public void setServicesResults(List<ServicesResult> servicesResults) {
        this.servicesResults = servicesResults;
    }
}
