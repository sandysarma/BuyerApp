package com.buyer.buyerApp.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.buyer.buyerApp.Adapter.AdapterCompletedList;
import com.buyer.buyerApp.Model.OrderCompleteModel;
import com.buyer.buyerApp.ModelResult.CompleteListResult;
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


public class CompletedFragment extends Fragment {

    List<CompleteListResult> completeListResultList = new ArrayList<>();
    RecyclerView orderCompletedRecyclerView;
    AdapterCompletedList adapterCompletedList;
    String userId = "", userType = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.tab3, container, false);

        orderCompletedRecyclerView = view.findViewById(R.id.orderCompleteRecyclerView);
        RecyclerView.LayoutManager popularLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        orderCompletedRecyclerView.setLayoutManager(popularLayoutManager);
        orderCompletedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");
        userType = sharedPreferences.getString("userType", "");
        Log.e("dasda","sdsadsaix"+userId);


                completedListApi();

        return view;
    }

    private void completedListApi(){
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
        Call<OrderCompleteModel> call = service.getCompleteOrderlist("123",userId , userType);
        call.enqueue(new Callback<OrderCompleteModel>() {
            @Override
            public void onResponse(Call<OrderCompleteModel> call, Response<OrderCompleteModel> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful()) {
                        OrderCompleteModel result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();
                        Log.e("sdfsad","sfdsadx"+success);
                        if (success.equalsIgnoreCase("true")) {
                            completeListResultList = result.getCompletelistresponse();
                            adapterCompletedList = new AdapterCompletedList(getActivity(),completeListResultList);
                            orderCompletedRecyclerView.setAdapter(adapterCompletedList);
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
            public void onFailure(Call<OrderCompleteModel> call, Throwable t) {
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