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
import com.buyer.buyerApp.Activity.ServiceDetailsActivity;
import com.buyer.buyerApp.Activity.ServiceRequestActivity;
import com.buyer.buyerApp.BuildConfig;
import com.buyer.buyerApp.ModelResult.ServicesResult;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.UrlClass;

import java.util.List;

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ViewHolder> {

    @NonNull
    List<ServicesResult> servicesResultList;
    Context context;

    public ServiceListAdapter(@NonNull List<ServicesResult> servicesResultList, Context context) {
        this.servicesResultList = servicesResultList;
        this.context = context;
    }

    @Override
    public ServiceListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_list_adapter, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceListAdapter.ViewHolder holder, int position) {
        holder.sevicelistName.setText(servicesResultList.get(position).getName());
        holder.servicelistPrice.setText("$" + servicesResultList.get(position).getPrice());

        String strImage = servicesResultList.get(position).getImage();
        String image_url = BuildConfig.API_URL + UrlClass.categoryServiceImageUrl + strImage;
        Glide.with(context).load(image_url)
                .thumbnail(0.5f)
                .into(holder.servicelistImg);

        holder.itemView.setOnClickListener(v -> {
            String strName = servicesResultList.get(position).getName();
            String strPrice = servicesResultList.get(position).getPrice();
            String strDescription = servicesResultList.get(position).getDescription();
            String strCategoryId = servicesResultList.get(position).getCategoryId();
            String strImages = servicesResultList.get(position).getImage();
            String strServiceId = String.valueOf(servicesResultList.get(position).getId());
            Intent intent = new Intent(context, ServiceDetailsActivity.class);
            intent.putExtra("strName", strName);
            intent.putExtra("strPrice", strPrice);
            intent.putExtra("strDescription", strDescription);
            intent.putExtra("strCategoryId", strCategoryId);
            intent.putExtra("strServiceId", strServiceId);
            intent.putExtra("strImage", strImages);
            context.startActivity(intent);
        });

        holder.serviceBtnBook.setOnClickListener(v -> {
            String strCategoryId = servicesResultList.get(position).getCategoryId();
            String strServiceId = String.valueOf(servicesResultList.get(position).getId());
            String strPrice = servicesResultList.get(position).getPrice();
            String strImg = servicesResultList.get(position).getImage();
            String strName = servicesResultList.get(position).getName();

            Intent intent = new Intent(context, ServiceRequestActivity.class);
            intent.putExtra("strCategoryId", strCategoryId);
            intent.putExtra("strServiceId", strServiceId);
            intent.putExtra("strPrice", strPrice);
            intent.putExtra("strImg", strImg);
            intent.putExtra("strName", strName);
            intent.putExtra("url", "1");
            context.startActivity(intent);
//                                Log.e("strImg", strImg + " "  );

        });

    }

    @Override
    public int getItemCount()    {
        return servicesResultList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView servicelistImg;
        TextView sevicelistName, servicelistPrice, serviceBtnBook;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            servicelistImg = itemView.findViewById(R.id.servicelistImg);
            sevicelistName = itemView.findViewById(R.id.sevicelistName);
            servicelistPrice = itemView.findViewById(R.id.servicelistPrice);
            serviceBtnBook = itemView.findViewById(R.id.serviceBtnBook);
        }
    }
}
