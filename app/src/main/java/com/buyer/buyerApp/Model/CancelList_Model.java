package com.buyer.buyerApp.Model;


import com.buyer.buyerApp.ModelResult.CancelList_Response;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CancelList_Model {
    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("record")
    @Expose
    private List<CancelList_Response> cancelList_responseList = null;

    public String getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<CancelList_Response> getCancelList_responseList() {
        return cancelList_responseList;
    }
}
