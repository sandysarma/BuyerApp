package com.buyer.buyerApp.Model;


import com.buyer.buyerApp.ModelResult.TransationRequestResult;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionRequestModel {
    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("record")
    @Expose
    private List<TransationRequestResult> record = null;

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

    public List<TransationRequestResult> getRecord() {
        return record;
    }

    public void setRecord(List<TransationRequestResult> record) {
        this.record = record;
    }
}
