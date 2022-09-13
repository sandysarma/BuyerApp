package com.buyer.buyerApp.Model;

import com.buyer.buyerApp.ModelResult.VehicleListResult;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehicleListModel {
    @SerializedName("success")
    @Expose
    public String success;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("record")
    @Expose
    public List<VehicleListResult> record = null;

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

    public List<VehicleListResult> getRecord() {
        return record;
    }

    public void setRecord(List<VehicleListResult> record) {
        this.record = record;
    }
}
