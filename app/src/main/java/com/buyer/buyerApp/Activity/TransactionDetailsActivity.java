package com.buyer.buyerApp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.buyer.buyerApp.Model.CommonModel;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.ApiClient;
import com.buyer.buyerApp.RetrofitApi.ApiServices;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

public class TransactionDetailsActivity extends AppCompatActivity {

    ImageView transDetailsImg;
    TextView transDetailsName, transDetailsDate, transDetailsAmount, tranDetailsStatus, tranDetailsNumber, transactionDetailsId;

    ImageView back_arrow;
    LinearLayout TransactionLayout,mailinvoicelayout;

    String userType,userId;
    private String strImage="",strServiceName="",strCreatedAt="",strTotalAmount="",strRequestNumber="",
            strStatus="",strPhoneNumber="",strTransactionId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        TransactionLayout = findViewById(R.id.myTransaction);
        back_arrow = findViewById(R.id.back_arrow);

        transDetailsImg = findViewById(R.id.transDetailsImg);
        transDetailsName = findViewById(R.id.transDetailsName);
        transDetailsDate = findViewById(R.id.transDetailsDate);
        transDetailsAmount = findViewById(R.id.transDetailsAmount);
        tranDetailsStatus = findViewById(R.id.tranDetailsStatus);
        tranDetailsNumber = findViewById(R.id.tranDetailsNumber);
        mailinvoicelayout = findViewById(R.id.mailinvoicelayout);
        transactionDetailsId = findViewById(R.id.transactionDetailsId);

        back_arrow.setOnClickListener(v -> finish());


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");
        userType = sharedPreferences.getString("userType", "");

        strImage = getIntent().getStringExtra("strImage");
        strServiceName = getIntent().getStringExtra("strServiceName");
        strCreatedAt = getIntent().getStringExtra("strCreatedAt");
        strTotalAmount = getIntent().getStringExtra("strTotalAmount");
        strRequestNumber = getIntent().getStringExtra("strRequestNumber");
        strStatus = getIntent().getStringExtra("strStatus");
        strPhoneNumber = getIntent().getStringExtra("strPhoneNumber");
        strTransactionId = getIntent().getStringExtra("strTransactionId");


        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            TransactionLayout.setBackgroundDrawable(ContextCompat.getDrawable(TransactionDetailsActivity.this, R.drawable.bg));
        } else {
            TransactionLayout.setBackground(ContextCompat.getDrawable(TransactionDetailsActivity.this, R.drawable.bg));
        }


        mailinvoicelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MailInvoice_Api();
            }
        });


        if (strStatus.equals("0")) {
            tranDetailsStatus.setText("Panding");
            tranDetailsStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.red));
        } else if (strStatus.equals("1")) {
            tranDetailsStatus.setText("Complete");
            tranDetailsStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.green));
        } else if (strStatus.equals("2")) {
            tranDetailsStatus.setText("Cancel");
            tranDetailsStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.red));
        }else if (strStatus.equals("3")) {
            tranDetailsStatus.setText("Inprogress");
            tranDetailsStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.colorYellow));
        }

        String image_url = strImage;
        Glide.with(TransactionDetailsActivity.this).load(image_url)
                .thumbnail(0.5f)
                .into(transDetailsImg);


        transDetailsName.setText(strServiceName);
        transDetailsDate.setText(strCreatedAt);
        transDetailsAmount.setText(strTotalAmount);
        tranDetailsNumber.setText(strPhoneNumber);
        transactionDetailsId.setText(strTransactionId);


    }

    private void MailInvoice_Api() {
        final ProgressDialog progressDialog = new ProgressDialog(TransactionDetailsActivity.this, R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        progressDialog.show();

        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        retrofit2.Call<CommonModel> call = service.getMailInvooice("123",userType,userId,strRequestNumber);
        call.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<CommonModel> call, @NonNull retrofit2.Response<CommonModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success = response.body().getSuccess();

                        if (success.equalsIgnoreCase("true")) {
                            Toast.makeText(TransactionDetailsActivity.this,  message, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(TransactionDetailsActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(TransactionDetailsActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(TransactionDetailsActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(TransactionDetailsActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(TransactionDetailsActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(TransactionDetailsActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(TransactionDetailsActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(TransactionDetailsActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(TransactionDetailsActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(TransactionDetailsActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(TransactionDetailsActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}