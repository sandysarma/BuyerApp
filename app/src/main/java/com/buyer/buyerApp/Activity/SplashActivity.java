package com.buyer.buyerApp.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;

import com.buyer.buyerApp.Class.GPSTracker;
import com.buyer.buyerApp.MainActivity;
import com.buyer.buyerApp.R;
import com.google.android.material.snackbar.Snackbar;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class SplashActivity extends AppCompatActivity implements DialogInterface.OnClickListener {
    RelativeLayout rlBaseLayout;
    FragmentActivity context;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private Handler handler;
    String strUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = SplashActivity.this;

        rlBaseLayout = findViewById(R.id.rlBaseLayout);
        ImageView image = findViewById(R.id.image);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Glide.with(this).load(R.drawable.splash).into(image);

        try {
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo info = getPackageManager().getPackageInfo("com.farmer.smartfarms91", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                internetStatusCheck();
            }
        }, 3000);
    }

    public void internetStatusCheck() {
        ConnectivityManager connectivityManager = (ConnectivityManager) SplashActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            if (connectivityManager.getActiveNetworkInfo().isConnected()) {
                askPermissions();
            } else {
                showNetworkSnackBar();
            }
        } else {
            showNetworkSnackBar();
        }
    }

    private void showNetworkSnackBar() {
        Snackbar snackbar = Snackbar.make(rlBaseLayout, getResources().getString(R.string.please_connect_internet_connection), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getResources().getString(R.string.retry), view -> internetStatusCheck());
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    private final ArrayList<String> requirePermissions = new ArrayList<>();
    private final ArrayList<String> permissionsToRequest = new ArrayList<>();
    private final ArrayList<String> permissionsRejected = new ArrayList<>();

    private void askPermissions() {
        requirePermissions.add(Manifest.permission.CAMERA);
        requirePermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        requirePermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        requirePermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        for (String permission : requirePermissions) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(permission);
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                //ActivityCompat.requestPermissions(context, permissionsToRequest.toArray(new String[permissionsToRequest.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                ActivityCompat.requestPermissions(context, permissionsToRequest.toArray(new String[permissionsToRequest.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            } else {
                launchLocation();
            }
        } else {
            launchLocation();
        }
    }

    private void launchLocation() {
        GPSTracker gps = new GPSTracker(context);
        if (!gps.canGetLocation()) {
            gps.showSettingsAlert();
        } else {
            //launchApp();

            SharedPreferences sharedPreferences = SplashActivity.this.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
            strUserId = sharedPreferences.getString("userId", "");

            if (strUserId.equals("")) {
                Intent intent = new Intent(SplashActivity.this, Login1Activity.class);
                intent.putExtra("val", "567");
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                //sharedPreference_main.set_version_check(strVersionCheck);
                intent.putExtra("login_key", "login_value");
                // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsRejected.clear();
        if (requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
            if (permissionsToRequest != null) {
                for (String permission : permissionsToRequest) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                            permissionsRejected.add(permission);
                        }
                    }
                }
            }

            if (permissionsRejected.size() > 0) {
                String message = getResources().getString(R.string.these_permissions_are_mandatory_for_this_application_please_allow_access);
                alert(message);
            } else {
                launchLocation();
            }
        }
    }

    private void alert(String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(message);
        alert.setPositiveButton(getResources().getString(R.string.ok), this);
        alert.setNegativeButton(getResources().getString(R.string.cancel), this);
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(context, permissionsRejected.toArray(new String[permissionsRejected.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
        } else {
            dialog.dismiss();
            finish();
        }
    }
}
