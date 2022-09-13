package com.buyer.buyerApp;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.buyer.buyerApp.Activity.CartListActivity;
import com.buyer.buyerApp.Fragment.CommonFragment;
import com.buyer.buyerApp.Fragment.ContactUsFragment;
import com.buyer.buyerApp.Fragment.DashboardFragment;
import com.buyer.buyerApp.Fragment.GetQuoteFragment;
import com.buyer.buyerApp.Fragment.MyServiceFragment;
import com.buyer.buyerApp.Fragment.MyTransactionFragment;
import com.buyer.buyerApp.Fragment.NotificationFragment;
import com.buyer.buyerApp.Fragment.OrderHistoryFragment;
import com.buyer.buyerApp.Fragment.SettingFragment;
import com.buyer.buyerApp.Activity.Login1Activity;
import com.buyer.buyerApp.Model.CartListModel;
import com.buyer.buyerApp.Model.ProfileDetailsModel;
import com.buyer.buyerApp.ModelResult.CartListResponse;
import com.buyer.buyerApp.ModelResult.ProfileDetailsResponse;
import com.buyer.buyerApp.RetrofitApi.ApiClient;
import com.buyer.buyerApp.RetrofitApi.ApiServices;
import com.google.gson.JsonSyntaxException;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends SlidingFragmentActivity {

    public static int navItemIndex = 0;
    private static final String TAG_DASHBOARD = "services";
    public static String CURRENT_TAG = TAG_DASHBOARD;
    private static final String TAG_MY_SERVICES = "my_services";
    private static final String TAG_ORDER_HISTORY = "order_history";
    private static final String TAG_NOTIFICATION = "notification";
    private static final String TAG_PROFILE = "profile";
    private static final String TAG_SETTINGS = "settings";
    private static final String TAG_ABOUT_US = "about_us";
    private static final String TAG_PRIVACY_POLICY = "privacy_policy";
    private static final String TAG_TERM_CONDITION = "terms_condition";
    private static final String TAG_MY_TRANSACTION = "my_transaction";
    private static final String TAG_GETA_QUOTE = "GETA_QUOTE ";
    boolean doubleBackToExitPressedOnce = false;

    TextView appTitleTxt, cartTxt, userNameTxt, forgtPhonetxt;
    ImageButton btnMenuImage;
    RelativeLayout buttonMenuLayout, fragment_contaner;

    RelativeLayout dashboard, my_services, my_transaction, order_history, notifications,
            setting, contact, get_a_quotes, about_us, privacy_policy, term_condtion, logout;

    RelativeLayout relative_cartList;
    private AlertDialog dialogs;
    SharedPreferences sharedPreferences;

    private String userType, userId;
    ProfileDetailsResponse profileDetailsResponse;
    List<CartListResponse> cartListResponseList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.menu_layout);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contaner, new DashboardFragment()).commit();


        btnMenuImage = (ImageButton) findViewById(R.id.btnMenuImage);
        buttonMenuLayout = (RelativeLayout) findViewById(R.id.buttonMenuLayout);
        fragment_contaner = (RelativeLayout) findViewById(R.id.fragment_contaner);
        userNameTxt = (TextView) findViewById(R.id.userNameTxt);
        appTitleTxt = (TextView) findViewById(R.id.appTitleTxt);
        forgtPhonetxt = (TextView) findViewById(R.id.forgtPhonetxt);

        relative_cartList = (RelativeLayout) findViewById(R.id.relative_cartList);
        cartTxt = (TextView) findViewById(R.id.cartTxt);

        sharedPreferences = MainActivity.this.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");
        userType = sharedPreferences.getString("userType", "");


        dashboard = (RelativeLayout) findViewById(R.id.dashboard);
        my_services = (RelativeLayout) findViewById(R.id.my_services);
        my_transaction = (RelativeLayout) findViewById(R.id.my_transaction);
        order_history = (RelativeLayout) findViewById(R.id.order_history);
        notifications = (RelativeLayout) findViewById(R.id.notifications);
        setting = (RelativeLayout) findViewById(R.id.setting);
        contact = (RelativeLayout) findViewById(R.id.contact);
        get_a_quotes = (RelativeLayout) findViewById(R.id.get_a_quotes);
        about_us = (RelativeLayout) findViewById(R.id.about_us);
        privacy_policy = (RelativeLayout) findViewById(R.id.privacy_policy);
        term_condtion = (RelativeLayout) findViewById(R.id.term_condtion);
        logout = (RelativeLayout) findViewById(R.id.logout);


        try {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

            final Display display = getWindowManager().getDefaultDisplay();
            int screenWidth = display.getWidth();
            final int slidingmenuWidth = (int) (screenWidth - (screenWidth / 3.7) + 23);
            final int offset = Math.max(0, screenWidth - slidingmenuWidth);
            getSlidingMenu().setBehindOffset(offset);
            getSlidingMenu().toggle();
            getSlidingMenu().isVerticalFadingEdgeEnabled();
            getSlidingMenu().isHorizontalFadingEdgeEnabled();
            getSlidingMenu().setMode(SlidingMenu.LEFT);
            getSlidingMenu().setFadeEnabled(true);
            getSlidingMenu().setFadeDegree(0.8f);
            getSlidingMenu().setFadingEdgeLength(13);
            getSlidingMenu().setEnabled(false);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("slidingexception", e.toString());
        }

        btnMenuImage.setOnClickListener(v -> {
            try {
                showMenu();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        });

        buttonMenuLayout.setOnClickListener(v -> {
            try {
                showMenu();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            } catch (NullPointerException e) {
            }
        });

        if (userType.equals("2")) {
            ProfileDetailsGroup_Api();
        } else {
            ProfileDetailsIndividual_Api();
        }

        relative_cartList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cartlistIntent = new Intent(MainActivity.this, CartListActivity.class);
                startActivity(cartlistIntent);
            }
        });

        CartList_Api();
        setupLeftNavigationView();

    }

    public void CartList_Api() {
        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        retrofit2.Call<CartListModel> call = service.getCartList("123", userType, userId);
        call.enqueue(new Callback<CartListModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<CartListModel> call, @NonNull retrofit2.Response<CartListModel> response) {
                try {
                    if (response.isSuccessful()) {
                        CartListModel result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();

                        if (success.equalsIgnoreCase("true")) {
                            cartListResponseList = result.getCartListResponse();

                            cartTxt.setText("" + cartListResponseList.size());
                            // Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                            Log.e("MainActivity", "112    " + cartListResponseList.size());
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(MainActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(MainActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(MainActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(MainActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(MainActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(MainActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(MainActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(MainActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            }

            @Override
            public void onFailure(@NonNull Call<CartListModel> call, Throwable t) {

                if (t instanceof IOException) {
                    Toast.makeText(MainActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(MainActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void ProfileDetailsGroup_Api() {
        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        retrofit2.Call<ProfileDetailsModel> call = service.getProfileDetails("123", userType, userId);
        call.enqueue(new Callback<ProfileDetailsModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ProfileDetailsModel> call, @NonNull retrofit2.Response<ProfileDetailsModel> response) {
                try {
                    if (response.isSuccessful()) {
                        ProfileDetailsModel result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();

                        if (success.equalsIgnoreCase("true")) {
                            profileDetailsResponse = result.getProfileDetailsResponse();

                            userNameTxt.setText(profileDetailsResponse.getNameOfLeader());
                            forgtPhonetxt.setText(profileDetailsResponse.getEmail());
                        } else {
                            //  Toast.makeText(MainActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(MainActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(MainActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(MainActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(MainActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(MainActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(MainActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(MainActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(MainActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            }

            @Override
            public void onFailure(@NonNull Call<ProfileDetailsModel> call, Throwable t) {

                if (t instanceof IOException) {
                    Toast.makeText(MainActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(MainActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ProfileDetailsIndividual_Api() {

        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        retrofit2.Call<ProfileDetailsModel> call = service.getProfileDetails("123", userType, userId);
        call.enqueue(new Callback<ProfileDetailsModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ProfileDetailsModel> call, @NonNull retrofit2.Response<ProfileDetailsModel> response) {
                try {
                    if (response.isSuccessful()) {
                        ProfileDetailsModel result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();

                        if (success.equalsIgnoreCase("true")) {
                            profileDetailsResponse = result.getProfileDetailsResponse();

                            userNameTxt.setText(profileDetailsResponse.getNameOfLeader());
                            forgtPhonetxt.setText(profileDetailsResponse.getEmail());
                        } else {
                            //  Toast.makeText(MainActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(MainActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(MainActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(MainActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(MainActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(MainActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(MainActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(MainActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(MainActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            }

            @Override
            public void onFailure(@NonNull Call<ProfileDetailsModel> call, Throwable t) {

                if (t instanceof IOException) {
                    Toast.makeText(MainActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(MainActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void setupLeftNavigationView() {
        String terms_key = getIntent().getStringExtra("login_key");

        if (terms_key.equals("login_value")) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_DASHBOARD;
            appTitleTxt.setText(getString(R.string.smartForm));
            DashboardFragment dashBoardFragment = new DashboardFragment();
            androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
            ((RelativeLayout) findViewById(R.id.fragment_contaner)).removeAllViews();
            androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_contaner, dashBoardFragment, CURRENT_TAG);
            fragmentTransaction.commit();


        } else if (terms_key.equals("order_detail")) {
            navItemIndex = 2;
            CURRENT_TAG = TAG_ORDER_HISTORY;
            appTitleTxt.setText("Order History List");
            Bundle bundle = new Bundle();
            bundle.putString("value", "oder");
            OrderHistoryFragment orderHistoryFragment = new OrderHistoryFragment();
            orderHistoryFragment.setArguments(bundle);
            androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
            ((RelativeLayout) findViewById(R.id.fragment_contaner)).removeAllViews();
            androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_contaner, orderHistoryFragment, CURRENT_TAG);
            fragmentTransaction.commit();
            getSlidingMenu().toggle();
        } else if (terms_key.equals("order_cancel")) {
            navItemIndex = 2;
            CURRENT_TAG = TAG_ORDER_HISTORY;
            appTitleTxt.setText("Order History List");
            Bundle bundle = new Bundle();
            bundle.putString("value", "cancel");
            OrderHistoryFragment orderHistoryFragment = new OrderHistoryFragment();
            orderHistoryFragment.setArguments(bundle);
            androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
            ((RelativeLayout) findViewById(R.id.fragment_contaner)).removeAllViews();
            androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_contaner, orderHistoryFragment, CURRENT_TAG);
            fragmentTransaction.commit();
            getSlidingMenu().toggle();
        } else if (terms_key.equals("my_service")) {
            navItemIndex = 1;
            CURRENT_TAG = TAG_MY_SERVICES;
            appTitleTxt.setText(getString(R.string.my_service));
            MyServiceFragment myServicesFragment = new MyServiceFragment();
            androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
            ((RelativeLayout) findViewById(R.id.fragment_contaner)).removeAllViews();
            androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_contaner, myServicesFragment, CURRENT_TAG);
            fragmentTransaction.commit();
            getSlidingMenu().toggle();
        } else if (terms_key.equals("my_service_cancel")) {
            navItemIndex = 1;
            CURRENT_TAG = TAG_MY_SERVICES;
            appTitleTxt.setText(getString(R.string.my_service));
            MyServiceFragment myServicesFragment = new MyServiceFragment();
            androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
            ((RelativeLayout) findViewById(R.id.fragment_contaner)).removeAllViews();
            androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_contaner, myServicesFragment, CURRENT_TAG);
            fragmentTransaction.commit();
            getSlidingMenu().toggle();
        }


        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_DASHBOARD;
                appTitleTxt.setText(getString(R.string.smartForm));
                DashboardFragment dashBoardFragment = new DashboardFragment();
                androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
                ((RelativeLayout) findViewById(R.id.fragment_contaner)).removeAllViews();
                androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_contaner, dashBoardFragment, CURRENT_TAG);
                fragmentTransaction.commit();
                getSlidingMenu().toggle();

            }
        });

        my_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navItemIndex = 1;
                CURRENT_TAG = TAG_MY_SERVICES;
                appTitleTxt.setText(getString(R.string.my_service));
                MyServiceFragment serviceFragment = new MyServiceFragment();
                androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
                ((RelativeLayout) findViewById(R.id.fragment_contaner)).removeAllViews();
                androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_contaner, serviceFragment, CURRENT_TAG);
                fragmentTransaction.commit();
                getSlidingMenu().toggle();
            }
        });

        my_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navItemIndex = 2;
                CURRENT_TAG = TAG_MY_TRANSACTION;
                appTitleTxt.setText(getString(R.string.my_transaction));
                MyTransactionFragment transactionFragment = new MyTransactionFragment();
                androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
                ((RelativeLayout) findViewById(R.id.fragment_contaner)).removeAllViews();
                androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_contaner, transactionFragment, CURRENT_TAG);
                fragmentTransaction.commit();
                getSlidingMenu().toggle();
            }
        });

        order_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navItemIndex = 3;
                CURRENT_TAG = TAG_ORDER_HISTORY;
                appTitleTxt.setText(getString(R.string.order_history_list));
                OrderHistoryFragment orderHistoryFragment = new OrderHistoryFragment();
                androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
                ((RelativeLayout) findViewById(R.id.fragment_contaner)).removeAllViews();
                androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_contaner, orderHistoryFragment, CURRENT_TAG);
                fragmentTransaction.commit();
                getSlidingMenu().toggle();
            }
        });

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navItemIndex = 4;
                CURRENT_TAG = TAG_NOTIFICATION;
                appTitleTxt.setText(getString(R.string.notifications));
                NotificationFragment notificationFragment = new NotificationFragment();
                androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
                ((RelativeLayout) findViewById(R.id.fragment_contaner)).removeAllViews();
                androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_contaner, notificationFragment, CURRENT_TAG);
                fragmentTransaction.commit();
                getSlidingMenu().toggle();
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navItemIndex = 5;
                CURRENT_TAG = TAG_SETTINGS;
                appTitleTxt.setText(getString(R.string.settings));
                SettingFragment settingFragment = new SettingFragment();
                androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
                ((RelativeLayout) findViewById(R.id.fragment_contaner)).removeAllViews();
                androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_contaner, settingFragment, CURRENT_TAG);
                fragmentTransaction.commit();
                getSlidingMenu().toggle();
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navItemIndex = 6;
                CURRENT_TAG = TAG_ABOUT_US;
                appTitleTxt.setText(getString(R.string.contact_us));
                ContactUsFragment contactUsFragment = new ContactUsFragment();
                androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
                ((RelativeLayout) findViewById(R.id.fragment_contaner)).removeAllViews();
                androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_contaner, contactUsFragment, CURRENT_TAG);
                fragmentTransaction.commit();
                getSlidingMenu().toggle();
            }
        });

        get_a_quotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navItemIndex = 7;
                CURRENT_TAG = TAG_GETA_QUOTE;
                appTitleTxt.setText(getString(R.string.get_quotes));
                GetQuoteFragment getQuoteFragment = new GetQuoteFragment();
                androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
                ((RelativeLayout) findViewById(R.id.fragment_contaner)).removeAllViews();
                androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_contaner, getQuoteFragment, CURRENT_TAG);
                fragmentTransaction.commit();
                getSlidingMenu().toggle();
            }
        });

        about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navItemIndex = 8;
                CURRENT_TAG = TAG_ABOUT_US;
                appTitleTxt.setText(getString(R.string.about_us));
                Bundle bundle = new Bundle();
                bundle.putString("value", "about");
                CommonFragment commanFragment = new CommonFragment();
                commanFragment.setArguments(bundle);
                androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
                ((RelativeLayout) findViewById(R.id.fragment_contaner)).removeAllViews();
                androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_contaner, commanFragment, CURRENT_TAG);
                fragmentTransaction.commit();
                getSlidingMenu().toggle();
            }
        });

        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navItemIndex = 9;
                CURRENT_TAG = TAG_ABOUT_US;
                appTitleTxt.setText(getString(R.string.privacy_policy));
                Bundle bundle = new Bundle();
                bundle.putString("value", "privacy");
                CommonFragment commanFragment = new CommonFragment();
                commanFragment.setArguments(bundle);
                androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
                ((RelativeLayout) findViewById(R.id.fragment_contaner)).removeAllViews();
                androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_contaner, commanFragment, CURRENT_TAG);
                fragmentTransaction.commit();
                getSlidingMenu().toggle();
            }
        });

        term_condtion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navItemIndex = 10;
                CURRENT_TAG = TAG_ABOUT_US;
                appTitleTxt.setText(getString(R.string.term_condition));
                Bundle bundle = new Bundle();
                bundle.putString("value", "other");
                CommonFragment commanFragment = new CommonFragment();
                commanFragment.setArguments(bundle);
                androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
                ((RelativeLayout) findViewById(R.id.fragment_contaner)).removeAllViews();
                androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_contaner, commanFragment, CURRENT_TAG);
                fragmentTransaction.commit();
                getSlidingMenu().toggle();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout_AlertDialog();
            }
        });
    }

    public void Logout_AlertDialog() {

        final LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.clear_all_cartlist_item, null);
        final TextView tvMessage = alertLayout.findViewById(R.id.textViewMessage);
        final TextView btnYes = alertLayout.findViewById(R.id.btnd_delete);
        final TextView btnNo = alertLayout.findViewById(R.id.btn_cancel);

        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle(MainActivity.this.getString(R.string.confirm_logout));
        tvMessage.setText(MainActivity.this.getString(R.string.are_you_sure_to_logout));
        alert.setView(alertLayout);
        alert.setCancelable(false);

        btnYes.setOnClickListener(v -> {
            dialogs.dismiss();
            logout.setEnabled(true);
            SharedPreferences preferences = MainActivity.this.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
            preferences.edit().remove("userId").apply();
            Intent intent = new Intent(MainActivity.this, Login1Activity.class);
            startActivity(intent);
            finish();
        });

        btnNo.setOnClickListener(v -> {
            dialogs.dismiss();
            logout.setEnabled(true);
        });

        dialogs = alert.create();
        dialogs.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        UiModeManager uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
        CartList_Api();


    }

    @Override
    public void onBackPressed() {
        boolean shouldLoadHomeFragOnBackPress = true;
        if (shouldLoadHomeFragOnBackPress) {
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_DASHBOARD;
                loadHomeFragment();
            } else {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
                return;
            }
        }
    }

    private void loadHomeFragment() {
        navItemIndex = 0;
        CURRENT_TAG = TAG_DASHBOARD;
        appTitleTxt.setText(getString(R.string.smartForm));
        DashboardFragment dashBoardFragment = new DashboardFragment();
        androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
        ((RelativeLayout) findViewById(R.id.fragment_contaner)).removeAllViews();
        androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_contaner, dashBoardFragment, CURRENT_TAG);
        fragmentTransaction.commit();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        UiModeManager uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
    }


}