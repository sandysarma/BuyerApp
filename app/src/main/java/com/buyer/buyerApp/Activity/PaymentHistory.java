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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buyer.buyerApp.MainActivity;
import com.buyer.buyerApp.Model.CommonModel;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.ApiClient;
import com.buyer.buyerApp.RetrofitApi.ApiServices;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

public class PaymentHistory extends AppCompatActivity {
TextView submit_btn;
EditText phone_edt;

    ImageView back_arrow;
    private String strphone_edt;
    String strsName = "", strImage = "", strsPrice = "", url;

    String strLandSize = "", strLandType = "", strNumberOfDay = "", strLocation = "", strStartDate = "",
            strCategoryId = "", strServiceId = "", userId = "", userType = "", strTotalAmount = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);

        back_arrow = findViewById(R.id.back_arrow);
        submit_btn = findViewById(R.id.submit_btn);

        phone_edt = findViewById(R.id.phone_edt);

        back_arrow.setOnClickListener(v -> finish());


        strsName = getIntent().getStringExtra("strName");
        strsPrice = getIntent().getStringExtra("strPrice");
        strImage = getIntent().getStringExtra("strImg");
        url = getIntent().getStringExtra("url");
        strCategoryId = getIntent().getStringExtra("strCategoryId");
        strServiceId = getIntent().getStringExtra("strServiceId");
        strTotalAmount = getIntent().getStringExtra("totalAmount");
        strLandSize = getIntent().getStringExtra("landSize");
        strLandType = getIntent().getStringExtra("landType");
        strStartDate = getIntent().getStringExtra("date");
        strNumberOfDay = getIntent().getStringExtra("noOFDays");
        strLocation = getIntent().getStringExtra("location");

        SharedPreferences sharedPreferences = PaymentHistory.this.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");
        userType = sharedPreferences.getString("userType", "");

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strphone_edt = phone_edt.getText().toString();

                if (strphone_edt.equals("")){
                    Toast toast = Toast.makeText(PaymentHistory.this, getString(R.string.pls_enter_acount_id), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else
                {
                    SendServiceRequestApi();
                }
            }
        });

    }
    private void SendServiceRequestApi() {
        final ProgressDialog progressDialog = new ProgressDialog(PaymentHistory.this, R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        progressDialog.show();

        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        retrofit2.Call<CommonModel> call = service.getServiceRequest("123",
                strCategoryId, strServiceId, userType, userId,
                strLandSize, strLandType, strLocation, strStartDate, strNumberOfDay, strTotalAmount);
        call.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<CommonModel> call, @NonNull retrofit2.Response<CommonModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success = response.body().getSuccess();

                        if (success.equalsIgnoreCase("true")) {
                            Toast.makeText(PaymentHistory.this, "" + message, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PaymentHistory.this, MainActivity.class);
                            intent.putExtra("login_key", "login_value");
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(PaymentHistory.this, message, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(PaymentHistory.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(PaymentHistory.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(PaymentHistory.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(PaymentHistory.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(PaymentHistory.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(PaymentHistory.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(PaymentHistory.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(PaymentHistory.this, "unknown error", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(PaymentHistory.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(PaymentHistory.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}