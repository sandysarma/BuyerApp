package com.buyer.buyerApp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.buyer.buyerApp.Activity.ProductListActivity;
import com.buyer.buyerApp.BuildConfig;
import com.buyer.buyerApp.ModelResult.ProductCategoryResult;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.UrlClass;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder>
{
    Context context;
    List<ProductCategoryResult> productCategoryResultList;

    public ProductsAdapter(Context context, List<ProductCategoryResult> productCategoryResultList) {
        this.context = context;
        this.productCategoryResultList = productCategoryResultList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_adapter,parent,false);

        return new ProductsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.text_service.setText(productCategoryResultList.get(position).getName());

        String strImage = productCategoryResultList.get(position).getImage();
        String image_url = BuildConfig.API_URL+ UrlClass.productImageUrl + strImage;
        Glide.with(context).load(image_url)
                .thumbnail(0.5f)
                .into(holder.service_image);

        holder.itemView.setOnClickListener(v -> {
            String strCatigoryId = String.valueOf(productCategoryResultList.get(position).getId());
            Intent intent = new Intent(context, ProductListActivity.class);
            intent.putExtra("strCatigoryId",strCatigoryId);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return  productCategoryResultList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView text_service;
        ImageView service_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_service = itemView.findViewById(R.id.text_service);
            service_image = itemView.findViewById(R.id.service_image);
        }
    }
}
