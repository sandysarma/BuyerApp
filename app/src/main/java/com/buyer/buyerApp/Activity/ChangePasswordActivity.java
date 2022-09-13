package com.buyer.buyerApp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
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

public class ChangePasswordActivity extends AppCompatActivity {

    ImageView back_arrow,background ,oldPasswordImg,newPasswordImg,confirmPasswordImg;
    EditText oldPasswordEdt,newPasswordEdt,confirmPasswordEdt;
    TextView ChangePasswordBtn;
    String strOldPassword = "", strNewPassword = "", strConfirmPassword = "", userId = "", userType = "";
    ScrollView changePasswordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        back_arrow = findViewById(R.id.back_arrow);
        background =findViewById(R.id.background);
        Glide.with(ChangePasswordActivity.this).load(R.drawable.bg).into(background);

        oldPasswordEdt = findViewById(R.id.oldPassword);
        newPasswordEdt = findViewById(R.id.newPassword);
        confirmPasswordEdt = findViewById(R.id.confirmPassword);
        ChangePasswordBtn = findViewById(R.id.ChangePasswordBtn);
        oldPasswordImg = findViewById(R.id.oldPasswordImg);
        newPasswordImg = findViewById(R.id.newPasswordImg);
        confirmPasswordImg = findViewById(R.id.confirmPasswordImg);

        SharedPreferences sharedPreferences = ChangePasswordActivity.this.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");
        userType = sharedPreferences.getString("userType", "");


        back_arrow.setOnClickListener(v -> finish());

        ChangePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strOldPassword = oldPasswordEdt.getText().toString();
                strNewPassword = newPasswordEdt.getText().toString();
                strConfirmPassword = confirmPasswordEdt.getText().toString();

                if (strOldPassword.isEmpty())
                {
                    Toast toast = Toast.makeText(ChangePasswordActivity.this, getString(R.string.pls_enter_old_password), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else if (strNewPassword.isEmpty())
                {
                    Toast toast = Toast.makeText(ChangePasswordActivity.this, getString(R.string.pls_enter_new_password), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                else if (strConfirmPassword.isEmpty())
                {
                    Toast toast = Toast.makeText(ChangePasswordActivity.this, getString(R.string.pls_enter_confirm), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else if (!strNewPassword.equals(strConfirmPassword)) {
                    Toast toast = Toast.makeText(ChangePasswordActivity.this, getString(R.string.newpassword_and_confirm_password_should_be_same), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                else {
                    ChangePassword_Api();
                }

            }
        });
        oldPasswordImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.oldPasswordImg) {

                    if (oldPasswordEdt.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                        oldPasswordImg.setImageResource(R.drawable.visibility_on);

                        //Show Password
                        oldPasswordEdt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    } else {
                        ((ImageView) (view)).setImageResource(R.drawable.visibility_off);

                        //Hide Password
                        oldPasswordEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    }
                }
            }
        });
        newPasswordImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.newPasswordImg)
                {

                    if (newPasswordEdt.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                        newPasswordImg.setImageResource(R.drawable.visibility_on);

                        //Show Password
                        newPasswordEdt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    } else {
                        ((ImageView) (view)).setImageResource(R.drawable.visibility_off);

                        //Hide Password
                        newPasswordEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    }
                }
            }
        });
        confirmPasswordImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.confirmPasswordImg) {

                    if (confirmPasswordEdt.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                        confirmPasswordImg.setImageResource(R.drawable.visibility_on);

                        //Show Password
                        confirmPasswordEdt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    } else {
                        ((ImageView) (view)).setImageResource(R.drawable.visibility_off);

                        //Hide Password
                        confirmPasswordEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    }
                }
            }
        });

    }
    private void ChangePassword_Api() {
        final ProgressDialog progressDialog = new ProgressDialog(ChangePasswordActivity.this, R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        progressDialog.show();

        ApiServices service = ApiClient.getClient().create(ApiServices.class);

        retrofit2.Call<CommonModel> call = service.getChangePassword("123", userType,
                userId, strOldPassword, strNewPassword);
        //calling the api
        call.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(retrofit2.Call<CommonModel> call, retrofit2.Response<CommonModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        CommonModel result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();

                        if (success.equals("true")) {
                            Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_LONG).show();
                            oldPasswordEdt.setText("");
                            newPasswordEdt.setText("");
                            confirmPasswordEdt.setText("");
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(ChangePasswordActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(ChangePasswordActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(ChangePasswordActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(ChangePasswordActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(ChangePasswordActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(ChangePasswordActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(ChangePasswordActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(ChangePasswordActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<CommonModel> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(ChangePasswordActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}