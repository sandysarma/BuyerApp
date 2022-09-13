package com.buyer.buyerApp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.buyer.buyerApp.Adapter.ServiceListAdapter;
import com.buyer.buyerApp.Class.GridSpacingItemDecoration;
import com.buyer.buyerApp.Model.ServicesModel;
import com.buyer.buyerApp.ModelResult.ServicesResult;
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

public class ServiceListActivity extends AppCompatActivity {

    ImageView back_arrow;
    RecyclerView service_list_recycle;
    ServiceListAdapter serviceListAdapter;

    List<ServicesResult> servicesResponseList = new ArrayList<>();


    String strCatigoryId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);

        back_arrow = findViewById(R.id.back_arrow);

     back_arrow.setOnClickListener(v -> finish());
     service_list_recycle = findViewById(R.id.service_list_recycle);
        strCatigoryId = getIntent().getStringExtra("strCatigoryId");

        RecyclerView.LayoutManager servicesLayoutManager = new GridLayoutManager(ServiceListActivity.this, 2);
        service_list_recycle.setLayoutManager(servicesLayoutManager);
        service_list_recycle.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(5), true));
        service_list_recycle.setItemAnimator(new DefaultItemAnimator());

        ServiceList_Api();

    }

    private void ServiceList_Api() {
        final ProgressDialog progressDialog = new ProgressDialog(ServiceListActivity.this, R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        progressDialog.show();

        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        retrofit2.Call<ServicesModel> call = service.getServices("123",strCatigoryId);
        call.enqueue(new Callback<ServicesModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ServicesModel> call, @NonNull retrofit2.Response<ServicesModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        ServicesModel result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();

                        if (success.equalsIgnoreCase("true")) {
                            servicesResponseList = result.getServicesResults();
                            serviceListAdapter = new ServiceListAdapter(servicesResponseList, ServiceListActivity.this);
                            service_list_recycle.setAdapter(serviceListAdapter);
                        }
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(ServiceListActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(ServiceListActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(ServiceListActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(ServiceListActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(ServiceListActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(ServiceListActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(ServiceListActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(ServiceListActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<ServicesModel> call, Throwable t) {
                progressDialog.dismiss();

                if (t instanceof IOException) {
                    Toast.makeText(ServiceListActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(ServiceListActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}