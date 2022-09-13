package com.buyer.buyerApp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buyer.buyerApp.Forget.ForgotPasswordActivity;
import com.buyer.buyerApp.MainActivity;
import com.buyer.buyerApp.Model.LoginGroupModel;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.ApiClient;
import com.buyer.buyerApp.RetrofitApi.ApiServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

public class Login1Activity extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton rdo_individual, rdo_group;
    String strIndividualCorporateType = "";
    TextView loginTxtBtn, signUpTxt, forgotTxt;
    EditText emailEdt, passwordEdt;
    RelativeLayout loginLayout;
    LinearLayout passwordLayout;
    ImageView password_show;
    private boolean isClick = true;
    String strEmail = "", strPassword = "", device_tokens = "",strIndividualGroupType = "1";
    private Context context;
    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);

        FirebaseApp.initializeApp(this);
        context = Login1Activity.this;
        init();

        radioGroup = findViewById(R.id.radioGroup);
        rdo_individual = findViewById(R.id.rdo_individual);
        rdo_group = findViewById(R.id.rdo_group);
        loginTxtBtn = findViewById(R.id.loginTxtBtn);
        signUpTxt = findViewById(R.id.signUpTxt);
        forgotTxt = findViewById(R.id.forgotTxt);
        loginLayout = findViewById(R.id.loginLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        password_show = findViewById(R.id.password_show);
        emailEdt = findViewById(R.id.emailEdt);
        passwordEdt = findViewById(R.id.passwordEdt);

        final int sdk = Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            loginLayout.setBackgroundDrawable(ContextCompat.getDrawable(Login1Activity.this, R.drawable.bg));
        } else {
            loginLayout.setBackground(ContextCompat.getDrawable(Login1Activity.this, R.drawable.bg));
        }

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (rdo_individual.isChecked()) {
                strIndividualCorporateType = rdo_individual.getText().toString();
                strIndividualGroupType = "1";
            } else if (rdo_group.isChecked()) {
                strIndividualCorporateType = rdo_group.getText().toString();
                strIndividualGroupType = "2";
            }
        });

        password_show.setImageResource(R.drawable.visibility_off);
        passwordLayout.setOnClickListener(v -> {
            if (!isClick) {
                isClick = true;
                password_show.setImageResource(R.drawable.visibility_off);
                passwordEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                passwordEdt.setSelection(passwordEdt.getText().length());
            } else {
                isClick = false;
                password_show.setImageResource(R.drawable.visibility_on);
                passwordEdt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                passwordEdt.setSelection(passwordEdt.getText().length());
            }
        });

        loginTxtBtn.setOnClickListener(v -> {
            strEmail = emailEdt.getText().toString();
            strPassword = passwordEdt.getText().toString();

            if (strIndividualGroupType.equals("1")) {
                if (strEmail.matches("")) {
                    Toast toast = Toast.makeText(Login1Activity.this, getString(R.string.pls_enter_phone_number), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else if (strEmail.length()<7) {
                    Toast toast = Toast.makeText(Login1Activity.this, getString(R.string.phone_no_minimum_length), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if (strEmail.length()>13) {
                    Toast toast = Toast.makeText(Login1Activity.this, getString(R.string.phone_no_maximum_length), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                } else if (strPassword.matches("")) {
                    Toast toast = Toast.makeText(Login1Activity.this, getString(R.string.pls_enter_password), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    String countryCode = strEmail.substring(0,3);
                    String countryCode1 = strEmail.substring(0,4);
                    String zero = strEmail.substring(0,1);

                    LoginIndividualApi();

                }
            } else if (strIndividualGroupType.equals("2")) {

                if (strEmail.matches("")) {
                    Toast toast = Toast.makeText(Login1Activity.this, getString(R.string.pls_enter_phone_number), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (strPassword.matches("")) {
                    Toast toast = Toast.makeText(Login1Activity.this, getString(R.string.pls_enter_password), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {



                    LoginGroupApi();
                }
            }
        });

        signUpTxt.setOnClickListener(v -> {
            Intent intent = new Intent(Login1Activity.this, SignUpActivity.class);
            startActivity(intent);
        });

        forgotTxt.setOnClickListener(v ->
        {
            Intent intent = new Intent(Login1Activity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

    }

    private void init() {

        FirebaseApp.getApps(context);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(Login1Activity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                success(instanceIdResult);
            }

            private void success(InstanceIdResult instanceIdResult) {
                // Get new Instance ID token
                device_tokens = instanceIdResult.getToken();
                Log.e("tokens", "tok" + device_tokens);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("tokens", "tok" + e.getMessage());

                if (Build.VERSION_CODES.KITKAT > Build.VERSION.SDK_INT) {
                    failure();
                } else {
                    device_tokens = "123456";
                }
            }

            private void failure() {
                init();
            }
        });
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

    }

    private void LoginGroupApi() {

        String withoutConryCodeNumberGroup = "";

        withoutConryCodeNumberGroup = "+91"+strEmail;

        final ProgressDialog progressDialog = new ProgressDialog(Login1Activity.this, R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        progressDialog.show();

        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        retrofit2.Call<LoginGroupModel> call = service.getLoginGroup(withoutConryCodeNumberGroup, strPassword,
                device_tokens, "12345");
        call.enqueue(new Callback<LoginGroupModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<LoginGroupModel> call, @NonNull retrofit2.Response<LoginGroupModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success = response.body().getSuccess();
                        String userId = String.valueOf(response.body().getUserId());
                        Toast.makeText(Login1Activity.this, message, Toast.LENGTH_LONG).show();


                        if (success.equalsIgnoreCase("true")) {

                            sharedPreferences = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString("userId", userId);
                            editor.putString("userType", strIndividualGroupType);
                            editor.apply();
                            Log.e("userId", userId+"  "+strIndividualGroupType);

                            Intent intent = new Intent(Login1Activity.this, MainActivity.class);
                            intent.putExtra("login_key", "login_value");
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Login1Activity.this, message, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(Login1Activity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(Login1Activity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(Login1Activity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(Login1Activity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(Login1Activity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(Login1Activity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(Login1Activity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(Login1Activity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<LoginGroupModel> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(Login1Activity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(Login1Activity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void LoginIndividualApi() {

        String withoutConryCodeNumberIndividual = "";

        withoutConryCodeNumberIndividual = "+91"+strEmail;
        Log.e("Number final login ","=== "+withoutConryCodeNumberIndividual);

        final ProgressDialog progressDialog = new ProgressDialog(Login1Activity.this, R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        progressDialog.show();

        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        retrofit2.Call<LoginGroupModel> call = service.getLoginIndividual(withoutConryCodeNumberIndividual, strPassword,
                device_tokens, device_tokens);
        call.enqueue(new Callback<LoginGroupModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<LoginGroupModel> call, @NonNull retrofit2.Response<LoginGroupModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success = response.body().getSuccess();
                        String userId = String.valueOf(response.body().getUserId());
                        Toast.makeText(Login1Activity.this, message, Toast.LENGTH_LONG).show();


                        if (success.equalsIgnoreCase("true")) {

                            sharedPreferences = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString("userId", userId);
                            editor.putString("userType", strIndividualGroupType);
                            Log.e("userId", userId+"  "+strIndividualGroupType);

                            editor.apply();

                            Intent intent = new Intent(Login1Activity.this, MainActivity.class);
                            intent.putExtra("login_key", "login_value");
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Login1Activity.this, message, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(Login1Activity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(Login1Activity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(Login1Activity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(Login1Activity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(Login1Activity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(Login1Activity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(Login1Activity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(Login1Activity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<LoginGroupModel> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(Login1Activity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(Login1Activity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("Login", "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e("Login", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Login", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("Login", "onStop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Login", "onStart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Login", "onDestroy");
    }
}
