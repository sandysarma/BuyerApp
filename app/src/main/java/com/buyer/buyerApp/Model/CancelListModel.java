package com.buyer.buyerApp.Model;

import com.buyer.buyerApp.ModelResult.CancelListResult;

import com.buyer.buyerApp.ModelResult.CancelList_Response;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CancelListModel {
    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("record")
    @Expose
    private List<CancelList_Response> cancelListResults = null;

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

    public List<CancelList_Response> getCancelListResults() {
        return cancelListResults;
    }

    public void setCancelListResults(List<CancelList_Response> cancelListResults) {
        this.cancelListResults = cancelListResults;
    }
}
