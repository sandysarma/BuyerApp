package com.buyer.buyerApp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.buyer.buyerApp.BuildConfig;
import com.buyer.buyerApp.Model.CartListModel;
import com.buyer.buyerApp.Model.CommonModel;
import com.buyer.buyerApp.ModelResult.CartListResponse;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.ApiClient;
import com.buyer.buyerApp.RetrofitApi.ApiServices;
import com.buyer.buyerApp.RetrofitApi.UrlClass;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class  ProductDetailsActivity extends AppCompatActivity {

    ImageView back_arrow, productImage;
    RelativeLayout relative_cart;
    TextView productNameTxt, priceTxt, quantityTxt, remaningQuantityTxt, descriptionTxt,
            addToCartTxtBtn, cartTxt;
    String strImage, strName, strPrice, strQuantity, strRemaningQuantity,
            strDescription, strAddedToCart, strProductId, userId, userType;

    List<CartListResponse> cartListResponseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        back_arrow = findViewById(R.id.back_arrow);
        addToCartTxtBtn = findViewById(R.id.addToCartTxtBtn);
        productImage = findViewById(R.id.productImage);
        productNameTxt = findViewById(R.id.productNameTxt);
        priceTxt = findViewById(R.id.priceTxt);
        quantityTxt = findViewById(R.id.quantityTxt);
        remaningQuantityTxt = findViewById(R.id.remaningQuantityTxt);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        relative_cart = findViewById(R.id.relative_cart);
        cartTxt = findViewById(R.id.cartTxt);


        back_arrow.setOnClickListener(v -> finish());

        relative_cart.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailsActivity.this, CartListActivity.class);
            startActivity(intent);
        });

        strImage = getIntent().getStringExtra("strImage");
        strName = getIntent().getStringExtra("strName");
        strPrice = getIntent().getStringExtra("strPrice");
        strQuantity = getIntent().getStringExtra("strQuantity");
        strRemaningQuantity = getIntent().getStringExtra("strRemaningQuantity");
        strDescription = getIntent().getStringExtra("strDescription");
        strAddedToCart = getIntent().getStringExtra("strAddedToCart");
        strProductId = getIntent().getStringExtra("strProductId");


        SharedPreferences sharedPreferences = ProductDetailsActivity.this.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");
        userType = sharedPreferences.getString("userType", "");


        String image_url = com.buyer.buyerApp.BuildConfig.API_URL + UrlClass.productServiceImageUrl + strImage;
        Glide.with(ProductDetailsActivity.this).load(image_url)
                .thumbnail(0.5f)
                .into(productImage);

        productNameTxt.setText(strName);
        priceTxt.setText("$" + strPrice);
        quantityTxt.setText(strQuantity);
        remaningQuantityTxt.setText(strRemaningQuantity);
        descriptionTxt.setText(strDescription);


        addToCartTxtBtn.setOnClickListener(v -> {
            if (strAddedToCart.equals("0")) {

                if (checkItemInCartList()) {
                    AddToCart_Api();
                } else {
                    Toast.makeText(ProductDetailsActivity.this, "Item already added in this list", Toast.LENGTH_SHORT).show();

                }

            } else {
                Toast.makeText(ProductDetailsActivity.this, "You have already add this item", Toast.LENGTH_SHORT).show();
            }
        });
        CartList_Api();

    }

    private boolean checkItemInCartList() {
        for (CartListResponse item : cartListResponseList) {

            if (item.getProductId().equals(strProductId)) {
                return false;
            }
        }

        return true;
    }


    private void AddToCart_Api() {
        final ProgressDialog progressDialog = new ProgressDialog(ProductDetailsActivity.this, R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        progressDialog.show();

        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        retrofit2.Call<CommonModel> call = service.getAddToCart("12345",
                userType, userId, strProductId, "1", strPrice, strPrice, strName);
        call.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<CommonModel> call, @NonNull retrofit2.Response<CommonModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success = response.body().getSuccess();

                        if (success.equalsIgnoreCase("true")) {
                            //Toast.makeText(ProductDetailsActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ProductDetailsActivity.this, CartListActivity.class);
                            intent.putExtra("strRemaningQuantity", strRemaningQuantity);
                            startActivity(intent);

                        } else {
                            Toast.makeText(ProductDetailsActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(ProductDetailsActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(ProductDetailsActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(ProductDetailsActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(ProductDetailsActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(ProductDetailsActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(ProductDetailsActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(ProductDetailsActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(ProductDetailsActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } catch (Exception e) {
                        }
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (JsonSyntaxException exception) {
                    exception.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(ProductDetailsActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(ProductDetailsActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void CartList_Api() {
        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        retrofit2.Call<CartListModel> call = service.getCartList("123", userType, userId);
        call.enqueue(new Callback<CartListModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<CartListModel> call, @NonNull retrofit2.Response<CartListModel> response) {
                try {
                    if (response.isSuccessful()) {
                        CartListModel result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();

                        if (success.equalsIgnoreCase("true")) {
                            cartListResponseList = result.getCartListResponse();

                            cartTxt.setText("" + cartListResponseList.size());
                            Log.e("MainActivity", "112    " + cartListResponseList.size());
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(ProductDetailsActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(ProductDetailsActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(ProductDetailsActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(ProductDetailsActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(ProductDetailsActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(ProductDetailsActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(ProductDetailsActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(ProductDetailsActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } catch (Exception e) {
                        }
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (JsonSyntaxException exception) {
                    exception.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CartListModel> call, Throwable t) {

                if (t instanceof IOException) {
                    Toast.makeText(ProductDetailsActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(ProductDetailsActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        CartList_Api();
    }
}