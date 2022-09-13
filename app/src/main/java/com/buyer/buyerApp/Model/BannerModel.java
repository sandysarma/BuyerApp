package com.buyer.buyerApp.Model;

import com.buyer.buyerApp.ModelResult.BannerResult;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BannerModel {
    @SerializedName("success")
    @Expose
    public String success;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("record")
    @Expose
    public List<BannerResult> record = null;

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

    public List<BannerResult> getRecord() {
        return record;
    }

    public void setRecord(List<BannerResult> record) {
        this.record = record;
    }
}
