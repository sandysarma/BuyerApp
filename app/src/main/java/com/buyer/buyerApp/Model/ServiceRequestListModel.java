package com.buyer.buyerApp.Model;

import com.buyer.buyerApp.ModelResult.ServiceRequestListResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceRequestListModel
{
    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("record")
    @Expose
    private List<ServiceRequestListResponse> record = null;

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

    public List<ServiceRequestListResponse> getRecord() {
        return record;
    }

    public void setRecord(List<ServiceRequestListResponse> record) {
        this.record = record;
    }
}
