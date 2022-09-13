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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.buyer.buyerApp.Adapter.AdapterCancelOrderList;

import com.buyer.buyerApp.Class.InterfaceCancelListItem;
import com.buyer.buyerApp.Model.CancelListModel;
import com.buyer.buyerApp.ModelResult.CancelListResult;

import com.buyer.buyerApp.ModelResult.CancelList_Response;
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

public class CancelListFragment extends Fragment implements InterfaceCancelListItem {
    List<CancelList_Response> orderCancelResponseList = new ArrayList<>();
    RecyclerView orderCancelRecyclerView;
    AdapterCancelOrderList adapterCancelOrderList;
    String userId = "", userType = "";
    InterfaceCancelListItem interfaceCancelListItem;
    CancelList_Response cancelListResult;
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.tab2, container, false);
        orderCancelRecyclerView = view.findViewById(R.id.orderCancelRecyclerView);
        RecyclerView.LayoutManager popularLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        orderCancelRecyclerView.setLayoutManager(popularLayoutManager);
        orderCancelRecyclerView.setItemAnimator(new DefaultItemAnimator());

        interfaceCancelListItem=CancelListFragment.this;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");
        userType = sharedPreferences.getString("userType", "");
         CancelList_Api();
        return view;
    }

    private void CancelList_Api() {
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
        Call<CancelListModel> call = service.getCancelList("123",userId, userType);
         call.enqueue(new Callback<CancelListModel>() {
             @Override
             public void onResponse(Call<CancelListModel> call, Response<CancelListModel> response) {
                 Log.e("dfdsfadfs","retsfvsdfd"+userId  +"     "+userType);
                 progressDialog.dismiss();
                 try {
                     if (response.isSuccessful()) {
                         CancelListModel result = response.body();
                         String success = result.getSuccess();
                         String message = result.getMessage();
                         float finalTotalPrice = 0, totalPrice = 0;

                         if (success.equalsIgnoreCase("true")) {
                             orderCancelResponseList = result.getCancelListResults();
                            // Log.e("dfdsfavgi","retsfvszxxgoi"+orderCancelResponseList.get(1).getProductImage());
                             adapterCancelOrderList = new AdapterCancelOrderList(getActivity(),orderCancelResponseList,interfaceCancelListItem);
                             orderCancelRecyclerView.setAdapter(adapterCancelOrderList);
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
             public void onFailure(Call<CancelListModel> call, Throwable t) {
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



    private void cancelListItemApi(CancelList_Response cancelList_response) {

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
        Call<CancelList_Response> call = service.getCancellist_item("123",""+cancelList_response.getUserId(),""+cancelList_response.getUserType(),""+cancelList_response.getId() );
        call.enqueue(new Callback<CancelList_Response>() {
            @Override
            public void onResponse(Call<CancelList_Response> call, Response<CancelList_Response> response) {
                progressDialog.dismiss();
                // Toast.makeText(getActivity(), "Cancel this order", Toast.LENGTH_SHORT).show();
                try {
                    if (response.isSuccessful()) {
                        CancelList_Response result = response.body();
                        removeItem(cancelList_response);
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
            public void onFailure(Call<CancelList_Response> call, Throwable t) {
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


    private void removeItem(CancelList_Response cancelListResult) {
        orderCancelResponseList.remove(cancelListResult);
        adapterCancelOrderList.notifyDataSetChanged();
    }



    @Override
    public void onItemClick(CancelList_Response cancelList_response) {
        cancelListItemApi(cancelList_response);
    }
}

