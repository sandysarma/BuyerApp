package com.buyer.buyerApp.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.buyer.buyerApp.Class.RefreshInterface;
import com.buyer.buyerApp.MainActivity;
import com.buyer.buyerApp.Model.NotificationDeleteModel;
import com.buyer.buyerApp.ModelResult.NotificationListResponce;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.ApiClient;
import com.buyer.buyerApp.RetrofitApi.ApiServices;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.ViewHolder> {
    List<NotificationListResponce> notificationListResponceList;
    Context context;
    View view = null;
    AlertDialog dialogs;
    String userId="", userType="";
    RefreshInterface refreshInterface;


    public NotificationsListAdapter(Context context, List<NotificationListResponce> notificationListResponces) {
        this.context = context;
        this.notificationListResponceList=notificationListResponces;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");
        userType = sharedPreferences.getString("userType", "");

        holder.notificationTitleTxt.setText(notificationListResponceList.get(position).getTitle());
        holder.notificationContentTxt.setText(notificationListResponceList.get(position).getMessage());
        holder.notificationTimeTxt.setText(notificationListResponceList.get(position).getCreatedAt());
        holder.notificationTimeTxt.setText(notificationListResponceList.get(position).getCreatedAt());

        holder.notificationDelete.setOnClickListener(v -> {
            String id = String.valueOf(notificationListResponceList.get(position).getId());
            DeleteNotification_AlertDialog(position, holder,id);
            holder.notificationDelete.setEnabled(false);
        });
    }

    public void DeleteNotification_AlertDialog(int position, ViewHolder holder,String id)
    {
        MainActivity mainActivity = (MainActivity) view.getContext();
        final LayoutInflater inflater = mainActivity.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.clear_all_cartlist_item, null);
        final TextView tvMessage = alertLayout.findViewById(R.id.textViewMessage);
        final TextView btnDelete = alertLayout.findViewById(R.id.btnd_delete);
        final TextView btncancel = alertLayout.findViewById(R.id.btn_cancel);

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(context.getString(R.string.delete_notification));
        tvMessage.setText(context.getString(R.string.Are_you_sure_delete_all_notification));
        alert.setView(alertLayout);
        alert.setCancelable(false);

        btncancel.setOnClickListener(v -> {
            dialogs.dismiss();
            holder.notificationDelete.setEnabled(true);
        });

        btnDelete.setOnClickListener(v -> {
            dialogs.dismiss();
            holder.notificationDelete.setEnabled(true);
            DeleteNotification_Api(position,id);
        });

        dialogs = alert.create();
        dialogs.show();
    }

    public void deletenotification(int position) {
        try {
            notificationListResponceList.remove(position);
            notifyDataSetChanged();
        } catch (IndexOutOfBoundsException index) {
            index.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DeleteNotification_Api(int position, String id)
    {
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

        Call<NotificationDeleteModel> call = service.getDeleteNotificationItem("123",userType,userId,id);
        //calling the api
        call.enqueue(new Callback<NotificationDeleteModel>() {
            @Override
            public void onResponse(Call<NotificationDeleteModel> call, retrofit2.Response<NotificationDeleteModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        NotificationDeleteModel result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();

                        if (success.equals("true")) {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            deletenotification(position);
                            notifyDataSetChanged();
                            refreshInterface.Refresh();
                            dialogs.dismiss();

//                            Toast.makeText(context, "Task successful", Toast.LENGTH_SHORT).show();

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
            public void onFailure(Call<NotificationDeleteModel> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(context, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return notificationListResponceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView notificationDelete;
        TextView notificationTitleTxt, notificationContentTxt, notificationTimeTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationDelete = itemView.findViewById(R.id.notificationDelete);
            notificationTitleTxt = itemView.findViewById(R.id.notificationTitleTxt);
            notificationContentTxt = itemView.findViewById(R.id.notificationContentTxt);
            notificationTimeTxt = itemView.findViewById(R.id.notificationTimeTxt);

        }
    }
}
