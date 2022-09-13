package com.buyer.buyerApp.Forget;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.buyer.buyerApp.Model.CommonModel;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.ApiClient;
import com.buyer.buyerApp.RetrofitApi.ApiServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;


public class ForgotPasswordActivity extends AppCompatActivity {

    RelativeLayout relativeLogin;
    LinearLayout back_arrow_button;
    ImageView image;
    EditText groupforgtPhonetxt, forgtPhonetxt;
    TextView forgetTxt;
    String userType = "", phoneNumber = "", strGroupphoneNumber = "", password = "";
    RadioGroup radioGroup;
    RadioButton rdo_individual, rdo_group;
    LinearLayout individualFramerLayout, groupFramerLayout, institutionFramerLayout;
    String strIndividualCorporateType = "", strIndividualGroupType = "1", strIndividualGroupInstituteType = "1";
    ViewFlipper viewFlipper;
    TextView Verify_layout, skiptxt;
    EditText numOne, numTwo, numThree, numFour, numFive, numSix;
    private FirebaseAuth mAuth;
    private String verificationId;
    private Context context;
    String device_tokens = "12345";
    private String newPhoneNumber, checkPhoneNumber="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);


        mAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);
        context = ForgotPasswordActivity.this;
//        init();

        relativeLogin = findViewById(R.id.relativeLogin);
        radioGroup = findViewById(R.id.radioGroup);
        rdo_individual = findViewById(R.id.rdo_individual);
        rdo_group = findViewById(R.id.rdo_group);
        viewFlipper = findViewById(R.id.view_flip);
        Verify_layout = findViewById(R.id.Verify_layout);
        individualFramerLayout = findViewById(R.id.individualFramerLayout);
        groupFramerLayout = findViewById(R.id.groupFramerLayout);
        institutionFramerLayout = findViewById(R.id.institutionFramerLayout);


        image = findViewById(R.id.image);
        back_arrow_button = findViewById(R.id.back_arrow_button);
        forgtPhonetxt = findViewById(R.id.forgtPhonetxt);
        groupforgtPhonetxt = findViewById(R.id.groupforgtPhonetxt);
        forgetTxt = findViewById(R.id.forgetTxt);


        SharedPreferences sharedPreferences = ForgotPasswordActivity.this.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        userType = sharedPreferences.getString("userType", "");


        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            relativeLogin.setBackgroundDrawable(ContextCompat.getDrawable(ForgotPasswordActivity.this, R.drawable.bg));
        } else {
            relativeLogin.setBackground(ContextCompat.getDrawable(ForgotPasswordActivity.this, R.drawable.bg));
        }

        Glide.with(this).load(R.drawable.logo).into(image);

        back_arrow_button.setOnClickListener(v -> finish());

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (rdo_individual.isChecked()) {
                individualFramerLayout.setVisibility(View.VISIBLE);
                groupFramerLayout.setVisibility(View.GONE);
                strIndividualGroupInstituteType = "1";
            } else if (rdo_group.isChecked()) {
                individualFramerLayout.setVisibility(View.GONE);
                groupFramerLayout.setVisibility(View.VISIBLE);
                strIndividualGroupInstituteType = "2";
            }
            Log.e("fsdgf", "strIndividualGroupInstituteType: " + strIndividualGroupInstituteType);

        });


        codenumber();
        Verify_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCodes(v);
            }
        });

        forgetTxt.setOnClickListener(v -> {
            phoneNumber = forgtPhonetxt.getText().toString();
            strGroupphoneNumber = groupforgtPhonetxt.getText().toString();

            userType = strIndividualGroupInstituteType;
            if (strIndividualGroupInstituteType.equals("1")) {

                Individual_Validation();
            } else if (strIndividualGroupInstituteType.equals("2")) {

                Grouop_Validation();
            }

        });

    }
    public void Individual_Validation() {

        if (phoneNumber.matches("")) {
            Toast toast = Toast.makeText(ForgotPasswordActivity.this, getString(R.string.pls_enter_phone_number), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        } else if (phoneNumber.length() < 10) {
            forgtPhonetxt.setError("Please enter valid phone number");
            forgtPhonetxt.requestFocus();
        } else {


            newPhoneNumber = phoneNumber;
            ForgotPasswordCheck_Api();
        }

    }

    public void Grouop_Validation() {
        if (strGroupphoneNumber.matches("")) {
            Toast toast = Toast.makeText(ForgotPasswordActivity.this, getString(R.string.pls_enter_phone_number), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        } else if (strGroupphoneNumber.length() < 10) {
            groupforgtPhonetxt.setError("Plesae enter valid phone number");
            groupforgtPhonetxt.requestFocus();
        } else {

            newPhoneNumber = strGroupphoneNumber;
            ForgotPasswordCheck_Api();
        }

    }

    private void sendVerificationCode(String number) {
        //   progressDialog.show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

    }

    private void ForgotPasswordInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(), "OTP verification successful", Toast.LENGTH_SHORT).show();

                            // ForgotPasswordCheck_Api();
                            Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                            intent.putExtra("phone_no", checkPhoneNumber);
                            intent.putExtra("user_Type", userType);
                            startActivity(intent);
                            //  progressDialog.dismiss();

                        } else {
                            // progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Wrong OTP !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void verifyCodes(View view) {
        String code = "" + numOne.getText().toString() + numTwo.getText().toString() + numThree.getText().toString() + numFour.getText().toString() + numFive.getText().toString() + numSix.getText().toString();
//        Toast.makeText(ForgotPasswordActivity.this, code, Toast.LENGTH_SHORT).show();
        if (!code.equals("")) {

            Log.e("bhu", "running phase 1");
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

            ForgotPasswordInWithPhoneAuthCredential(credential);
        } else {
            Toast.makeText(this, "Enter the Correct verification Code", Toast.LENGTH_SHORT).show();
        }
    }

    public void codenumber() {
        numOne = findViewById(R.id.numone);
        numTwo = findViewById(R.id.numtwo);
        numThree = findViewById(R.id.numthree);
        numFour = findViewById(R.id.numfour);
        numFive = findViewById(R.id.numfive);
        numSix = findViewById(R.id.numsix);

        numOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (numOne.getText().toString().length() == 0) {
                    numTwo.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        numTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (numTwo.getText().toString().length() == 0) {
                    numThree.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (numTwo.getText().toString().length() == 0) {
                    numOne.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (numTwo.getText().toString().length() == 0) {
                    numOne.requestFocus();
                }

            }
        });

        numThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (numThree.getText().toString().length() == 0) {
                    numFour.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (numThree.getText().toString().length() == 0) {
                    numTwo.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (numThree.getText().toString().length() == 0) {
                    numTwo.requestFocus();
                }

            }
        });

        numFour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (numFour.getText().toString().length() == 0) {
                    numFive.requestFocus();
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (numFour.getText().toString().length() == 0) {
                    numThree.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (numFour.getText().toString().length() == 0) {
                    numThree.requestFocus();
                }

            }
        });

        numFive.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (numFive.getText().toString().length() == 0) {
                    numSix.requestFocus();
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (numFive.getText().toString().length() == 0) {
                    numFour.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (numFive.getText().toString().length() == 0) {
                    numFour.requestFocus();
                }
            }
        });

        numSix.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (numSix.getText().toString().length() == 0) {
                    numFive.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (numSix.getText().toString().length() == 0) {
                    numFive.requestFocus();
                }
            }
        });

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
           /* Intent intent=new Intent(getApplicationContext(),Verification_Activity.class);
            startActivity(intent);*/
            verificationId = s;
            viewFlipper.setDisplayedChild(1);
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();
            ForgotPasswordInWithPhoneAuthCredential(phoneAuthCredential);
            // checking if the code
            // is null or not.
            //verificationId = s;
            //viewFlipper.setDisplayedChild(1);

            if (code != null) {
                //   Forget_Login();
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                //edtOTP.setText(code);

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                //verifyCode(code);
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(ForgotPasswordActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void init() {
        FirebaseApp.getApps(context);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(ForgotPasswordActivity.this, new OnSuccessListener<InstanceIdResult>() {
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

    private void next_button(View view) {
//        Toast.makeText(ForgotPasswordActivity.this, "calling this validation2", Toast.LENGTH_SHORT).show();
        //sendVerificationCode("+91"+phoneNumber);
        Log.e("bhfu", (phoneNumber));


        Log.e("bhfu", "newPhoneNumber: " + (newPhoneNumber));

    }
    private void ForgotPasswordCheck_Api() {

        checkPhoneNumber = "+91" + newPhoneNumber;
        // checkPhoneNumber = "+91"+newPhoneNumber;
        Log.e("checkPhoneNumber", checkPhoneNumber);

        final ProgressDialog progressDialog = new ProgressDialog(ForgotPasswordActivity.this, R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        progressDialog.show();

        ApiServices service = ApiClient.getClient().create(ApiServices.class);

        retrofit2.Call<CommonModel> call = service.getForgotCheckPassword(device_tokens, userType, checkPhoneNumber);
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
//                            Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_LONG).show();
                            forgtPhonetxt.setText("");
                            groupforgtPhonetxt.setText("");
                            sendVerificationCode(checkPhoneNumber);

                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(ForgotPasswordActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(ForgotPasswordActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(ForgotPasswordActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(ForgotPasswordActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(ForgotPasswordActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(ForgotPasswordActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(ForgotPasswordActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(ForgotPasswordActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
                    Log.e("Forgotpassword", String.valueOf(t));

                    Toast.makeText(ForgotPasswordActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}