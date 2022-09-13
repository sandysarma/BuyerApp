package com.buyer.buyerApp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buyer.buyerApp.Adapter.CartListAdapter;
import com.buyer.buyerApp.Class.InterfacrItemClickListener;
import com.buyer.buyerApp.Class.RefreshInterface;
import com.buyer.buyerApp.MainActivity;
import com.buyer.buyerApp.Model.CartListModel;
import com.buyer.buyerApp.Model.ClearAllCartitems_Model;
import com.buyer.buyerApp.Model.CommonModel;
import com.buyer.buyerApp.Model.Increment_DecrementModel;
import com.buyer.buyerApp.Model.ProceedToCheckOut_Model;
import com.buyer.buyerApp.ModelResult.CartListResponse;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.ApiClient;
import com.buyer.buyerApp.RetrofitApi.ApiServices;
import com.buyer.buyerApp.Util.SharedPrefClass;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartListActivity extends AppCompatActivity  implements RefreshInterface, InterfacrItemClickListener {


    LinearLayout back_arrow_button;
    RelativeLayout Empty_layout;
    CardView relative_checkout;
    RecyclerView cartListRecyclerView;
    CartListAdapter cartListAdapter;
    String userId = "", userType = "";
    TextView subtotalTxt, cart_total_amount, ClearAllItemTxtBtn, text_proceed_to_chckout;
    RefreshInterface refreshInterface;
    List<CartListResponse> cartListResponseList = new ArrayList<>();
    CartListResponse cartListResponse;
    ArrayList<String> quantitylist=new ArrayList<>();
    ArrayList<String> productidlist=new ArrayList<>();
    private AlertDialog dialogs;
    final Context context = this;
    SharedPrefClass sharedPrefClass;
    String finalproductid="";
    String finalquantity="";
    String finalproducttotal="";
    int total_price= 0;
    InterfacrItemClickListener interfacrItemClickListener;
    private boolean ischecked=true;
    public static int navItemIndex = 0;
    private static final String TAG_DASHBOARD = "services";
    public static String CURRENT_TAG = TAG_DASHBOARD;

    float totalPrice = 0,finalTotalPrice=0,newtotal=0;
    float newtotalPrice = 0,newfinalTotalPrice=0;
    float finalprice=0;
    float t_price=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        refreshInterface = this;
        interfacrItemClickListener=this;
        sharedPrefClass=new SharedPrefClass(this);

        back_arrow_button = findViewById(R.id.back_arrow_button);
        cartListRecyclerView = findViewById(R.id.cartListRecyclerView);
        subtotalTxt = findViewById(R.id.subtotalTxt);
        ClearAllItemTxtBtn = findViewById(R.id.ClearAllItemTxtBtn);
        cart_total_amount = findViewById(R.id.cart_total_amount1);
        relative_checkout = findViewById(R.id.relative_checkout);
        Empty_layout = findViewById(R.id.Empty_layout);
        text_proceed_to_chckout = findViewById(R.id.text_proceed_to_chckout);

        // String finalprise= String.valueOf(finalTotalPrice);

        SharedPreferences sharedPreferences = CartListActivity.this.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");
        userType = sharedPreferences.getString("userType", "");

        back_arrow_button.setOnClickListener(v -> finish());
        text_proceed_to_chckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.cart_list_address_payment);
                dialog.setTitle("Title...");
                // dialog.getWindow().setLayout(700, 900);

                SharedPrefClass sharedPrefClass=new SharedPrefClass(CartListActivity.this);
                EditText name=(EditText) dialog.findViewById(R.id.fullnameEdt);
                EditText phoneno=(EditText) dialog.findViewById(R.id.phoneEdt);
                EditText address=(EditText) dialog.findViewById(R.id.cityEdt);
                EditText zipcode=(EditText) dialog.findViewById(R.id.pinCodeEdt);
                ImageView imageView =(ImageView) dialog.findViewById(R.id.back_arrow1);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                TextView cod=(TextView) dialog.findViewById(R.id.continueTxtBtn);
                // if button is clicked, close the custom dialog
                cod.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String edit_name = name.getText().toString();
                        String edt_phoneno= phoneno.getText().toString();
                        String edt_address= address.getText().toString();
                        String edt_zipcode=zipcode.getText().toString();
                        Log.e("sfsadfad","egdsss"+edit_name);
                        Log.e("byjtr",edt_phoneno);
                        Log.e("rytrdg","tyhdgbdfg"+edt_address);
                        if(edit_name!=null && edit_name.trim().length()==0) {
                            Toast.makeText(CartListActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                        }else if(edt_phoneno!=null && edt_phoneno.trim().length()==0 ){
                            Toast.makeText(CartListActivity.this, "Please enter phoneno", Toast.LENGTH_SHORT).show();
                        }else if (edt_phoneno.trim().length()<8) {
                            Toast toast = Toast.makeText(CartListActivity.this, getString(R.string.Phone_no_minimum_length), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                        }
                        else if(edt_address!=null && edt_address.trim().length()==0){
                            Toast.makeText(CartListActivity.this, "Please enter address", Toast.LENGTH_SHORT).show();
                        }else  if(edt_zipcode != null && edt_zipcode.trim().length()==0){
                            Toast.makeText(CartListActivity.this, "Please enter zipcode", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            ProceedToCheck_Api(edit_name,edt_address,edt_phoneno,edt_zipcode);
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });

        ClearAllItemTxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final LayoutInflater inflater = CartListActivity.this.getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.clear_all_cartlist_item, null);
                final TextView tvMessage = alertLayout.findViewById(R.id.textViewMessage);
                final TextView btnDelete = alertLayout.findViewById(R.id.btnd_delete);
                final TextView btncancel = alertLayout.findViewById(R.id.btn_cancel);

                final AlertDialog.Builder alert = new AlertDialog.Builder(CartListActivity.this);
                alert.setTitle(CartListActivity.this.getString(R.string.clear_all_cart_item));
                tvMessage.setText(CartListActivity.this.getString(R.string.Are_you_sure_to_clear));
                alert.setView(alertLayout);
                alert.setCancelable(false);


                btncancel.setOnClickListener(v1 ->
                        dialogs.dismiss());

                btnDelete.setOnClickListener(v1 -> {
                    clearall_Api();
                    dialogs.dismiss();
                });

                dialogs = alert.create();
                dialogs.show();
            }
        });

        RecyclerView.LayoutManager productsLayoutManager = new LinearLayoutManager(CartListActivity.this, LinearLayoutManager.VERTICAL, false);
        cartListRecyclerView.setLayoutManager(productsLayoutManager);
        cartListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        cartListAdapter = new CartListAdapter(CartListActivity.this, refreshInterface,interfacrItemClickListener);

        if(cartListResponseList.size()==0){
            relative_checkout.setVisibility(View.GONE);
        }else {
            relative_checkout.setVisibility(View.VISIBLE);
        }

        CartList_Api();
    }
    private void incrementApi(int qty, CartListResponse cartListResponse, int position) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);
        progressDialog.hide();
        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        // progressDialog.show();
        progressDialog.hide();
        ApiServices service = ApiClient.getClient().create(ApiServices.class);

        Call<Increment_DecrementModel> call = service.getIncre_decre_cart("12345", String.valueOf(cartListResponse.getUserId()), String.valueOf(cartListResponse.getUserType()),cartListResponse.getProductId(),""+qty);
        call.enqueue(new Callback<Increment_DecrementModel>() {
            @Override
            public void onResponse(Call<Increment_DecrementModel> call, Response<Increment_DecrementModel> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful()) {
                        Increment_DecrementModel result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();

                        CartList_Api();
                        Toast.makeText(CartListActivity.this, message, Toast.LENGTH_SHORT).show();

                     /*  subtotalTxt.setText(""+finalTotalPrice);
                        cart_total_amount.setText(""+finalTotalPrice);*/


                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(CartListActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(CartListActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(CartListActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(CartListActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(CartListActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(CartListActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(CartListActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(CartListActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<Increment_DecrementModel> call, Throwable t) {
                progressDialog.dismiss();

                if (t instanceof IOException) {
                    Toast.makeText(CartListActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(CartListActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @SuppressLint("LongLogTag")
    public void ProceedToCheck_Api(String edit_name, String edt_address, String edt_phoneno, String edt_zipcode) {

        String withoutConryCodeNumberCheckout = "";
        withoutConryCodeNumberCheckout = "+91"+edt_phoneno;
        Log.e("withoutConryCodeNumberCheckout",withoutConryCodeNumberCheckout);
        final ProgressDialog progressDialog = new ProgressDialog(CartListActivity.this, R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        progressDialog.show();

        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        Call<ProceedToCheckOut_Model> call = service.getProceedCheckOut("12345", userType,userId,
                finalproductid,finalquantity, "1",finalproducttotal,edit_name,withoutConryCodeNumberCheckout,edt_address,edt_zipcode);
        call.enqueue(new Callback<ProceedToCheckOut_Model>() {
            @Override
            public void onResponse(@NonNull Call<ProceedToCheckOut_Model> call, @NonNull Response<ProceedToCheckOut_Model> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        ProceedToCheckOut_Model result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();
                        cartListAdapter.clearList();
                        Toast.makeText(CartListActivity.this, message, Toast.LENGTH_SHORT).show();
                       /* Intent intent =new Intent(CartListActivity.this, MainActivity.class);
                         startActivity(intent);*/

                        Intent intent = new Intent(CartListActivity.this, MainActivity.class);
                        intent.putExtra("login_key", "login_value");
                        startActivity(intent);
                        finish();

                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(CartListActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(CartListActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(CartListActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(CartListActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(CartListActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(CartListActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(CartListActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(CartListActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<ProceedToCheckOut_Model> call, Throwable t) {
                progressDialog.dismiss();

                if (t instanceof IOException) {
                    Toast.makeText(CartListActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(CartListActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void clearall_Api() {
        final ProgressDialog progressDialog = new ProgressDialog(CartListActivity.this, R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        progressDialog.show();

        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        Call<ClearAllCartitems_Model> call = service.getClearallCartList("123", userType,userId);
        call.enqueue(new Callback<ClearAllCartitems_Model>() {
            @Override
            public void onResponse(@NonNull Call<ClearAllCartitems_Model> call, @NonNull Response<ClearAllCartitems_Model> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        ClearAllCartitems_Model result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();
                        CartList_Api();
                        Toast.makeText(CartListActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(CartListActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(CartListActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(CartListActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(CartListActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(CartListActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(CartListActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(CartListActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(CartListActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<ClearAllCartitems_Model> call, Throwable t) {
                progressDialog.dismiss();

                if (t instanceof IOException) {
                    Toast.makeText(CartListActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(CartListActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void CartList_Api() {
        final ProgressDialog progressDialog = new ProgressDialog(CartListActivity.this, R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);
        progressDialog.hide();
        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        // progressDialog.show();
        progressDialog.hide();
        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        Call<CartListModel> call = service.getCartList("123", userType, userId);
        call.enqueue(new Callback<CartListModel>() {
            @Override
            public void onResponse(@NonNull Call<CartListModel> call, @NonNull Response<CartListModel> response) {
                progressDialog.dismiss();
                Log.e("dadsdsad","sdsadasdsdcdb"+userType +  "    " +     userId);
                try {
                    if (response.isSuccessful()) {
                        CartListModel result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();
                        float totalPrice = 0,finalTotalPrice=0;

                        if (success.equalsIgnoreCase("true")) {
                            cartListResponseList = result.getCartListResponse();
                            if (cartListResponseList.size() == 0) {
                                relative_checkout.setVisibility(View.GONE);
                                ClearAllItemTxtBtn.setVisibility(View.GONE);
                                cartListRecyclerView.setVisibility(View.GONE);
                                Empty_layout.setVisibility(View.VISIBLE);
                            } else {
                                relative_checkout.setVisibility(View.VISIBLE);
                                cartListRecyclerView.setVisibility(View.VISIBLE);
                                Empty_layout.setVisibility(View.GONE);
                                ClearAllItemTxtBtn.setVisibility(View.VISIBLE);
                                for (int i = 0; i < cartListResponseList.size(); i++) {
                                    String strPrice = cartListResponseList.get(i).getTotalPrice();
                                    totalPrice = Float.parseFloat(strPrice);
                                    finalTotalPrice = finalTotalPrice + totalPrice;
                                    cart_total_amount.setText("$" + finalTotalPrice);
                                    subtotalTxt.setText("$" + finalTotalPrice);
                                    finalproducttotal= String.valueOf(finalTotalPrice);
                                    Log.e("sfdra","regregw"+finalproducttotal);
                                    String productid=cartListResponseList.get(i).getProductId();
                                    String quantity=cartListResponseList.get(i).getQuantity();
                                    // String quantity1= quantity + ""+cartListResponseList.get(i).getQuantity();
                                    //   quantitylist.add(cartListResponseList.get(i).getQuantity());
                                    //  productidlist.add(cartListResponseList.get(i).getProductId());

                                    if(i==0){
                                        finalproductid=productid;
                                        finalquantity=quantity;
                                    }else {
                                        finalproductid=finalproductid+","+productid;
                                        finalquantity=finalquantity+","+quantity;
                                    }
                                }
                                cartListAdapter.addItem(cartListResponseList);
                                cartListRecyclerView.setAdapter(cartListAdapter);
                            }
                        }
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(CartListActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(CartListActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(CartListActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(CartListActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(CartListActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(CartListActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(CartListActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(CartListActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<CartListModel> call, Throwable t) {
                progressDialog.dismiss();

                if (t instanceof IOException) {
                    Toast.makeText(CartListActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(CartListActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void Refresh() {
        CartList_Api();
    }


    private void derementApi(int qty, CartListResponse cartListResponse, int position) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);
        progressDialog.hide();
        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        //progressDialog.show();
        progressDialog.hide();
        ApiServices service = ApiClient.getClient().create(ApiServices.class);

        Call<Increment_DecrementModel> call = service.getIncre_decre_cart("12345", String.valueOf(cartListResponse.getUserId()), String.valueOf(cartListResponse.getUserType()),cartListResponse.getProductId(),""+qty);
        call.enqueue(new Callback<Increment_DecrementModel>() {
            @Override
            public void onResponse(Call<Increment_DecrementModel> call, Response<Increment_DecrementModel> response) {

                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        Increment_DecrementModel  result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();
                        CartList_Api();



                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(CartListActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(CartListActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(CartListActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(CartListActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(CartListActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(CartListActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(CartListActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(CartListActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<Increment_DecrementModel> call, Throwable t) {
                progressDialog.dismiss();

                if (t instanceof IOException) {
                    Toast.makeText(CartListActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(CartListActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }




    private void RemoveItem_AlertDialog(int position, CartListResponse cartListResponse, String id) {
        //  CartListActivity cartListActivity = (CartListActivity) view.getContext();
        final LayoutInflater inflater = this.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.clear_all_cartlist_item, null);
        final TextView tvMessage = alertLayout.findViewById(R.id.textViewMessage);
        final TextView btnDelete = alertLayout.findViewById(R.id.btnd_delete);
        final TextView btncancel = alertLayout.findViewById(R.id.btn_cancel);

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(context.getString(R.string.remove_cart_item));
        tvMessage.setText(context.getString(R.string.Are_you_sure_to_remove_this_item_from_the_cart));
        alert.setView(alertLayout);
        alert.setCancelable(false);

        btncancel.setOnClickListener(v -> {
            dialogs.dismiss();
            //  removeItemTxtBtn.setEnabled(true);
        });

        btnDelete.setOnClickListener(v -> {
            // dialogs.dismiss();
            //  removeItemTxtBtn.setEnabled(true);
            DeleteCartItem_Api(position,id,cartListResponse);
        });

        dialogs = alert.create();
        dialogs.show();
    }

    private void DeleteCartItem_Api(int position, String id, CartListResponse cartListResponse) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        progressDialog.show();

        ApiServices service = ApiClient.getClient().create(ApiServices.class);

        Call<CommonModel> call = service.getDeleteCartItem("123", userType, userId, id);
        //calling the api
        call.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        CommonModel result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();

                        if (success.equals("true")) {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            removeItem(position,cartListResponse);
                            refreshInterface.Refresh();
                            dialogs.dismiss();
                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(context, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(context, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(context, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(context, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(context, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(context, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(context, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(context, "unknown error", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void removeItem(int position, CartListResponse cartListResponse) {
        try {
            cartListResponseList.remove(position);
            cartListAdapter.notifyDataSetChanged();
        } catch (IndexOutOfBoundsException index) {
            index.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(String id, CartListResponse cartListResponse, int position) {
        RemoveItem_AlertDialog(position, cartListResponse,id);
    }

    @Override
    public void onItemClickChange(int quantity, CartListResponse cartListResponse, int position) {
        incrementApi(quantity,cartListResponse,position);


    }

    @Override
    public void onDecrementClick(int quantity, CartListResponse cartListResponse, int position) {
        derementApi(quantity,cartListResponse,position);

    }
}