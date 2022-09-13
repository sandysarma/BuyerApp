package com.buyer.buyerApp.Model;

import com.buyer.buyerApp.ModelResult.ProductCategoryResult;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductCategoryModel
{
    @SerializedName("success")
    @Expose
    public String success;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("record")
    @Expose
    public List<ProductCategoryResult> productCategoryResults = null;

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

    public List<ProductCategoryResult> getProductCategoryResults() {
        return productCategoryResults;
    }

    public void setProductCategoryResults(List<ProductCategoryResult> productCategoryResults) {
        this.productCategoryResults = productCategoryResults;
    }
}
