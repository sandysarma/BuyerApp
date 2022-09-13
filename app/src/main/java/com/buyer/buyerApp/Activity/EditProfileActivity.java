package com.buyer.buyerApp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buyer.buyerApp.Model.ProfileDetailsModel;
import com.buyer.buyerApp.Model.UpdateProfileModel;
import com.buyer.buyerApp.ModelResult.ProfileDetailsResponse;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.ApiClient;
import com.buyer.buyerApp.RetrofitApi.ApiServices;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

public class EditProfileActivity extends AppCompatActivity {
    ImageView back_arrow;
    TextView editProfileBtn;
    LinearLayout  individualFramerLayout, groupFramerLayout;
    EditText nameEdt, phoneContactUsEdt, edtCity, edtChief, sectoinEdt, edtVillage, emailOption;


    private String strNameOfFarmer, strDistrictIndividual, strChiefdomIndividual, strEmail, strPhoneIndividual, strSectionIndividual, strTownIndividual;

    EditText nameGroupEdt, leaderGroupEdt, phoneGroupEdt, emailGroupEdt, districtGroupEdt, chiefdomGroupEdt,
            sectionGroupEdt, townGroupEdt;

    String strNameGroup = "", strLeaderGroup = "", strPhoneGroup = "", strEmailGroup = "", strDistrictGroup = "",
            strChiefdomGroup = "", strSectionGroup = "", strTownGroup = "";

    SharedPreferences sharedPreferences;
    String userId = "", userType = "";
    ProfileDetailsResponse profileDetailsResponse;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        back_arrow = findViewById(R.id.back_arrow);
        editProfileBtn = findViewById(R.id.editProfileBtn);

        sharedPreferences = EditProfileActivity.this.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");
        userType = sharedPreferences.getString("userType", "");

        nameEdt = findViewById(R.id.nameEdt);
        phoneContactUsEdt = findViewById(R.id.phoneContactUsEdt);
        edtCity = findViewById(R.id.edtCity);
        edtChief = findViewById(R.id.edtChief);
        sectoinEdt = findViewById(R.id.sectoinEdt);
        edtVillage = findViewById(R.id.edtVillage);
        emailOption = findViewById(R.id.emailOption);

        nameGroupEdt = findViewById(R.id.nameGroupEdt);
        leaderGroupEdt = findViewById(R.id.leaderGroupEdt);
        phoneGroupEdt = findViewById(R.id.phoneGroupEdt);
        emailGroupEdt = findViewById(R.id.emailGroupEdt);
        districtGroupEdt = findViewById(R.id.districtGroupEdt);
        chiefdomGroupEdt = findViewById(R.id.chiefdomGroupEdt);
        sectionGroupEdt = findViewById(R.id.sectionGroupEdt);
        townGroupEdt = findViewById(R.id.townGroupEdt);

        groupFramerLayout = findViewById(R.id.groupFramerLayout);
        individualFramerLayout = findViewById(R.id.individualFramerLayout);

        back_arrow.setOnClickListener(v -> finish());

        if (userType.equals("2")) {
            groupFramerLayout.setVisibility(View.VISIBLE);
            individualFramerLayout.setVisibility(View.GONE);
            ProfileDetailsGroup_Api();
        } else {
            groupFramerLayout.setVisibility(View.GONE);
            individualFramerLayout.setVisibility(View.VISIBLE);
            ProfileDetailsIndividual_Api();
        }


        editProfileBtn.setOnClickListener(v -> {
            strNameOfFarmer = nameEdt.getText().toString();
            strEmail = emailOption.getText().toString();
            strPhoneIndividual = phoneContactUsEdt.getText().toString();
            strDistrictIndividual = edtCity.getText().toString();
            strChiefdomIndividual = edtChief.getText().toString();
            strSectionIndividual = sectoinEdt.getText().toString();
            strTownIndividual = edtVillage.getText().toString();

            strNameGroup = nameGroupEdt.getText().toString();
            strLeaderGroup = leaderGroupEdt.getText().toString();
            strPhoneGroup = phoneGroupEdt.getText().toString();
            strEmailGroup = emailGroupEdt.getText().toString();
            strDistrictGroup = districtGroupEdt.getText().toString();
            strChiefdomGroup = chiefdomGroupEdt.getText().toString();
            strSectionGroup = sectionGroupEdt.getText().toString();
            strTownGroup = townGroupEdt.getText().toString();

            if (userType.equals("2")) {
                Grouop_Validation();
            } else {
                Individual_Validation();
            }
        });
    }

    public void Individual_Validation() {
        if (strNameOfFarmer.equals("")) {
            Toast toast = Toast.makeText(EditProfileActivity.this, getString(R.string.pls_enter_name_of_farmer), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        } else if (strPhoneIndividual.equals("")) {
            Toast toast = Toast.makeText(EditProfileActivity.this, getString(R.string.pls_enter_phoneNumber), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        } else if (strPhoneIndividual.length()<8) {
            Toast toast = Toast.makeText(EditProfileActivity.this, getString(R.string.Phone_no_minimum_length), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

//        }else if (strPhoneIndividual.length()>13) {
//            Toast toast = Toast.makeText(EditProfileActivity.this, getString(R.string.phone_no_maximum_length), Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();

//        }else if (strEmail.equals("")) {
//            Toast toast = Toast.makeText(EditProfileActivity.this, "Please enter email address", Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
        }else if (!strEmail.equals("")){
            if (!strEmail.matches(emailPattern)) {
                Toast toast = Toast.makeText(EditProfileActivity.this, "Invalid email address", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (strDistrictIndividual.equals("")) {
                Toast toast = Toast.makeText(EditProfileActivity.this, getString(R.string.pls_enter_district), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (strChiefdomIndividual.equals("")) {
                Toast toast = Toast.makeText(EditProfileActivity.this, getString(R.string.pls_enter_chiefdom), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (strSectionIndividual.equals("")) {
                Toast toast = Toast.makeText(EditProfileActivity.this, getString(R.string.pls_enter_section), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (strTownIndividual.equals("")) {
                Toast toast = Toast.makeText(EditProfileActivity.this, getString(R.string.pls_enter_town), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {

                UpdateProfileIndividual_Api();
            }

        } else if (strDistrictIndividual.equals("")) {
            Toast toast = Toast.makeText(EditProfileActivity.this, getString(R.string.pls_enter_district), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (strChiefdomIndividual.equals("")) {
            Toast toast = Toast.makeText(EditProfileActivity.this, getString(R.string.pls_enter_chiefdom), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (strSectionIndividual.equals("")) {
            Toast toast = Toast.makeText(EditProfileActivity.this, getString(R.string.pls_enter_section), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (strTownIndividual.equals("")) {
            Toast toast = Toast.makeText(EditProfileActivity.this, getString(R.string.pls_enter_town), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {



            UpdateProfileIndividual_Api();
        }
    }

    public void Grouop_Validation()
    {
        if (strNameGroup.equals("")) {
            Toast toast = Toast.makeText(EditProfileActivity.this, getString(R.string.pls_enter_name_of_group), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (strLeaderGroup.equals("")) {
            Toast toast = Toast.makeText(EditProfileActivity.this, getString(R.string.pls_enter_name_of_leader), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (strPhoneGroup.equals("")) {
            Toast toast = Toast.makeText(EditProfileActivity.this, getString(R.string.pls_enter_phoneNumber), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (strPhoneGroup.length() < 8) {
            Toast toast = Toast.makeText(EditProfileActivity.this, getString(R.string.phone_no_minimum_length_should), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            //        }else if (strEmail.equals("")) {
//            Toast toast = Toast.makeText(EditProfileActivity.this, "Please enter email address", Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
        } else if (!strEmailGroup.equals("")) {
            if (!strEmailGroup.matches(emailPattern)) {
                Toast toast = Toast.makeText(EditProfileActivity.this, "Invalid email address", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (strDistrictGroup.equals("")) {
                Toast toast = Toast.makeText(EditProfileActivity.this, getString(R.string.pls_enter_district), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (strChiefdomGroup.equals("")) {
                Toast toast = Toast.makeText(EditProfileActivity.this, getString(R.string.pls_enter_chiefdom), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (strSectionGroup.equals("")) {
                Toast toast = Toast.makeText(EditProfileActivity.this, getString(R.string.pls_enter_section), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (strTownGroup.equals("")) {
                Toast toast = Toast.makeText(EditProfileActivity.this, getString(R.string.pls_enter_town), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {

                UpdateProfileGroup_Api();
            }

        } else if (strDistrictGroup.equals("")) {
            Toast toast = Toast.makeText(EditProfileActivity.this, getString(R.string.pls_enter_district), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (strChiefdomGroup.equals("")) {
            Toast toast = Toast.makeText(EditProfileActivity.this, getString(R.string.pls_enter_chiefdom), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (strSectionGroup.equals("")) {
            Toast toast = Toast.makeText(EditProfileActivity.this, getString(R.string.pls_enter_section), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (strTownGroup.equals("")) {
            Toast toast = Toast.makeText(EditProfileActivity.this, getString(R.string.pls_enter_town), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {


            String countryCode = strPhoneGroup.substring(0,3);
            String countryCode1 = strPhoneGroup.substring(0,4);
            String zero = strPhoneGroup.substring(0,1);


//            if(strPhoneGroup.length() == 8)
//            {
//                newPhoneNumber = "+276" + strPhoneGroup;
//            }
//
//            if(countryCode.equals("+91"))
//            {
//                newPhoneNumber = strPhoneGroup;
//            }
//            if(countryCode1.equals("+276"))
//            {
//                newPhoneNumber = strPhoneGroup;
//            }
//            if(zero.equals("0"))
//            {
//                String tempString = strPhoneGroup.substring(1, strPhoneGroup.length());
//                newPhoneNumber = "+91"+tempString;
//
//            }
//
//            if (strPhoneGroup.length() == 10 )
//            {
//                newPhoneNumber = "+91"+strPhoneGroup;
//
//            }


            UpdateProfileGroup_Api();
        }
    }

    private void ProfileDetailsGroup_Api() {
        final ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this, R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        progressDialog.show();

        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        retrofit2.Call<ProfileDetailsModel> call = service.getProfileDetails("123", userType, userId);
        call.enqueue(new Callback<ProfileDetailsModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ProfileDetailsModel> call, @NonNull retrofit2.Response<ProfileDetailsModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        ProfileDetailsModel result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();

                        if (success.equalsIgnoreCase("true")) {
                            profileDetailsResponse = result.getProfileDetailsResponse();

                            nameGroupEdt.setText(profileDetailsResponse.getNameOfGroup());
                            leaderGroupEdt.setText(profileDetailsResponse.getNameOfLeader());
                            phoneGroupEdt.setText(profileDetailsResponse.getPhoneNumber());
                            emailGroupEdt.setText(profileDetailsResponse.getEmail());
                            districtGroupEdt.setText(profileDetailsResponse.getDistrict());
                            chiefdomGroupEdt.setText(profileDetailsResponse.getChiefdom());
                            sectionGroupEdt.setText(profileDetailsResponse.getSection());
                            townGroupEdt.setText(profileDetailsResponse.getTown());
                        } else {
                            Toast.makeText(EditProfileActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(EditProfileActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(EditProfileActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(EditProfileActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(EditProfileActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(EditProfileActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(EditProfileActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(EditProfileActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(EditProfileActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<ProfileDetailsModel> call, Throwable t) {
                progressDialog.dismiss();

                if (t instanceof IOException) {
                    Toast.makeText(EditProfileActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(EditProfileActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ProfileDetailsIndividual_Api() {
        final ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this, R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        progressDialog.show();

        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        retrofit2.Call<ProfileDetailsModel> call = service.getProfileDetails("123", userType, userId);
        call.enqueue(new Callback<ProfileDetailsModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ProfileDetailsModel> call, @NonNull retrofit2.Response<ProfileDetailsModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        ProfileDetailsModel result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();

                        if (success.equalsIgnoreCase("true")) {
                            profileDetailsResponse = result.getProfileDetailsResponse();
                            nameEdt.setText(profileDetailsResponse.getNameOfGroup());
                            phoneContactUsEdt.setText(profileDetailsResponse.getPhoneNumber());
                            emailOption.setText(profileDetailsResponse.getEmail());
                            edtCity.setText(profileDetailsResponse.getDistrict());
                            edtChief.setText(profileDetailsResponse.getChiefdom());
                            sectoinEdt.setText(profileDetailsResponse.getSection());
                            edtVillage.setText(profileDetailsResponse.getTown());
                        } else {
                            Toast.makeText(EditProfileActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(EditProfileActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(EditProfileActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(EditProfileActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(EditProfileActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(EditProfileActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(EditProfileActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(EditProfileActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(EditProfileActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<ProfileDetailsModel> call, Throwable t) {
                progressDialog.dismiss();

                if (t instanceof IOException) {
                    Toast.makeText(EditProfileActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(EditProfileActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void UpdateProfileGroup_Api() {

        String withoutConryCodeNumberGroup = "";
        withoutConryCodeNumberGroup = profileDetailsResponse.getCountryCode()+strPhoneGroup;

        Log.e("withoutConryCodeNumberGroup",withoutConryCodeNumberGroup);

        final ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this, R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        progressDialog.show();

        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        retrofit2.Call<UpdateProfileModel> call = service.getEditProfileGroup("123",
                userId, strNameGroup, strLeaderGroup, withoutConryCodeNumberGroup, strEmailGroup,
                strDistrictGroup, strChiefdomGroup, strSectionGroup, strTownGroup);
        call.enqueue(new Callback<UpdateProfileModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<UpdateProfileModel> call, @NonNull retrofit2.Response<UpdateProfileModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success = response.body().getSuccess();

                        if (success.equalsIgnoreCase("true")) {
                            Toast.makeText(EditProfileActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(EditProfileActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(EditProfileActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(EditProfileActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(EditProfileActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(EditProfileActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(EditProfileActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(EditProfileActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(EditProfileActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<UpdateProfileModel> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(EditProfileActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(EditProfileActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void UpdateProfileIndividual_Api() {

        String withoutConryCodeNumberIndividual = "";
        withoutConryCodeNumberIndividual = profileDetailsResponse.getCountryCode()+strPhoneIndividual;

        Log.e("withoutConryCodeNumberIndividual",withoutConryCodeNumberIndividual);

        final ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this, R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        progressDialog.show();

        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        retrofit2.Call<UpdateProfileModel> call = service.getEditProfileIndividual("123",
                userId, strNameOfFarmer, withoutConryCodeNumberIndividual, strEmail,
                strDistrictIndividual, strChiefdomIndividual, strSectionIndividual, strTownIndividual);
        call.enqueue(new Callback<UpdateProfileModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<UpdateProfileModel> call, @NonNull retrofit2.Response<UpdateProfileModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success = response.body().getSuccess();

                        if (success.equalsIgnoreCase("true")) {
                            Toast.makeText(EditProfileActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(EditProfileActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(EditProfileActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(EditProfileActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(EditProfileActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(EditProfileActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(EditProfileActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(EditProfileActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(EditProfileActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<UpdateProfileModel> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(EditProfileActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(EditProfileActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}