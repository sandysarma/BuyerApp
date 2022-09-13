package com.buyer.buyerApp.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.buyer.buyerApp.Model.CommonModel;
import com.buyer.buyerApp.Model.SignUpGroupFarmerModel;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.ApiClient;
import com.buyer.buyerApp.RetrofitApi.ApiServices;
import com.buyer.buyerApp.Util.SharedPrefClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonSyntaxException;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;


public class SignUpActivity extends AppCompatActivity {

    SharedPrefClass sharedPrefClass;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private String verificationId;
    ViewFlipper viewFlipper;
    EditText numOne, numTwo, numThree, numFour, numFive, numSix;
        TextView verify_layout;
    TextView country_code;
    EditText nameOfFarmerEdt, emailIndividualEdt, phoneIndividualEdt, districtIndividualEdt,
            chiefdomIndividualEdt, sectionIndividualEdt, townIndividualEdt, passwordEdt, confirmPasswordEdt;
    EditText nameGroupEdt, leaderGroupEdt, phoneGroupEdt, emailGroupEdt, districtGroupEdt, chiefdomGroupEdt,
            sectionGroupEdt, townGroupEdt;

    TextView signUpTxtBtn, loginTxt;
    LinearLayout signupLayout, passwordLayout, confirmPasswordLayout, back_arrow_button,
            individualFramerLayout, groupFramerLayout, institutionFramerLayout;

    ImageView password_show, confirm_password_show;
    private boolean isClick = true, isClickConfirm = true;
    String strNameOfFarmer, strEmail, strPhoneIndividual, strDistrictIndividual,
            strChiefdomIndividual, strSectionIndividual, strTownIndividual, strPassword,
            strConfirmPassword;
    String strNameGroup = "", strLeaderGroup = "", strPhoneGroup = "", strEmailGroup = "", strDistrictGroup = "",
            strChiefdomGroup = "", strSectionGroup = "", strTownGroup = "";
    String  strIndividualGroupInstituteType = "1";

    RadioGroup radioGroup;
    RadioButton rdo_individual, rdo_group, rdo_institution;

    private Context context;
    String device_tokens = "";
    private String newPhoneNumber;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        mAuth = FirebaseAuth.getInstance();

        FirebaseApp.initializeApp(this);

        context = SignUpActivity.this;
        init();

        sharedPrefClass = new SharedPrefClass(this);
        verify_layout = findViewById(R.id.Verify_layout);
        nameOfFarmerEdt = findViewById(R.id.nameOfFarmerEdt);
        viewFlipper = findViewById(R.id.view_fkip);
        emailIndividualEdt = findViewById(R.id.emailIndividualEdt);
        phoneIndividualEdt = findViewById(R.id.phoneIndividualEdt);
        districtIndividualEdt = findViewById(R.id.districtIndividualEdt);
        chiefdomIndividualEdt = findViewById(R.id.chiefdomIndividualEdt);
        sectionIndividualEdt = findViewById(R.id.sectionIndividualEdt);
        townIndividualEdt = findViewById(R.id.townIndividualEdt);

        nameGroupEdt = findViewById(R.id.nameGroupEdt);
        leaderGroupEdt = findViewById(R.id.leaderGroupEdt);
        phoneGroupEdt = findViewById(R.id.phoneGroupEdt);
        emailGroupEdt = findViewById(R.id.emailGroupEdt);
        districtGroupEdt = findViewById(R.id.districtGroupEdt);
        chiefdomGroupEdt = findViewById(R.id.chiefdomGroupEdt);
        sectionGroupEdt = findViewById(R.id.sectionGroupEdt);
        townGroupEdt = findViewById(R.id.townGroupEdt);



        passwordEdt = findViewById(R.id.passwordEdt);
        confirmPasswordEdt = findViewById(R.id.confirmPasswordEdt);
        signUpTxtBtn = findViewById(R.id.signUpTxtBtn);
        loginTxt = findViewById(R.id.loginTxt);
        password_show = findViewById(R.id.password_show);
        confirm_password_show = findViewById(R.id.confirm_password_show);

        signupLayout = findViewById(R.id.signupLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);
        back_arrow_button = findViewById(R.id.back_arrow_button);
        individualFramerLayout = findViewById(R.id.individualFramerLayout);
        groupFramerLayout = findViewById(R.id.groupFramerLayout);
        institutionFramerLayout = findViewById(R.id.institutionFramerLayout);

        radioGroup = findViewById(R.id.radioGroup);
        rdo_individual = findViewById(R.id.rdo_individual);
        rdo_group = findViewById(R.id.rdo_group);
        rdo_institution = findViewById(R.id.rdo_institution);

        country_code = findViewById(R.id.txt_countrycode);

        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            signupLayout.setBackgroundDrawable(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.bg));
        } else {
            signupLayout.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.bg));
        }

        back_arrow_button.setOnClickListener(v -> finish());

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (rdo_individual.isChecked()) {
                individualFramerLayout.setVisibility(View.VISIBLE);
                groupFramerLayout.setVisibility(View.GONE);
                //institutionFramerLayout.setVisibility(View.GONE);
                strIndividualGroupInstituteType = "1";
            } else if (rdo_group.isChecked()) {
                individualFramerLayout.setVisibility(View.GONE);
                groupFramerLayout.setVisibility(View.VISIBLE);
                //institutionFramerLayout.setVisibility(View.GONE);
                strIndividualGroupInstituteType = "2";
            }

        });



        password_show.setImageResource(R.drawable.visibility_off);
        confirm_password_show.setImageResource(R.drawable.visibility_off);
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

        codenumber();
        verify_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCodes(v);
            }
        });

        confirmPasswordLayout.setOnClickListener(v -> {
            if (!isClickConfirm) {
                isClickConfirm = true;
                confirm_password_show.setImageResource(R.drawable.visibility_off);
                confirmPasswordEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                confirmPasswordEdt.setSelection(confirmPasswordEdt.getText().length());

            } else {
                isClickConfirm = false;
                confirm_password_show.setImageResource(R.drawable.visibility_on);
                confirmPasswordEdt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                confirmPasswordEdt.setSelection(confirmPasswordEdt.getText().length());
            }
        });

        loginTxt.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, Login1Activity.class);
            startActivity(intent);
            finish();
        });

        signUpTxtBtn.setOnClickListener(v -> {
            strNameOfFarmer = nameOfFarmerEdt.getText().toString();
            strEmail = emailIndividualEdt.getText().toString();
            strPhoneIndividual = phoneIndividualEdt.getText().toString();
            strDistrictIndividual = districtIndividualEdt.getText().toString();
            strChiefdomIndividual = chiefdomIndividualEdt.getText().toString();
            strSectionIndividual = sectionIndividualEdt.getText().toString();
            strTownIndividual = townIndividualEdt.getText().toString();
            strPassword = passwordEdt.getText().toString();
            strConfirmPassword = confirmPasswordEdt.getText().toString();

            strNameGroup = nameGroupEdt.getText().toString();
            strLeaderGroup = leaderGroupEdt.getText().toString();
            strPhoneGroup = phoneGroupEdt.getText().toString();
            strEmailGroup = emailGroupEdt.getText().toString();
            strDistrictGroup = districtGroupEdt.getText().toString();
            strChiefdomGroup = chiefdomGroupEdt.getText().toString();
            strSectionGroup = sectionGroupEdt.getText().toString();
            strTownGroup = townGroupEdt.getText().toString();




            if (strIndividualGroupInstituteType.equals("1")) {
                Individual_Validation();
            } else if (strIndividualGroupInstituteType.equals("2")) {
                Grouop_Validation();
            }


        });
    }

    private void init() {

        FirebaseApp.getApps(context);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SignUpActivity.this, new OnSuccessListener<InstanceIdResult>() {
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

    public void Individual_Validation() {
        if (strNameOfFarmer.equals("")) {
            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_name_of_farmer), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        else if (strPhoneIndividual.equals("")) {
            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_phoneNumber), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        else if (strPhoneIndividual.length() < 10) {
            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.phone_no_minimum_length_should), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        /*else if (strPhoneIndividual.length() > 13) {
            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.phone_no_minimum_length), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else if (strEmail.equals("")) {
            Toast toast = Toast.makeText(SignUpActivity.this, "Please enter email address", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();*/
        }
        else if (!strEmail.equals("")) {
            if (!strEmail.matches(emailPattern)) {
                Toast toast = Toast.makeText(SignUpActivity.this, "Invalid email address", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            } else if (strDistrictIndividual.equals("")) {
                Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_district), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (strChiefdomIndividual.equals("")) {
                Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_chiefdom), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (strSectionIndividual.equals("")) {
                Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_section), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (strTownIndividual.equals("")) {
                Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_town), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (strPassword.equals("")) {
                Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_password), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (strConfirmPassword.equals("")) {
                Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_confirm_password), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (!strPassword.equals(strConfirmPassword)) {
                Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_same_password), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                newPhoneNumber = strPhoneIndividual;
                ForgotPasswordCheck_Api();
            }
        } else if (strDistrictIndividual.equals("")) {
            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_district), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (strChiefdomIndividual.equals("")) {
            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_chiefdom), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (strSectionIndividual.equals("")) {
            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_section), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (strTownIndividual.equals("")) {
            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_town), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (strPassword.equals("")) {
            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_password), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (strConfirmPassword.equals("")) {
            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_confirm_password), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (!strPassword.equals(strConfirmPassword)) {
            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_same_password), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            newPhoneNumber = strPhoneIndividual;
            ForgotPasswordCheck_Api();
        }

        Log.e("checkVelue", "rg: " + newPhoneNumber);
    }

    public void Grouop_Validation() {
        if (strNameGroup.equals("")) {
            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_name_of_group), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (strLeaderGroup.equals("")) {
            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_name_of_leader), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (strPhoneGroup.equals("")) {
            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_phoneNumber), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (strPhoneGroup.length() < 10) {
            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.phone_no_minimum_length_should), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
//        } else if (!(validatePhone(phoneGroupEdt.getText().toString()))) {
//            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.phone_no_minimum_length), Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();

        /*else if (strPhoneGroup.length() < 6) {
            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.phone_no_minimum_length), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (strPhoneGroup.length() > 14) {
            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.phone_no_maximum_length), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
//        }else if (strEmailGroup.equals("")) {
//            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.plz_enter_email), Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
        }*//*
        }*/
//        else if (strPhoneGroup.length() < 10) {
//            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.phone_no_minimum_length_should), Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
//        }
        /*else if (strPhoneGroup.length() >13) {
            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.phone_no_minimum_length), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }*/
        } else if (!strEmailGroup.equals("")) {
            if (!strEmailGroup.matches(emailPattern)) {
                Toast toast = Toast.makeText(SignUpActivity.this, "Invalid email address", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            } else if (strDistrictGroup.equals("")) {
                Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_district), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (strChiefdomGroup.equals("")) {
                Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_chiefdom), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (strSectionGroup.equals("")) {
                Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_section), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (strTownGroup.equals("")) {
                Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_town), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (strPassword.equals("")) {
                Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_password), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (strConfirmPassword.equals("")) {
                Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_confirm_password), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (!strPassword.equals(strConfirmPassword)) {
                Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_same_password), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                newPhoneNumber = strPhoneGroup;
                ForgotPasswordCheck_Api();
            }
        } else if (strDistrictGroup.equals("")) {
            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_district), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (strChiefdomGroup.equals("")) {
            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_chiefdom), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (strSectionGroup.equals("")) {
            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_section), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (strTownGroup.equals("")) {
            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_town), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (strPassword.equals("")) {
            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_password), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (strConfirmPassword.equals("")) {
            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_confirm_password), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (!strPassword.equals(strConfirmPassword)) {
            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_same_password), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        /*else {

            String countryCode = strPhoneGroup.substring(0, 3);
            String countryCode1 = strPhoneGroup.substring(0, 4);
            String zero = strPhoneGroup.substring(0, 1);

            if (countryCode.equals("+91")) {
                newPhoneNumber = strPhoneGroup;
            } else if (countryCode1.equals("+276")) {
                newPhoneNumber = strPhoneGroup;
            }*/
        /*else if (zero.equals("0")) {
                //  String tempString = strEmail.substring(1, strEmail.length());
                newPhoneNumber = strPhoneGroup.substring(1, strPhoneGroup.length());
            }*/
        else {
            newPhoneNumber = strPhoneGroup;
            ForgotPasswordCheck_Api();
        }

    }

    private void ForgotPasswordCheck_Api() {

        String checkPhoneNumber = "+91" + newPhoneNumber;
        // checkPhoneNumber = "+91"+newPhoneNumber;
        Log.e("checkPhoneNumber", checkPhoneNumber);

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this, R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        progressDialog.show();

        ApiServices service = ApiClient.getClient().create(ApiServices.class);

        retrofit2.Call<CommonModel> call = service.getForgotCheckPassword(device_tokens, strIndividualGroupInstituteType, checkPhoneNumber);
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

                        if (success.equalsIgnoreCase("true")) {
//                            Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_LONG).show();
                            Toast.makeText(SignUpActivity.this, "Number is already exist", Toast.LENGTH_LONG).show();

                        } else {
                            next_button(viewFlipper);
                        }
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(SignUpActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(SignUpActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(SignUpActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(SignUpActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(SignUpActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(SignUpActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(SignUpActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(SignUpActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(SignUpActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void next_button(View view) {
        // Toast.makeText(SignUpActivity.this, "calling this validation2", Toast.LENGTH_SHORT).show();
        String fbUserPhoneNumber = "";

        if (newPhoneNumber.length() == 8) {
            //  Toast.makeText(this, "Enter the Countrycode", Toast.LENGTH_SHORT).show();

            // fbUserPhoneNumber = "+276" + newPhoneNumber;
            sendVerificationCode(fbUserPhoneNumber);
        } else if (newPhoneNumber.length() == 10) {
            fbUserPhoneNumber = newPhoneNumber;
            fbUserPhoneNumber = "+91" + newPhoneNumber;
            sendVerificationCode(fbUserPhoneNumber);
            // Toast.makeText(this, "Enter the Countrycode", Toast.LENGTH_SHORT).show();

        } else if (newPhoneNumber.length() == 9) {
            fbUserPhoneNumber = newPhoneNumber;
            sendVerificationCode(fbUserPhoneNumber);
        } else if (newPhoneNumber.length() == 11) {
            fbUserPhoneNumber = newPhoneNumber;
            sendVerificationCode(fbUserPhoneNumber);
        } else if (newPhoneNumber.length() == 12) {
            fbUserPhoneNumber = newPhoneNumber;
            sendVerificationCode(fbUserPhoneNumber);
        } else if (newPhoneNumber.length() == 13) {
            fbUserPhoneNumber = newPhoneNumber;
            sendVerificationCode(fbUserPhoneNumber);
        } else {
            Toast toast = Toast.makeText(SignUpActivity.this, "Please enter valid mobile number", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        Log.e("checkVelue", "firebase: " + fbUserPhoneNumber);

        Log.e("bhfu", fbUserPhoneNumber);
    }


    private void sendVerificationCode(String number) {
        //   progressDialog.show();
        // SignupIndividualApi();
        // SignupGroupApi();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        private PhoneAuthCredential phoneAuthCredential;

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            viewFlipper.setDisplayedChild(1);
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();
            signInWithPhoneAuthCredential(phoneAuthCredential);
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
                // verifyCode(code);
            }
        }


        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {

            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();


        }
    };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            //  Toast.makeText(getApplicationContext(),"OTP verification successfull",Toast.LENGTH_SHORT).show();
                            if (strIndividualGroupInstituteType.equals("1")) {
                                SignupIndividualApi();
                            } else if (strIndividualGroupInstituteType.equals("2")) {
                                SignupGroupApi();
                            }
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
        // Toast.makeText(SignUpActivity.this, code, Toast.LENGTH_SHORT).show();
        if (!code.equals("")) {

            Log.e("bhu", "running phase 1");
            PhoneAuthCredential credential =
                    PhoneAuthProvider.getCredential(verificationId, code);

            signInWithPhoneAuthCredential(credential);
        } else {
            Toast.makeText(this, "Enter the Correct verification Code", Toast.LENGTH_SHORT).show();
        }

    }

//    public void Institute_Validation() {
//        if (strNameInstitution.equals("")) {
//            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_name_of_institution), Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
//        } else if (strSectorInstitution.equals("")) {
//            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_sector), Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
//        } else if (strDepartmentInstitution.equals("")) {
//            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_department), Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
//        } else if (strPhoneInstitution.equals("")) {
//            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_phoneNumber), Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
//        } else if (strLocationInstitution.equals("")) {
//            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_location), Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
//        } else if (strPassword.equals("")) {
//            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_password), Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
//        } else if (strConfirmPassword.equals("")) {
//            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_confirm_password), Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
//        } else if (!strPassword.equals(strConfirmPassword)) {
//            Toast toast = Toast.makeText(SignUpActivity.this, getString(R.string.pls_enter_same_password), Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
//        } else {
//            //ChangePassword_Api();
//        }
//    }

    private void SignupGroupApi() {

        String strPhoneIndividual = "+91" + newPhoneNumber;
        String withoutConryCodeNumber = "";
        if (strPhoneIndividual.substring(0, 3).equals("+91")) {
            withoutConryCodeNumber = strPhoneIndividual;
        }

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this, R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        progressDialog.show();

        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        retrofit2.Call<SignUpGroupFarmerModel> call = service.getSignupGroup(strNameGroup, strLeaderGroup,
                withoutConryCodeNumber, strEmailGroup, strDistrictGroup, strChiefdomGroup, strSectionGroup,
                strTownGroup, device_tokens, "1234", strPassword);


        Log.e("strNameGroup",
                strNameGroup + " / " + strLeaderGroup + " / " + withoutConryCodeNumber + " / " + strEmailGroup + " / " + strDistrictGroup + " / " + strChiefdomGroup + " / " +
                        strSectionGroup + " / " + strTownGroup + " / " + device_tokens + " / " + device_tokens + " / " + strPassword);


        call.enqueue(new Callback<SignUpGroupFarmerModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<SignUpGroupFarmerModel> call, @NonNull retrofit2.Response<SignUpGroupFarmerModel> response) {
                progressDialog.dismiss();
                sharedPrefClass.putString(SharedPrefClass.NAME, strLeaderGroup);
                sharedPrefClass.putString(SharedPrefClass.PHONENO, strPhoneGroup);
                sharedPrefClass.putString(SharedPrefClass.ADDRESS, strTownGroup);
                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success = response.body().getSuccess();

                        if (success.equalsIgnoreCase("true")) {
                            Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
                            // sendVerificationCode(newPhoneNumber);

                            Intent intent = new Intent(getApplicationContext(), Login1Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                            startActivity(intent);
                        }

                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(SignUpActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(SignUpActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(SignUpActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(SignUpActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(SignUpActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(SignUpActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(SignUpActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(SignUpActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<SignUpGroupFarmerModel> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(SignUpActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(SignUpActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SignupIndividualApi() {

        // String strphoneIndividual = newPhoneNumber.substring(3, newPhoneNumber.length());

        String strPhoneIndividual = "+91" + newPhoneNumber;
        String withoutConryCodeNumber = strPhoneIndividual;
        if (strPhoneIndividual.substring(0, 3).equals("+91")) {
            withoutConryCodeNumber = strPhoneIndividual;
        }
        /*else if(strPhoneIndividual.substring(0,4).equals("+276")){
            withoutConryCodeNumber = strPhoneIndividual;
        }*//*else if(strPhoneIndividual.substring(0,1).equals("0")){
            withoutConryCodeNumber = strPhoneIndividual.substring(1,strPhoneIndividual.length());
        }*/
        Log.e("Number final ", "=== " + strPhoneIndividual);

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this, R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        progressDialog.show();

        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        retrofit2.Call<SignUpGroupFarmerModel> call = service.getSignupIndividual(strNameOfFarmer,
                withoutConryCodeNumber, strEmail, strDistrictIndividual, strChiefdomIndividual,
                strSectionIndividual, strTownIndividual, device_tokens, device_tokens, strPassword);

        Log.e("strNameOfFarmer", strNameOfFarmer + " / " + newPhoneNumber + " / " + strEmail + " / " + strDistrictIndividual + " / " + strChiefdomIndividual
                + " / " + strSectionIndividual + " / " +
                strTownIndividual + " / " + device_tokens + " / " + device_tokens + " / " + strPassword);

        call.enqueue(new Callback<SignUpGroupFarmerModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<SignUpGroupFarmerModel> call, @NonNull retrofit2.Response<SignUpGroupFarmerModel> response) {
                progressDialog.dismiss();
                sharedPrefClass.putString(SharedPrefClass.NAME, strNameOfFarmer);
                sharedPrefClass.putString(SharedPrefClass.PHONENO, strPhoneIndividual);
                sharedPrefClass.putString(SharedPrefClass.ADDRESS, strTownIndividual);
                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success = response.body().getSuccess();

                        if (success.equalsIgnoreCase("true")) {
                            Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
                            //sendVerificationCode(newPhoneNumber);

                            Intent intent = new Intent(getApplicationContext(), Login1Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(SignUpActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(SignUpActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(SignUpActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(SignUpActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(SignUpActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(SignUpActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(SignUpActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(SignUpActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<SignUpGroupFarmerModel> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(SignUpActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(SignUpActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean validatePhone(String strPhone) {
        Matcher matcher;
        String noSpaceRegex = "^\\d{8,13}$";
        Pattern pattern = Pattern.compile(noSpaceRegex);
        matcher = pattern.matcher(strPhone);
        return matcher.matches();
    }

}