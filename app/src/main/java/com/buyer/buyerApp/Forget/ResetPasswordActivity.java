package com.buyer.buyerApp.Forget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buyer.buyerApp.Activity.Login1Activity;
import com.buyer.buyerApp.Model.CommonModel;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.ApiClient;
import com.buyer.buyerApp.RetrofitApi.ApiServices;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

public class ResetPasswordActivity extends AppCompatActivity {
    RelativeLayout relativeResetPass;
    ImageView back_arrow, hidden_password, confirm_hiddenPass;
    EditText passwordEdt, confirmPasswordEdt;
    TextView forgotSubmitTxtBtn;
    private  String userPhoneNumber;
    String strNewPassword, strConfirmPass,userType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        userPhoneNumber = getIntent().getStringExtra("phone_no");
        userType = getIntent().getStringExtra("user_Type");
        Log.e("phone_no", ": " +userPhoneNumber);
        Log.e("user_Type", ": " +userType);

        relativeResetPass = findViewById(R.id.relativeResetPass);
        back_arrow = findViewById(R.id.back_arrow);
        passwordEdt = findViewById(R.id.passwordEdt);
        confirmPasswordEdt = findViewById(R.id.confirmPasswordEdt);
        hidden_password = findViewById(R.id.hidden_password);
        confirm_hiddenPass = findViewById(R.id.confirm_hiddenPass);
        forgotSubmitTxtBtn = findViewById(R.id.forgotSubmitBtn);

        back_arrow.setOnClickListener(v -> finish());

        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            relativeResetPass.setBackgroundDrawable(ContextCompat.getDrawable(ResetPasswordActivity.this, R.drawable.bg));
        } else {
            relativeResetPass.setBackground(ContextCompat.getDrawable(ResetPasswordActivity.this, R.drawable.bg));
        }

        hidden_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.hidden_password) {

                    if (passwordEdt.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                        hidden_password.setImageResource(R.drawable.visibility_on);

                        //Show Password
                        passwordEdt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    } else {
                        ((ImageView) (view)).setImageResource(R.drawable.visibility_off);

                        //Hide Password
                        passwordEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    }
                }
            }
        });

        confirm_hiddenPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.confirm_hiddenPass) {

                    if (confirmPasswordEdt.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                        confirm_hiddenPass.setImageResource(R.drawable.visibility_on);

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

        forgotSubmitTxtBtn.setOnClickListener(v -> {
            strNewPassword = passwordEdt.getText().toString();
            strConfirmPass = confirmPasswordEdt.getText().toString();

            Log.e("phone_no", ": " +strNewPassword);
            Log.e("phone_no1", ": " +strConfirmPass);

            if (strNewPassword.equals("")) {
                Toast toast = Toast.makeText(ResetPasswordActivity.this, getString(R.string.pls_enter_new_password), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (strConfirmPass.equals("")) {
                Toast toast = Toast.makeText(ResetPasswordActivity.this, getString(R.string.pls_enter_confirm_password), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            } else if (!strNewPassword.equals(strConfirmPass)) {
                Toast toast = Toast.makeText(ResetPasswordActivity.this, getString(R.string.please_enter_same_password), Toast.LENGTH_SHORT);
                toast.show();
            }
            else
            {
              ForgotChangePassword_Api();
            }

        });
    }
    private void ForgotChangePassword_Api() {
        final ProgressDialog progressDialog = new ProgressDialog(ResetPasswordActivity.this, R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        progressDialog.show();

        ApiServices service = ApiClient.getClient().create(ApiServices.class);

        retrofit2.Call<CommonModel> call = service.getForgotChangePassword("12345",
                userType,
                strNewPassword,
                userPhoneNumber);
        Log.e("changePassword",userType+ " " +strNewPassword + " " +userPhoneNumber);
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
                        passwordEdt.setText("");
                        confirmPasswordEdt.setText("");

                        if (success.equals("true")) {

                            Intent intent = new Intent(ResetPasswordActivity.this, Login1Activity.class);
                            intent.putExtra("login_key", "login_value");
                            startActivity(intent);
                            finish();

                            Toast.makeText(ResetPasswordActivity.this, message, Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(ResetPasswordActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(ResetPasswordActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(ResetPasswordActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(ResetPasswordActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(ResetPasswordActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(ResetPasswordActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(ResetPasswordActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(ResetPasswordActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(ResetPasswordActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
                    Log.e("ertret", String.valueOf(t));
                    Toast.makeText(ResetPasswordActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}