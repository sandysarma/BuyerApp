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
import com.buyer.buyerApp.Activity.ServiceListActivity;
import com.buyer.buyerApp.ModelResult.CatergoriesResult;
import com.buyer.buyerApp.ModelResult.ServicesResult;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.UrlClass;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {
    @NonNull
    Context context;
    List<CatergoriesResult> catergoriesResultList;

    public ServiceAdapter(@NonNull Context context, List<CatergoriesResult> catergoriesResultList) {
        this.context = context;
        this.catergoriesResultList = catergoriesResultList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_adapter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.serviceNameTxt.setText(catergoriesResultList.get(position).getName());

        String strImage = catergoriesResultList.get(position).getImage();
        //String image_url = UrlClass.categoryImageUrl + strImage;
        String image_url = com.buyer.buyerApp.BuildConfig.API_URL+ UrlClass.categoryImageUrl + strImage;

        Glide.with(context).load(image_url)
                .thumbnail(0.5f)
                .into(holder.serviceImg);

        holder.itemView.setOnClickListener(v ->
        {
            String strCatigoryId = String.valueOf(catergoriesResultList.get(position).getId());
            //Intent intent = new Intent(context, ServicesDetailsActivity.class);
            Intent intent = new Intent(context, ServiceListActivity.class);
            intent.putExtra("strCatigoryId",strCatigoryId);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return catergoriesResultList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView serviceImg;
        TextView serviceNameTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceImg = itemView.findViewById(R.id.service_image);
            serviceNameTxt = itemView.findViewById(R.id.text_service);
        }
    }
}
