package com.buyer.buyerApp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buyer.buyerApp.Adapter.ProductListAdapter;
import com.buyer.buyerApp.Class.GridSpacingItemDecoration;
import com.buyer.buyerApp.Model.CartListModel;
import com.buyer.buyerApp.Model.ProductModel;
import com.buyer.buyerApp.ModelResult.CartListResponse;
import com.buyer.buyerApp.ModelResult.ProductResult;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.ApiClient;
import com.buyer.buyerApp.RetrofitApi.ApiServices;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ProductListActivity extends AppCompatActivity {

    ImageView back_arrow;
    RecyclerView productListRecycle;
    RelativeLayout relative_cart;
    TextView cartTxt;
    String strCatigoryId="",userId="",userType="";
    ProductListAdapter productListAdapter;
    List<ProductResult> productResultList = new ArrayList<>();
    List<CartListResponse> cartListResponseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);


        back_arrow = findViewById(R.id.back_arrow);

        back_arrow.setOnClickListener(v -> finish());
        productListRecycle = findViewById(R.id.productListRecycle);
        relative_cart = findViewById(R.id.relative_cart);
        cartTxt = findViewById(R.id.cartTxt);

        strCatigoryId = getIntent().getStringExtra("strCatigoryId");

        SharedPreferences sharedPreferences = ProductListActivity.this.getSharedPreferences("PREFERENCE_NAME",MODE_PRIVATE);
        userId = sharedPreferences.getString("userId",userId);
        userType = sharedPreferences.getString("userType",userType);


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ProductListActivity.this,2);
        productListRecycle.setLayoutManager(layoutManager);
        productListRecycle.addItemDecoration(new GridSpacingItemDecoration(2,dpToPx(5),true));
        productListRecycle.setItemAnimator(new DefaultItemAnimator());

        relative_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductListActivity.this, CartListActivity.class);
                startActivity(intent);
            }
        });

        ProductList_Api();
        CartList_Api();

    }
    public void CartList_Api() {
        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        retrofit2.Call<CartListModel> call = service.getCartList("12345", userType, userId);
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

                            cartTxt.setText(""+cartListResponseList.size());
                            Log.e("MainActivity","112    "+cartListResponseList.size());
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(ProductListActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(ProductListActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(ProductListActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(ProductListActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(ProductListActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(ProductListActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(ProductListActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(ProductListActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ProductListActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(ProductListActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void ProductList_Api() {
        final ProgressDialog progressDialog = new ProgressDialog(ProductListActivity.this, R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        progressDialog.show();

        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        retrofit2.Call<ProductModel> call = service.getProduct("123",strCatigoryId,userId,userType);
        call.enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ProductModel> call, @NonNull retrofit2.Response<ProductModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        ProductModel result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();

                        if (success.equalsIgnoreCase("true")) {
                            productResultList = result.getProductResults();
                            productListAdapter = new ProductListAdapter(ProductListActivity.this, productResultList);
                            productListRecycle.setAdapter(productListAdapter);
                        }
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(ProductListActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(ProductListActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(ProductListActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(ProductListActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(ProductListActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(ProductListActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(ProductListActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(ProductListActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<ProductModel> call, Throwable t) {
                progressDialog.dismiss();

                if (t instanceof IOException) {
                    Toast.makeText(ProductListActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(ProductListActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int dpToPx(int dp)
    {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}