package com.buyer.buyerApp.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.buyer.buyerApp.Adapter.NotificationsListAdapter;
import com.buyer.buyerApp.Model.ClearAllNotification_Model;
import com.buyer.buyerApp.Model.NotificationList_Model;
import com.buyer.buyerApp.ModelResult.NotificationListResponce;
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


public class NotificationFragment extends Fragment {
    NotificationsListAdapter notificationsAdapter;
    RecyclerView notificationRecyclerView;
    TextView clearAllNotification;
    AlertDialog dialogs;
    RelativeLayout Empty_Notification_layout;
    List<NotificationListResponce> notificationListResponceList = new ArrayList<>();
    String userId, userType;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.fragment_notification, container, false);

        clearAllNotification = view.findViewById(R.id.clearAllNotification);
        Empty_Notification_layout = view.findViewById(R.id.Empty_Notification_layout);
        notificationRecyclerView = view.findViewById(R.id.notificationRecyclerView);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");
        userType = sharedPreferences.getString("userType", "");

        RecyclerView.LayoutManager popularLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        notificationRecyclerView.setLayoutManager(popularLayoutManager);
        notificationRecyclerView.setItemAnimator(new DefaultItemAnimator());

        clearAllNotification.setOnClickListener(v -> {
            clearAllNotification.setEnabled(false);
            DeleteAllNotification_AlertDialog();
        });
        NotificationList_Api();

        return view;
    }

    private void NotificationList_Api()
    {
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
        retrofit2.Call<NotificationList_Model> call = service.getNotificationList("123",userType,userId);
        call.enqueue(new Callback<NotificationList_Model>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<NotificationList_Model> call, @NonNull retrofit2.Response<NotificationList_Model> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        NotificationList_Model result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();
//                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getContext(), "task successful", Toast.LENGTH_SHORT).show();
                        if (success.equalsIgnoreCase("true")) {
                            notificationListResponceList = result.getRecord();
                            if (notificationListResponceList.size() == 0) {
                                notificationRecyclerView.setVisibility(View.GONE);
                                Empty_Notification_layout.setVisibility(View.VISIBLE);
                                clearAllNotification.setVisibility(View.GONE);
                            } else {
                                notificationRecyclerView.setVisibility(View.VISIBLE);
                                Empty_Notification_layout.setVisibility(View.GONE);
                                clearAllNotification.setVisibility(View.VISIBLE);

                                notificationsAdapter = new NotificationsListAdapter(getActivity() ,notificationListResponceList);
                                notificationRecyclerView.setAdapter(notificationsAdapter);
                            }
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
            public void onFailure(@NonNull Call<NotificationList_Model> call, Throwable t) {
                progressDialog.dismiss();

                if (t instanceof IOException){
                    Toast.makeText(getActivity(), "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(getActivity(), "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void DeleteAllNotification_AlertDialog() {
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.clear_all_cartlist_item, null);

        final TextView tvMessage = alertLayout.findViewById(R.id.textViewMessage);
        final TextView btnDelete = alertLayout.findViewById(R.id.btnd_delete);
        final TextView btncancel = alertLayout.findViewById(R.id.btn_cancel);

        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(getString(R.string.delete_notification));
        tvMessage.setText(getString(R.string.Are_You_sure_delete_all_notification));
        alert.setView(alertLayout);
        alert.setCancelable(false);

        btncancel.setOnClickListener(v -> {
            clearAllNotification.setEnabled(true);
            dialogs.dismiss();
        });
        btnDelete.setOnClickListener(v -> {
            clearAllNotification.setEnabled(true);
            dialogs.dismiss();
            clearAllNotification_Api();
        });

        dialogs = alert.create();
        dialogs.show();
    }

    private void clearAllNotification_Api()
    {
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
        Call<ClearAllNotification_Model> call = service.getClearAllNotification(userType,userId);
        call.enqueue(new Callback<ClearAllNotification_Model>() {
            @Override
            public void onResponse(@NonNull Call<ClearAllNotification_Model> call, @NonNull retrofit2.Response<ClearAllNotification_Model> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        ClearAllNotification_Model result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();
                        NotificationList_Api();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

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
            public void onFailure(@NonNull Call<ClearAllNotification_Model> call, Throwable t) {
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
}