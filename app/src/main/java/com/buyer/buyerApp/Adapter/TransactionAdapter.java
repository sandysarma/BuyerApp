package com.buyer.buyerApp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.buyer.buyerApp.Activity.TransactionDetailsActivity;
import com.buyer.buyerApp.ModelResult.TransationRequestResult;
import com.buyer.buyerApp.R;


import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    Context context;
    List<TransationRequestResult> transactionRequestListResponseList;


    public TransactionAdapter(Context context, List<TransationRequestResult> transactionRequestListResponseList) {
        this.context = context;
        this.transactionRequestListResponseList = transactionRequestListResponseList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.requesttotalAmount.setText("Total $" + transactionRequestListResponseList.get(position).getTotalAmount());
        holder.requesttotalName.setText(transactionRequestListResponseList.get(position).getServiceName());
        holder.requestPhoneNumber.setText(transactionRequestListResponseList.get(position).getPhoneNumber());

        String strImage = transactionRequestListResponseList.get(position).getImage();
//        String image_url = com.farmer.smartfarms.BuildConfig.API_URL+ UrlClass.categoryImageUrl + strImage;
        Glide.with(context).load(strImage)
                .thumbnail(0.5f)
                .into(holder.transactioRequestImg);

        holder.transactionlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TransactionDetailsActivity.class);
                String strImage = transactionRequestListResponseList.get(position).getImage();
                String strServiceName = transactionRequestListResponseList.get(position).getServiceName();
                String strCreatedAt = transactionRequestListResponseList.get(position).getCreatedAt();
                String strTotalAmount = transactionRequestListResponseList.get(position).getTotalAmount();
                String strRequestNumber = transactionRequestListResponseList.get(position).getRequestNumber();
                String strStatus = String.valueOf(transactionRequestListResponseList.get(position).getStatus());
                String strPhoneNumber = transactionRequestListResponseList.get(position).getPhoneNumber();
                String strTransactionId = transactionRequestListResponseList.get(position).getTransactionId();

                intent.putExtra("strImage", strImage);
                intent.putExtra("strServiceName", strServiceName);
                intent.putExtra("strCreatedAt", strCreatedAt);
                intent.putExtra("strTotalAmount", strTotalAmount);
                intent.putExtra("strRequestNumber", strRequestNumber);
                intent.putExtra("strStatus", strStatus);
                intent.putExtra("strPhoneNumber", strPhoneNumber);
                intent.putExtra("strTransactionId", strTransactionId);

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return transactionRequestListResponseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView requesttotalAmount, requesttotalName, requestPhoneNumber, tranDetailsNumber, transDetailsName, tranDetailsStatus, transDetailsDate, transDetailsAmount;
        ImageView transactioRequestImg, transDetailsImg, transactionDelete;
        LinearLayout transactionlayout;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            requesttotalAmount = itemView.findViewById(R.id.requesttotalAmount);
            requesttotalName = itemView.findViewById(R.id.requesttotalName);
            requestPhoneNumber = itemView.findViewById(R.id.requestPhoneNumber);
            transactioRequestImg = itemView.findViewById(R.id.transactioRequestImg);
            transactionlayout = itemView.findViewById(R.id.transactionlayout);
            transDetailsImg = itemView.findViewById(R.id.transDetailsImg);
            transDetailsName = itemView.findViewById(R.id.transDetailsName);
            transDetailsDate = itemView.findViewById(R.id.transDetailsDate);
            transDetailsAmount = itemView.findViewById(R.id.transDetailsAmount);
            tranDetailsStatus = itemView.findViewById(R.id.tranDetailsStatus);
            tranDetailsNumber = itemView.findViewById(R.id.tranDetailsNumber);

        }
    }
}
