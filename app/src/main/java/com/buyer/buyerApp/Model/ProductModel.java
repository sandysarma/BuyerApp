package com.buyer.buyerApp.Model;

import com.buyer.buyerApp.ModelResult.ProductResult;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductModel
{


        @SerializedName("success")
        @Expose
        public String success;
        @SerializedName("message")
        @Expose
        public String message;
        @SerializedName("record")
        @Expose
        public List<ProductResult> productResults = null;

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

        public List<ProductResult> getProductResults() {
                return productResults;
        }

        public void setProductResults(List<ProductResult> productResults) {
                this.productResults = productResults;
        }
}
