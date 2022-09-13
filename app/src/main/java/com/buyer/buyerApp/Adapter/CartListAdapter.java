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

import com.bumptech.glide.Glide;
import com.buyer.buyerApp.Activity.CartListActivity;
import com.buyer.buyerApp.BuildConfig;
import com.buyer.buyerApp.Class.InterfacrItemClickListener;
import com.buyer.buyerApp.Class.RefreshInterface;
import com.buyer.buyerApp.Model.CommonModel;
import com.buyer.buyerApp.ModelResult.CartListResponse;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.ApiClient;
import com.buyer.buyerApp.RetrofitApi.ApiServices;
import com.buyer.buyerApp.RetrofitApi.UrlClass;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class CartListAdapter  extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    Context context;
    List<CartListResponse> mcartListResponseList=new ArrayList<>();
    RefreshInterface refreshInterface;
    View view = null;
    AlertDialog dialogs;
    int count = 0;
    private float qt = 0, prc = 0, totalPrice = 0, finalTotalPrice = 0;
    String userType="",userId="";
    // IncrementOnItemClick incrementOnItemClick ;
    InterfacrItemClickListener interfacrItemClickListener;

    public CartListAdapter(Context context, RefreshInterface refreshInterface, InterfacrItemClickListener interfacrItemClickListener) {
        this.context = context;
        //  this.cartListResponseList = cartListResponseList;
        this.refreshInterface = refreshInterface;
        this.interfacrItemClickListener=interfacrItemClickListener;
    }
    public void setListener(InterfacrItemClickListener interfacrItemClickListener){
        this.interfacrItemClickListener=interfacrItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_adapter, parent, false);
        return new CartListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");
        userType = sharedPreferences.getString("userType", "");

        String productImage = mcartListResponseList.get(position).getProduct_image();
        String image_url = BuildConfig.API_URL + UrlClass.productServiceImageUrl + productImage;
        Glide.with(context).load(image_url)
                .thumbnail(0.5f)
                .into(holder.img_product);


        holder.serviceNameTxt.setText(mcartListResponseList.get(position).getProductName());
        holder.priceTxt.setText("Price :  $" + mcartListResponseList.get(position).getProductPrice());
        holder.totalPriceTxt.setText("Total :  $" + mcartListResponseList.get(position).getTotalPrice());
        holder.quantityTxt.setText(mcartListResponseList.get(position).getQuantity());

        holder.incrementProductQuantityCart.setOnClickListener(v -> {

            count = Integer.parseInt(String.valueOf(holder.quantityTxt.getText()));
            count++;

            holder.quantityTxt.setText(String.valueOf(count));

            interfacrItemClickListener.onItemClickChange(count,mcartListResponseList.get(position),position);
        });

        holder.decrementProductQuantityCart.setOnClickListener(v -> {


            if (count > 1) {
                count--;
                holder.quantityTxt.setText(String.valueOf(count));
                interfacrItemClickListener.onDecrementClick(count, mcartListResponseList.get(position),position);

            }
        });

        holder.removeItemTxtBtn.setOnClickListener(v -> {
            String id = String.valueOf(mcartListResponseList.get(position).getId());
            interfacrItemClickListener.onItemClick(id,mcartListResponseList.get(position),position);
            // RemoveItem_AlertDialog(position, holder,id);
            holder.removeItemTxtBtn.setEnabled(false);
        });

    }

    public void RemoveItem_AlertDialog(int position, CartListAdapter.ViewHolder holder,String id) {
        CartListActivity cartListActivity = (CartListActivity) view.getContext();
        final LayoutInflater inflater = cartListActivity.getLayoutInflater();
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
            holder.removeItemTxtBtn.setEnabled(true);
        });

        btnDelete.setOnClickListener(v -> {
            // dialogs.dismiss();
            holder.removeItemTxtBtn.setEnabled(true);
            DeleteCartItem_Api(position,id);
        });

        dialogs = alert.create();
        dialogs.show();
    }

    public void removeItem(int position) {
        try {
            mcartListResponseList.remove(position);
            notifyDataSetChanged();
        } catch (IndexOutOfBoundsException index) {
            index.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DeleteCartItem_Api(int position, String id) {
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

        retrofit2.Call<CommonModel> call = service.getDeleteCartItem("123", userType, userId, id);
        //calling the api
        call.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(retrofit2.Call<CommonModel> call, retrofit2.Response<CommonModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        CommonModel result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();

                        if (success.equals("true")) {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            removeItem(position);
                            notifyDataSetChanged();
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

    @Override
    public int getItemCount() {
        return mcartListResponseList.size();
    }
    public void addItem(List<CartListResponse> cartListResponseList) {
        mcartListResponseList.clear();
        this.mcartListResponseList=cartListResponseList;
        notifyDataSetChanged();
    }

    public void clearList(){
        mcartListResponseList.clear();
        notifyDataSetChanged();
    }

    public void updateIncrement(int position, CartListResponse cartListResponse) {
        mcartListResponseList.set(position,cartListResponse);
        this.notifyItemChanged(position);
    }

    public void updateDerement(int position, CartListResponse cartListResponse) {
        mcartListResponseList.set(position,cartListResponse);
        this.notifyDataSetChanged();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_product, incrementProductQuantityCart, decrementProductQuantityCart;
        TextView serviceNameTxt, priceTxt, totalPriceTxt, quantityTxt, removeItemTxtBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            img_product = itemView.findViewById(R.id.img_product);
            serviceNameTxt = itemView.findViewById(R.id.serviceNameTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            totalPriceTxt = itemView.findViewById(R.id.totalPriceTxt);
            quantityTxt = itemView.findViewById(R.id.quantityTxt);
            removeItemTxtBtn = itemView.findViewById(R.id.removeItemTxtBtn);
            incrementProductQuantityCart = itemView.findViewById(R.id.incrementProductQuantityCart);
            decrementProductQuantityCart = itemView.findViewById(R.id.decrementProductQuantityCart);
        }
    }
}
