package com.buyer.buyerApp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.buyer.buyerApp.Activity.VehicleDetailsActivity;
import com.buyer.buyerApp.Model.VehicleModel;
import com.buyer.buyerApp.ModelResult.VehicleListResult;
import com.buyer.buyerApp.R;

import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.ViewHolder>
{
    Context context;
    List<VehicleListResult>vehicleListResults;

    public VehicleAdapter(Context context, List<VehicleListResult> vehicleListResults) {
        this.context = context;
        this.vehicleListResults = vehicleListResults;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_adapter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String strImage = vehicleListResults.get(position).getImage();
//        String image_url = BuildConfig.API_URL+ UrlClass.serviceImageUrl + strImage;
        Glide.with(context).load(strImage)
                .thumbnail(0.5f)
                .into(holder.vehicleserviceImg);

        holder.card_view.setOnClickListener(v -> {
            Intent intent = new Intent(context, VehicleDetailsActivity.class);
            String vehicleDescription= vehicleListResults.get(position).getDescription();
            String vehicleImg = vehicleListResults.get(position).getImage();
            String vehicleNmae = vehicleListResults.get(position).getName();
            String vehiclePrice = vehicleListResults.get(position).getPrice();
            String getId= String.valueOf((vehicleListResults.get(position).getId()));
            intent.putExtra("vehicledescriptions",vehicleDescription);
            intent.putExtra("strImg",vehicleImg);
            intent.putExtra("strName",vehicleNmae);
            intent.putExtra("strPrice",vehiclePrice);
            intent.putExtra("getId",getId);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return vehicleListResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        CardView card_view;
        ImageView vehicleserviceImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card_view = itemView.findViewById(R.id.vehicle_cardview);
            vehicleserviceImg = itemView.findViewById(R.id.vehicleServiceImg);
        }
    }
}
