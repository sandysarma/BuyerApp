package com.buyer.buyerApp.Model;

;
import com.buyer.buyerApp.ModelResult.OrderList_Response;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderList_Model {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("record")
    @Expose
    private List<OrderList_Response> orderListResponses = null;

    public String getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<OrderList_Response> getOrderListResponses() {
        return orderListResponses;
    }
}
