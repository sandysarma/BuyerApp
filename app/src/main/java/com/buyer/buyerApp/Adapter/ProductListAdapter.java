package com.buyer.buyerApp.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
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
import com.buyer.buyerApp.Activity.ProductDetailsActivity;
import com.buyer.buyerApp.BuildConfig;
import com.buyer.buyerApp.Model.CommonModel;
import com.buyer.buyerApp.ModelResult.ProductResult;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.ApiClient;
import com.buyer.buyerApp.RetrofitApi.ApiServices;
import com.buyer.buyerApp.RetrofitApi.UrlClass;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    Context context;
    String userId , userType ;
    List<ProductResult> productResponseList;
    public ProductListAdapter(Context context, List<ProductResult> productResponseList) {
        this.context = context;
        this.productResponseList = productResponseList;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_adapter, parent, false);

        return new ProductListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");
        userType = sharedPreferences.getString("userType", "");

        holder.prdtNameTxt.setText(productResponseList.get(position).getName());
        holder.productPriceTxt.setText("$" + productResponseList.get(position).getPrice());
        holder.productQty.setText(productResponseList.get(position).getRemaningQuantity());

        String strImage = productResponseList.get(position).getImage();
        String image_url = BuildConfig.API_URL + UrlClass.productServiceImageUrl + strImage;
        Glide.with(context).load(image_url)
                .thumbnail(0.5f)
                .into(holder.productListImg);

        holder.itemView.setOnClickListener(v -> {
            String strImages = productResponseList.get(position).getImage();
            String strName = productResponseList.get(position).getName();
            String strPrice = productResponseList.get(position).getPrice();
            String strQuantity = productResponseList.get(position).getQuantity();
            String strRemaningQuantity = productResponseList.get(position).getRemaningQuantity();
            String strDescription = productResponseList.get(position).getDescription();
            String strAddedToCart = String.valueOf(productResponseList.get(position).getAddedToCart());
            String strProductId = String.valueOf(productResponseList.get(position).getId());
            Intent intent = new Intent(context, ProductDetailsActivity.class);

            intent.putExtra("strImage", strImages);
            intent.putExtra("strName", strName);
            intent.putExtra("strPrice", strPrice);
            intent.putExtra("strQuantity", strQuantity);
            intent.putExtra("strRemaningQuantity", strRemaningQuantity);
            intent.putExtra("strDescription", strDescription);
            intent.putExtra("strAddedToCart", strAddedToCart);
            intent.putExtra("strProductId", strProductId);
            context.startActivity(intent);
        });

        holder.addCartBtn.setOnClickListener(v -> {
            String strAddedToCart = String.valueOf(productResponseList.get(position).getAddedToCart());
            String strProductId = String.valueOf(productResponseList.get(position).getId());
            String strPrice = productResponseList.get(position).getPrice();
            String strProductName = productResponseList.get(position).getName();
            String strRemaningQuantity = productResponseList.get(position).getRemaningQuantity();
            if (strAddedToCart.equals("0")) {
                AddToCart_Api(strProductId, "1", strPrice, strProductName, strRemaningQuantity);
            } else {
                Toast.makeText(context, "You have already add this item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AddToCart_Api(String strProductId, String strQuantity, String strPrice, String strProductName, String strRemaningQuantity) {
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
        retrofit2.Call<CommonModel> call = service.getAddToCart("123",
                userType, userId, strProductId, strQuantity, strPrice, strPrice, strProductName);
        call.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<CommonModel> call, @NonNull retrofit2.Response<CommonModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success = response.body().getSuccess();

                        if (success.equalsIgnoreCase("true")) {
                            //Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, CartListActivity.class);
                            intent.putExtra("strRemaningQuantity", strRemaningQuantity);
                            context.startActivity(intent);
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
            public void onFailure(@NonNull Call<CommonModel> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(context, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(context, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productResponseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productListImg;
        TextView prdtNameTxt, productPriceTxt, productQty, addCartBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productListImg = itemView.findViewById(R.id.productListImg);
            prdtNameTxt = itemView.findViewById(R.id.prdtNameTxt);
            productPriceTxt = itemView.findViewById(R.id.productPriceTxt);
            productQty = itemView.findViewById(R.id.productQty);
            addCartBtn = itemView.findViewById(R.id.addCartBtn);
        }
    }
}
