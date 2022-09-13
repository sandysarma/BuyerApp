package com.buyer.buyerApp.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.buyer.buyerApp.Adapter.OrderHistoryAdapter;

import com.buyer.buyerApp.Class.InterfaceCancelItemListener;
import com.buyer.buyerApp.Model.OrderList_Model;
import com.buyer.buyerApp.ModelResult.CancelOrderResponse;
import com.buyer.buyerApp.ModelResult.OrderList_Response;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.ApiClient;
import com.buyer.buyerApp.RetrofitApi.ApiServices;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderListFragment extends Fragment implements InterfaceCancelItemListener {
    RecyclerView orderHistoryRecyclerView;
    List<OrderList_Response> orderListResponseList = new ArrayList<>();

    String userId = "", userType = "";
    InterfaceCancelItemListener interfaceCancelItemListener;
    com.buyer.buyerApp.Adapter.OrderHistoryAdapter OrderHistoryAdapter;
    OrderList_Response orderList_response;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1, container, false);
        interfaceCancelItemListener=OrderListFragment.this;

        orderHistoryRecyclerView = view.findViewById(R.id.orderHistoryRecyclerView);

        RecyclerView.LayoutManager popularLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        orderHistoryRecyclerView.setLayoutManager(popularLayoutManager);
        orderHistoryRecyclerView.setItemAnimator(new DefaultItemAnimator());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");
        userType = sharedPreferences.getString("userType", "");

        OrderList_Api();
        return view;
        }

        public void OrderList_Api() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        progressDialog.show();
        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        Call<OrderList_Model> call = service.getOrderlist("123", userId, userType);
        call.enqueue(new Callback<OrderList_Model>() {
            @Override
            public void onResponse(@NonNull Call<OrderList_Model> call, @NonNull Response<OrderList_Model> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        OrderList_Model result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();


                        if (success.equalsIgnoreCase("true")) {
                            orderListResponseList = result.getOrderListResponses();
                            OrderHistoryAdapter = new OrderHistoryAdapter(getActivity(), orderListResponseList,interfaceCancelItemListener);
                            orderHistoryRecyclerView.setAdapter(OrderHistoryAdapter);

                        }
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(getActivity(), "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(getActivity(), "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(getActivity(), "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(getActivity(), "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(getActivity(), "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(getActivity(), "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(getActivity(), "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(getActivity(), "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<OrderList_Model> call, Throwable t) {
                progressDialog.dismiss();

                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(getActivity(), "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void CancelOrderListItem_Api(OrderList_Response orderList_response, String txt_rsn) {


        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        progressDialog.show();

        ApiServices service= ApiClient.getClient().create(ApiServices.class);
        Call<CancelOrderResponse> call = service.getCancel_Order("1234", String.valueOf(orderList_response.getUserId()), String.valueOf(orderList_response.getUserType()),orderList_response.getOrderNumber(),orderList_response.getProductId(),txt_rsn);
        call.enqueue(new Callback<CancelOrderResponse>() {
            @Override
            public void onResponse(Call<CancelOrderResponse> call, Response<CancelOrderResponse> response) {
                progressDialog.dismiss();
               // Toast.makeText(getActivity(), "Cancel this order", Toast.LENGTH_SHORT).show();
                try {
                    if (response.isSuccessful()) {
                        CancelOrderResponse result = response.body();
                        removeItem(orderList_response);
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(getActivity(), "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(getActivity(), "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(getActivity(), "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(getActivity(), "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(getActivity(), "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(getActivity(), "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(getActivity(), "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(getActivity(), "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<CancelOrderResponse> call, Throwable t) {
             progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(getActivity(), "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void removeItem(OrderList_Response orderList_response) {
        orderListResponseList.remove(orderList_response);
        OrderHistoryAdapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(OrderList_Response orderList_response, String txt_rsn) {
        CancelOrderListItem_Api(orderList_response,txt_rsn);

    }
}

