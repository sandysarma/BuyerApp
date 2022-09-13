package com.buyer.buyerApp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.buyer.buyerApp.BuildConfig;
import com.buyer.buyerApp.MainActivity;
import com.buyer.buyerApp.Model.CommonModel;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.ApiClient;
import com.buyer.buyerApp.RetrofitApi.ApiServices;
import com.buyer.buyerApp.RetrofitApi.UrlClass;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class ProceedToCheckActivity extends AppCompatActivity {


    TextView skipServiceBtn, buynowbtn, makepaymentbtn,payNumberOfDays,payAmount,proceedDateTxt,proceedAddressTxt,proceedNameTxt;
    ImageView background, back_arrow_mode;
    CircleImageView cardpaidImage;
    AlertDialog dialogs;
    String strCaseOnDelaveryType, strOrangeMoneyType;
    String strsName = "", strImage = "", strsPrice = "", url;
    String stPaymentModeMoneyType = "1";
    private String strCategoryId, strServiceId, userType, userId,
            strLandSize, strLandType, strLocation, strStartDate, strNumberOfDay, strTotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed_to_check);

        SharedPreferences sharedPreferences = ProceedToCheckActivity.this.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");
        userType = sharedPreferences.getString("userType", "");


        skipServiceBtn = findViewById(R.id.skipServiceBtn);
        background = findViewById(R.id.background);
        buynowbtn = findViewById(R.id.buynowbtn);
        makepaymentbtn = findViewById(R.id.makepaymentbtn);
        back_arrow_mode = findViewById(R.id.back_arrow_mode);
        payNumberOfDays = findViewById(R.id.payNumberOfDays);
        payAmount = findViewById(R.id.payAmount);
        proceedDateTxt = findViewById(R.id.proceedDateTxt);
        proceedAddressTxt = findViewById(R.id.proceedAddressTxt);
        proceedNameTxt = findViewById(R.id.proceedNameTxt);
        cardpaidImage = findViewById(R.id.cardpaidImage);


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


        Glide.with(ProceedToCheckActivity.this).load(R.drawable.bg).into(background);


        Log.e("strImg", strImage + " ");

        if(url.equals("0"))
        {
            String image_url = strImage;
            Glide.with(ProceedToCheckActivity.this).load(image_url)
                    .thumbnail(0.5f)
                    .into(cardpaidImage);
        }
        else
        {
            String image_url = BuildConfig.API_URL + UrlClass.categoryServiceImageUrl + strImage;
            Glide.with(ProceedToCheckActivity.this).load(image_url)
                    .thumbnail(0.5f)
                    .into(cardpaidImage);
        }

        proceedNameTxt.setText(strsName);
        payAmount.setText(""+"$"+ strsPrice);
        proceedDateTxt.setText(strStartDate);
        proceedAddressTxt.setText(strLocation);
        payNumberOfDays.setText((strNumberOfDay +"days"));



        skipServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buynowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LayoutInflater inflater = ProceedToCheckActivity.this.getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.payment_mode_layout, null);
                TextView MakePaymentbtn = alertLayout.findViewById(R.id.makepaymentbtn);
                RadioGroup radioGroupp = alertLayout.findViewById(R.id.radio_group);
                RadioButton btnradio1 = alertLayout.findViewById(R.id.codbtntxt);
                ImageView back_arrow_mode = alertLayout.findViewById(R.id.back_arrow_mode);
                RadioButton btnradio2 = alertLayout.findViewById(R.id.orangebtn);
                final AlertDialog.Builder alert = new AlertDialog.Builder(ProceedToCheckActivity.this);
                alert.setView(alertLayout);
                alert.setCancelable(false);

                back_arrow_mode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                radioGroupp.setOnCheckedChangeListener((group, checkedId) -> {
                    if (btnradio1.isChecked()) {
                        strCaseOnDelaveryType = btnradio1.getText().toString();
                        stPaymentModeMoneyType = "1";

                    } else if (btnradio2.isChecked()) {
                        strOrangeMoneyType = btnradio2.getText().toString();
                        stPaymentModeMoneyType = "2";
                    }
                });

                MakePaymentbtn.setOnClickListener(v1 -> {
                    if (stPaymentModeMoneyType.equals("1")) {
                        dialogs.dismiss();
                       SendServiceRequestApi();

                    } else if (stPaymentModeMoneyType.equals("2")) {
                        Intent intent = new Intent(ProceedToCheckActivity.this, PaymentHistory.class);

                        intent.putExtra("strPrice",strsPrice);
                        intent.putExtra("strName",strsName);
                        intent.putExtra("strImg",strImage);
                        intent.putExtra("strCategoryId",strCategoryId);
                        intent.putExtra("url",url);
                        intent.putExtra("totalAmount",strTotalAmount);
                        intent.putExtra("strServiceId",strServiceId);
                        intent.putExtra("landSize",strLandSize);
                        intent.putExtra("landType",strLandType);
                        intent.putExtra("date",strStartDate);
                        intent.putExtra("noOFDays",strNumberOfDay);
                        intent.putExtra("location",strLocation);
                        startActivity(intent);
                        dialogs.dismiss();
                    }
                });

                dialogs = alert.create();
                dialogs.show();
            }
        });
    }

    private void SendServiceRequestApi() {
        final ProgressDialog progressDialog = new ProgressDialog(ProceedToCheckActivity.this, R.style.full_screen_dialog);
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
                            Toast.makeText(ProceedToCheckActivity.this, "" + message, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ProceedToCheckActivity.this, MainActivity.class);
                            intent.putExtra("login_key", "login_value");
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(ProceedToCheckActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(ProceedToCheckActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(ProceedToCheckActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(ProceedToCheckActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(ProceedToCheckActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(ProceedToCheckActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(ProceedToCheckActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(ProceedToCheckActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(ProceedToCheckActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ProceedToCheckActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(ProceedToCheckActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}