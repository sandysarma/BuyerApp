package com.buyer.buyerApp.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.buyer.buyerApp.Adapter.MyServiceAdapter;
import com.buyer.buyerApp.Model.CommonModel;
import com.buyer.buyerApp.Model.ServiceRequestListModel;
import com.buyer.buyerApp.ModelResult.ServiceRequestListResponse;
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


public class MyServiceFragment extends Fragment {

    RecyclerView myServicesRecyclerView;
    MyServiceAdapter myServicesAdapter;
    ImageView emailusimail, phoneusimage;
    LinearLayout dropdownlayout;
    RelativeLayout MyserviceListEmptyLayout;
    Spinner spinner;
    TextView clearAllMyServices;
    String userId = "", userType = "", status = "";
    List<ServiceRequestListResponse> serviceRequestListResponseList = new ArrayList<>();
    String ServiceType = "";
    private AlertDialog dialogs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View  view =inflater.inflate(R.layout.fragment_my_service, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");
        userType = sharedPreferences.getString("userType", "");


        myServicesRecyclerView = view.findViewById(R.id.myServicesRecyclerView);
        spinner = view.findViewById(R.id.spinner);
       // emailusimail = view.findViewById(R.id.emailusimail);
        //phoneusimage = view.findViewById(R.id.phoneusimage);
        clearAllMyServices = view.findViewById(R.id.clearAllMyServices);
        MyserviceListEmptyLayout = view.findViewById(R.id.MyserviceListEmptyLayout);

        final List<String> groupstatus = new ArrayList<>();
        groupstatus.add("Sort Service Type");
        groupstatus.add("Pending Service");
        groupstatus.add("Completed Service");
        groupstatus.add("Cancelled Service");
        groupstatus.add("Progress Service");

        spinnerAdapter group_status_adapter = new spinnerAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item);


        group_status_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        group_status_adapter.addAll(groupstatus);
        group_status_adapter.add("Selected Item");
        spinner.setAdapter(group_status_adapter);

        spinner.setSelection(group_status_adapter.getCount());


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                int selectedItemText = spinner.getSelectedItemPosition() + 1;
                status = spinner.getSelectedItem().toString();
                if (status.equalsIgnoreCase("Sort Service Type")) {
                    ServiceType = "";
                    status = "";
                    MyServiceList_Api();
                }  else if (status.equalsIgnoreCase("Pending Service")) {
                    ServiceType = "0";
                    status = "0";
                    MyServiceList_Api();
                } else if (status.equalsIgnoreCase("Completed Service")) {
                    ServiceType = "1";
                    status = "1";
                    MyServiceList_Api();
                } else if (status.equalsIgnoreCase("Cancelled Service")) {
                    ServiceType = "2";
                    status = "2";
                    MyServiceList_Api();
                } else if (status.equalsIgnoreCase("Progress Service")) {
                    ServiceType = "3";
                    status = "3";
                    MyServiceList_Api();
                }

                if (spinner.getSelectedView() != null) {
                    ((TextView) spinner.getSelectedView()).setTextColor(0x63403B3B);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        clearAllMyServices.setOnClickListener(v -> {
//            String status = serviceRequestListResponseList.get(position).getStatus();
            ClearAllServices_AlertDialog();
        });

        RecyclerView.LayoutManager popularLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        myServicesRecyclerView.setLayoutManager(popularLayoutManager);
        myServicesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        MyServiceList_Api();
        return view;

    }

    private void ClearAllServices_AlertDialog() {
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.clear_all_cartlist_item, null);

        final TextView tvMessage = alertLayout.findViewById(R.id.textViewMessage);
        final TextView btnDelete = alertLayout.findViewById(R.id.btnd_delete);
        final TextView btncancel = alertLayout.findViewById(R.id.btn_cancel);

        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(getString(R.string.Remove_myservices_request));
        tvMessage.setText(getString(R.string.Are_you_sure_remove_all_services));
        alert.setView(alertLayout);
        alert.setCancelable(false);

        btncancel.setOnClickListener(v -> {
            dialogs.dismiss();
        });
        btnDelete.setOnClickListener(v -> {
            dialogs.dismiss();
            clearAllMyServices_Api();
        });

        dialogs = alert.create();
        dialogs.show();
    }

    private void clearAllMyServices_Api() {
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
        retrofit2.Call<CommonModel> call = service.getRemoveAllMyServiceItem("123", userType, userId, status);
        call.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<CommonModel> call, @NonNull retrofit2.Response<CommonModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        CommonModel result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                        if (success.equals("true")) {
//                            Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_LONG).show();
                            clearAllMyServices.setVisibility(View.GONE);
                            MyServiceList_Api();

                        } else {
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            MyServiceList_Api();

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
            public void onFailure(@NonNull Call<CommonModel> call, Throwable t) {
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

    private void  MyServiceList_Api() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);
        Log.e("userId",status);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        progressDialog.show();

        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        retrofit2.Call<ServiceRequestListModel> call = service.getServiceRequestList("123", userType, userId, status);
        call.enqueue(new Callback<ServiceRequestListModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ServiceRequestListModel> call, @NonNull retrofit2.Response<ServiceRequestListModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        ServiceRequestListModel result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();
//                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                        if (success.equalsIgnoreCase("true")) {
                            serviceRequestListResponseList = result.getRecord();

                            Log.e("MyServicesFragment","  size    "+serviceRequestListResponseList.size());

                            myServicesAdapter = new MyServiceAdapter(getActivity(), serviceRequestListResponseList, ServiceType);
                            myServicesRecyclerView.setAdapter(myServicesAdapter);

                            if(serviceRequestListResponseList.size() != 0)
                            {
                                clearAllMyServices.setVisibility(View.VISIBLE);
                                MyserviceListEmptyLayout.setVisibility(View.GONE);
                            }else
                            {
                                clearAllMyServices.setVisibility(View.GONE);
                                MyserviceListEmptyLayout.setVisibility(View.VISIBLE);
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
            public void onFailure(@NonNull Call<ServiceRequestListModel> call, Throwable t) {
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

    public class spinnerAdapter extends ArrayAdapter<String> {
        private spinnerAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            int count = super.getCount();
            return count > 0 ? count - 1 : count;
        }
    }
}