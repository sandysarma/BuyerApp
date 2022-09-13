package com.buyer.buyerApp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.buyer.buyerApp.Activity.CompleteOrderDetails;
import com.buyer.buyerApp.BuildConfig;
import com.buyer.buyerApp.ModelResult.CompleteListResult;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.UrlClass;

import java.io.Serializable;
import java.util.List;

public class AdapterCompletedList extends RecyclerView.Adapter<AdapterCompletedList.VH> {
    Context context;
    List<CompleteListResult> orderCompleteResponseList;
    View view = null;

    public AdapterCompletedList(Context context, List<CompleteListResult> orderCompleteResponseList) {
        this.context = context;
        this.orderCompleteResponseList=orderCompleteResponseList;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adaptercompletedlist, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        String productImage = orderCompleteResponseList.get(position).getProductImage();
        String image_url = BuildConfig.API_URL + UrlClass.productServiceImageUrl + productImage;

        Glide.with(holder.addressImage.getContext()).load(orderCompleteResponseList.get(position).getProductImage()).thumbnail(0.5f)
                .into(holder.addressImage);
        Log.e("sdsdsda","sdsdsdvfgdtg"+orderCompleteResponseList.get(position).getProductImage());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CompleteOrderDetails.class);
            intent.putExtra("completelist", (Serializable) orderCompleteResponseList.get(position));
            context.startActivity(intent);
        });

        holder.totalAmountTxt.setText(""+orderCompleteResponseList.get(position).getTotalAmount());
        holder.orderDateTxt.setText(""+orderCompleteResponseList.get(position).getCreatedAt());
        holder.ordername.setText(orderCompleteResponseList.get(position).getProductName());
    }

    @Override
    public int getItemCount() {
        return orderCompleteResponseList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView orderNumberTxt, orderDateTxt, orderStatusTxt, addressTxt, totalAmountTxt,customername,mobileno,paymentmode,ordername;
        ImageView addressImage;
        Button order_cancel;
        public VH(@NonNull View itemView) {
            super(itemView);


            orderDateTxt = itemView.findViewById(R.id.text_date);
          //  orderStatusTxt = itemView.findViewById(R.id.orderStatusTxt);
            totalAmountTxt = itemView.findViewById(R.id.text_price);
            addressImage = itemView.findViewById(R.id.purchaseImage);
            ordername = itemView.findViewById(R.id.text_title);
        }
    }
}
