package com.buyer.buyerApp.Model;

import com.buyer.buyerApp.ModelResult.CompleteListResult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderCompleteModel {
    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("record")
    @Expose
    private List<CompleteListResult> completeListResults = null;

    public String getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<CompleteListResult> getCompletelistresponse() {
        return completeListResults;
    }
}
