package com.buyer.buyerApp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.buyer.buyerApp.Model.ProfileDetailsModel;
import com.buyer.buyerApp.ModelResult.ProfileDetailsResponse;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.ApiClient;
import com.buyer.buyerApp.RetrofitApi.ApiServices;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

public class ProfileActivity extends AppCompatActivity {

    ScrollView profileLayout;
    LinearLayout back_arrow_button, individualFramerLayout, groupFramerLayout, institutionFramerLayout;
    TextView nameOfFarmerTxt, phoneIndividualTxt, emailIndividualtxt, districtIndividualTxt,
            chiefdomIndividualTxt, sectionIndividualTxt, townIndividualTxt;
    TextView nameGroupTxt, leaderGroupTxt, phoneGroupTxt, emailGroupTxt, districtGroupTxt, chiefdomGroupTxt,
            sectionGroupTxt, townGroupTxt;
    TextView nameInstitutionTxt, sectorInstitutionTxt, departmentInstitutionTxt, phoneInstitutionTxt,
            emailInstitutionTxt, locationInstitutionTxt, editProfileTxtBtn;

    SharedPreferences sharedPreferences;
    String userId = "", userType = "";
    ProfileDetailsResponse profileDetailsResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        sharedPreferences = ProfileActivity.this.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");
        userType = sharedPreferences.getString("userType", "");

        back_arrow_button = findViewById(R.id.back_arrow_button);
        editProfileTxtBtn = findViewById(R.id.editProfileTxtBtn);
        individualFramerLayout = findViewById(R.id.individualFramerLayout);
        groupFramerLayout = findViewById(R.id.groupFramerLayout);
        institutionFramerLayout = findViewById(R.id.institutionFramerLayout);
        profileLayout = findViewById(R.id.profileLayout);

        nameOfFarmerTxt = findViewById(R.id.nameOfFarmerTxt);
        phoneIndividualTxt = findViewById(R.id.phoneIndividualTxt);
        emailIndividualtxt = findViewById(R.id.emailIndividualtxt);
        districtIndividualTxt = findViewById(R.id.districtIndividualTxt);
        chiefdomIndividualTxt = findViewById(R.id.chiefdomIndividualTxt);
        sectionIndividualTxt = findViewById(R.id.sectionIndividualTxt);
        townIndividualTxt = findViewById(R.id.townIndividualTxt);

        nameGroupTxt = findViewById(R.id.nameGroupTxt);
        leaderGroupTxt = findViewById(R.id.leaderGroupTxt);
        phoneGroupTxt = findViewById(R.id.phoneGroupTxt);
        emailGroupTxt = findViewById(R.id.emailGroupTxt);
        districtGroupTxt = findViewById(R.id.districtGroupTxt);
        chiefdomGroupTxt = findViewById(R.id.chiefdomGroupTxt);
        sectionGroupTxt = findViewById(R.id.sectionGroupTxt);
        townGroupTxt = findViewById(R.id.townGroupTxt);



        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            profileLayout.setBackgroundDrawable(ContextCompat.getDrawable(ProfileActivity.this, R.drawable.bg));
        } else {
            profileLayout.setBackground(ContextCompat.getDrawable(ProfileActivity.this, R.drawable.bg));
        }

        if (userType.equals("2")) {
            groupFramerLayout.setVisibility(View.VISIBLE);
            individualFramerLayout.setVisibility(View.GONE);
            ProfileDetailsGroup_Api();
        }
        else {
            groupFramerLayout.setVisibility(View.GONE);
            individualFramerLayout.setVisibility(View.VISIBLE);
            ProfileDetailsIndividual_Api();
        }

        back_arrow_button.setOnClickListener(v -> finish());

        editProfileTxtBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });
    }


    private void ProfileDetailsGroup_Api() {
        final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this, R.style.full_screen_dialog);
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
                            nameGroupTxt.setText(profileDetailsResponse.getNameOfGroup());
                            leaderGroupTxt.setText(profileDetailsResponse.getNameOfLeader());
                            phoneGroupTxt.setText(profileDetailsResponse.getPhoneNumber());
                            emailGroupTxt.setText(profileDetailsResponse.getEmail());
                            districtGroupTxt.setText(profileDetailsResponse.getDistrict());
                            chiefdomGroupTxt.setText(profileDetailsResponse.getChiefdom());
                            sectionGroupTxt.setText(profileDetailsResponse.getSection());
                            townGroupTxt.setText(profileDetailsResponse.getTown());
                        } else {
                            Toast.makeText(ProfileActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(ProfileActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(ProfileActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(ProfileActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(ProfileActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(ProfileActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(ProfileActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(ProfileActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(ProfileActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ProfileActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(ProfileActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ProfileDetailsIndividual_Api() {
        final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this, R.style.full_screen_dialog);
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
                            nameOfFarmerTxt.setText(profileDetailsResponse.getNameOfGroup());
                            phoneIndividualTxt.setText(profileDetailsResponse.getPhoneNumber());
                            emailIndividualtxt.setText(profileDetailsResponse.getEmail());
                            districtIndividualTxt.setText(profileDetailsResponse.getDistrict());
                            chiefdomIndividualTxt.setText(profileDetailsResponse.getChiefdom());
                            sectionIndividualTxt.setText(profileDetailsResponse.getSection());
                            townIndividualTxt.setText(profileDetailsResponse.getTown());
                        } else {
                            Toast.makeText(ProfileActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(ProfileActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(ProfileActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(ProfileActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(ProfileActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(ProfileActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(ProfileActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(ProfileActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(ProfileActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ProfileActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(ProfileActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (userType.equals("2")) {
            ProfileDetailsGroup_Api();
        } else {
            ProfileDetailsIndividual_Api();
        }
    }
}