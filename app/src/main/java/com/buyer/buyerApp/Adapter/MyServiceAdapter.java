package com.buyer.buyerApp.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.buyer.buyerApp.Activity.MyServiceDeatailsActivity;
import com.buyer.buyerApp.BuildConfig;
import com.buyer.buyerApp.Class.RefreshInterface;
import com.buyer.buyerApp.MainActivity;
import com.buyer.buyerApp.Model.CancelServicerequetModel;
import com.buyer.buyerApp.Model.DeleteMyServiceItemModel;
import com.buyer.buyerApp.ModelResult.ServiceRequestListResponse;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.ApiClient;
import com.buyer.buyerApp.RetrofitApi.ApiServices;
import com.buyer.buyerApp.RetrofitApi.UrlClass;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class MyServiceAdapter extends RecyclerView.Adapter<MyServiceAdapter.ViewHolder> {
    @NonNull
    Context context;

    List<ServiceRequestListResponse> serviceRequestListResponseList;
    View view = null;
    AlertDialog dialogs;
    RefreshInterface refreshInterface;
    String userId = "", userType = "", reason = "";
    String value = "";


    public MyServiceAdapter(Context context, List<ServiceRequestListResponse> serviceRequestListResponseList, String value) {
        this.context = context;
        this.serviceRequestListResponseList = serviceRequestListResponseList;
        this.value = value;
    }

    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myservice_adapter, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");
        userType = sharedPreferences.getString("userType", "");

        holder.serviceNameTxt.setText(serviceRequestListResponseList.get(position).getServiceName());
        holder.serviceDateTxt.setText(serviceRequestListResponseList.get(position).getStartDate());
        holder.servicePriceTxt.setText("$"+  serviceRequestListResponseList.get(position).getTotalAmount());
        holder.numberOfDaysTxt.setText(serviceRequestListResponseList.get(position).getNumberOfDays() + " Days");

        String strImage = serviceRequestListResponseList.get(position).getServiceImage();
        String image_url = BuildConfig.API_URL + UrlClass.categoryServiceImageUrl + strImage;
        Glide.with(context).load(image_url)
                .thumbnail(0.5f)
                .into(holder.serviceImg);

        String strStatus = String.valueOf(serviceRequestListResponseList.get(position).getStatus());


        if (value.equals("")) {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            Log.e("MySer","");
        } else {
            if (strStatus.equals(value)) {
                holder.itemView.setVisibility(View.VISIBLE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            } else {
                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }

        if (strStatus.equals("0")) {
            holder.statusTxt.setText("Pending");
            holder.cancelserviceRequestBtn.setVisibility(View.VISIBLE);
            holder.deleteMyServiceItem.setVisibility(View.GONE);
            holder.statusTxt.setTextColor(context.getResources().getColor(R.color.red));
        }

        else if (strStatus.equals("1")) {
            holder.statusTxt.setText("Completed");
            holder.cancelserviceRequestBtn.setVisibility(View.GONE);
            holder.deleteMyServiceItem.setVisibility(View.VISIBLE);
            holder.statusTxt.setTextColor(context.getResources().getColor(R.color.appColorGreen));
        }
        else if (strStatus.equals("2")) {
            holder.statusTxt.setText("Rejected");
            holder.cancelserviceRequestBtn.setVisibility(View.GONE);
            holder.deleteMyServiceItem.setVisibility(View.VISIBLE);
            holder.statusTxt.setTextColor(context.getResources().getColor(R.color.red));
        }
        else if (strStatus.equals("3")) {
            holder.statusTxt.setText("Inprocess");
            holder.cancelserviceRequestBtn.setVisibility(View.VISIBLE);
            holder.deleteMyServiceItem.setVisibility(View.GONE);
            holder.statusTxt.setTextColor(context.getResources().getColor(R.color.colorYellow));
        }

        SimpleDateFormat df_input = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss", Locale.UK);
        SimpleDateFormat df_output = new SimpleDateFormat("EEEE, dd MMM yyyy hh:mm:ss", Locale.UK);

        try {
            Date date = df_input.parse(serviceRequestListResponseList.get(position).getStartDate());
            String datefinal = df_output.format(date);
            holder.serviceDateTxt.setText(datefinal);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("formattedDateFromString", "Exception in formateDateFromstring(): " + e.getMessage());
        }


        holder.cardview_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = String.valueOf(serviceRequestListResponseList.get(position).getId());
                String serviceName1 = serviceRequestListResponseList.get(position).getServiceName();
                String land_size = serviceRequestListResponseList.get(position).getLandSize();
                String land_type = serviceRequestListResponseList.get(position).getLandType();
                String location = serviceRequestListResponseList.get(position).getLocation();
                String strStartDate = serviceRequestListResponseList.get(position).getStartDate();
                String number_of_days = serviceRequestListResponseList.get(position).getNumberOfDays() + "Days";
                String strSubTotal = ("$"+  serviceRequestListResponseList.get(position).getTotalAmount());
                String status = String.valueOf(serviceRequestListResponseList.get(position).getStatus());
                String category_name = serviceRequestListResponseList.get(position).getCategoryName();
                String strImage = serviceRequestListResponseList.get(position).getServiceImage();
                String description=serviceRequestListResponseList.get(position).getDescription();
                String bookingNumber = serviceRequestListResponseList.get(position).getRequestNumber();
                String bookingTime = serviceRequestListResponseList.get(position).getBookingtime();
                String phonenumber = serviceRequestListResponseList.get(position).getPhoneNumber();

                Intent intent = new Intent(context, MyServiceDeatailsActivity.class);
                intent.putExtra("serviceName", serviceName1);
                intent.putExtra("service_image", strImage);
                intent.putExtra("land_size", land_size);
                intent.putExtra("land_type", land_type);
                intent.putExtra("location", location);
                intent.putExtra("start_date", strStartDate);
                intent.putExtra("number_of_days", number_of_days);
                intent.putExtra("total_amount", strSubTotal);
                intent.putExtra("category_name", category_name);
                intent.putExtra("status", status);
                intent.putExtra("id", id);
                intent.putExtra("des",description);
                intent.putExtra("bookingno",bookingNumber);
                intent.putExtra("bookingTime",bookingTime);
                intent.putExtra("phonenumber",phonenumber);

                context.startActivity(intent);
            }
        });

        holder.deleteMyServiceItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = serviceRequestListResponseList.get(position).getRequestNumber();
                DeleteServiceItem_AlertDialog(position, holder,id);
            }
        });
        holder.cancelserviceRequestBtn.setOnClickListener(v -> {
            String requestNumber = serviceRequestListResponseList.get(position).getRequestNumber();
            CancelServiceRequest_AlertDialog(position, holder, requestNumber);
        });
    }


    private void DeleteServiceItem_AlertDialog(int position, MyServiceAdapter.ViewHolder holder,String requestNumber) {

        MainActivity mainActivity = (MainActivity) view.getContext();
        final LayoutInflater inflater = mainActivity.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.clear_all_cartlist_item, null);
        final TextView tvMessage = alertLayout.findViewById(R.id.textViewMessage);
        final TextView btnDelete = alertLayout.findViewById(R.id.btnd_delete);
        final TextView btncancel = alertLayout.findViewById(R.id.btn_cancel);

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(context.getString(R.string.Remove_myservices_request));
        tvMessage.setText(context.getString(R.string.Are_you_sure_remove_all_services));
        alert.setView(alertLayout);
        alert.setCancelable(false);

        btncancel.setOnClickListener(v -> {
            dialogs.dismiss();
        });

        btnDelete.setOnClickListener(v -> {
            dialogs.dismiss();
            DeleteMyService_Api( position, requestNumber);
        });

        dialogs = alert.create();
        dialogs.show();
    }

    private void DeleteMyService_Api(int position, String RequestNumber) {
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
        Call<DeleteMyServiceItemModel> call = service.getMyserviceDeleteItem("1234", userType,userId,RequestNumber);
        //calling the api
        call.enqueue(new Callback<DeleteMyServiceItemModel>() {
            @Override
            public void onResponse(Call<DeleteMyServiceItemModel> call, retrofit2.Response<DeleteMyServiceItemModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        DeleteMyServiceItemModel result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();

                        if (success.equals("true")) {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            deleteMyServiceItem(position);
                            notifyDataSetChanged();
                            refreshInterface.Refresh();
                            dialogs.dismiss();

                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

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
            public void onFailure(Call<DeleteMyServiceItemModel> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(context, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void deleteMyServiceItem(int position) {
        try {
            serviceRequestListResponseList.remove(position);
            notifyDataSetChanged();
        } catch (IndexOutOfBoundsException index) {
            index.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void CancelServiceRequest_AlertDialog(int position, ViewHolder holder, String id) {
        MainActivity mainActivity = (MainActivity) view.getContext();
        final LayoutInflater inflater = mainActivity.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_reason, null);
        final LinearLayout cancelordertxt = alertLayout.findViewById(R.id.cancelordertxt);
        final EditText textResionMessage = alertLayout.findViewById(R.id.textResionMessage);
        final ImageView crosstxt = alertLayout.findViewById(R.id.crosstxt);

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        crosstxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogs.dismiss();
            }
        });

        cancelordertxt.setOnClickListener(v -> {
            reason = textResionMessage.getText().toString();
            if (reason.equals("")){
                Toast toast = Toast.makeText(context, ("Please enter reason"), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }else {
                // holder.cancelorderTxtBtn.setEna
                CancelServiceRequest_Api(position,id);
                dialogs.dismiss();
            }
        });
        dialogs = alert.create();
        dialogs.show();
    }

    private void CancelServiceRequest_Api(int position, String requestNumber) {
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

        Call<CancelServicerequetModel> call = service.getCancelserviceRequest("1234", userType,userId,reason,requestNumber);
        //calling the api
        call.enqueue(new Callback<CancelServicerequetModel>() {
            @Override
            public void onResponse(Call<CancelServicerequetModel> call, retrofit2.Response<CancelServicerequetModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        CancelServicerequetModel result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();

                        if (success.equals("true")) {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            cancelservicerequest(position);
                            notifyDataSetChanged();
                            refreshInterface.Refresh();
                            dialogs.dismiss();

                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

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
            public void onFailure(Call<CancelServicerequetModel> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(context, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void cancelservicerequest(int position) {
        try {
            serviceRequestListResponseList.remove(position);
            notifyDataSetChanged();
        } catch (IndexOutOfBoundsException index) {
            index.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }







    @Override
    public int getItemCount() {
        return serviceRequestListResponseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView serviceImg, deleteMyServiceItem;
        TextView serviceNameTxt, statusTxt, serviceDateTxt, servicePriceTxt, numberOfDaysTxt, cancelserviceRequestBtn;
         CardView cardview_layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            serviceImg = itemView.findViewById(R.id.serviceImg);

            deleteMyServiceItem = itemView.findViewById(R.id.deleteMyServiceItem);
            serviceNameTxt = itemView.findViewById(R.id.serviceNameTxt);
            statusTxt = itemView.findViewById(R.id.statusTxt);
            serviceDateTxt = itemView.findViewById(R.id.serviceDateTxt);
            servicePriceTxt = itemView.findViewById(R.id.servicePriceTxt);
            numberOfDaysTxt = itemView.findViewById(R.id.numberOfDaysTxt);
            cancelserviceRequestBtn = itemView.findViewById(R.id.cancelserviceRequestBtn);
            cardview_layout = itemView.findViewById(R.id.cardview_layout);
        }
    }
}
