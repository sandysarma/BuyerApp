package com.buyer.buyerApp.Model;

import com.buyer.buyerApp.ModelResult.ExploreProjectresult;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExploreProjectModel {
    @SerializedName("success")
    @Expose
    public String success;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("record")
    @Expose
    public List<ExploreProjectresult> record = null;

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

    public List<ExploreProjectresult> getRecord() {
        return record;
    }

    public void setRecord(List<ExploreProjectresult> record) {
        this.record = record;
    }
}
