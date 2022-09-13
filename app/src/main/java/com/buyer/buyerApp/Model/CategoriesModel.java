package com.buyer.buyerApp.Model;

import com.buyer.buyerApp.ModelResult.CatergoriesResult;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoriesModel
{
    @SerializedName("success")
    @Expose
    public String success;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("record")
    @Expose
    public List<CatergoriesResult> catergoriesResults = null;

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

    public List<CatergoriesResult> getCatergoriesResults() {
        return catergoriesResults;
    }

    public void setCatergoriesResults(List<CatergoriesResult> catergoriesResults) {
        this.catergoriesResults = catergoriesResults;
    }
}
