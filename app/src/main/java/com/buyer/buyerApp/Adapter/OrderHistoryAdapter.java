package com.buyer.buyerApp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.buyer.buyerApp.Activity.OrderHistoryDetails;

import com.buyer.buyerApp.Class.InterfaceCancelItemListener;
import com.buyer.buyerApp.MainActivity;
import com.buyer.buyerApp.ModelResult.OrderList_Response;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.Util.SharedPrefClass;

import java.io.Serializable;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    Context context;
    List<OrderList_Response> orderListResponseList;
    SharedPrefClass sharedPrefClass;
    View view = null;
    AlertDialog dialogs;
    InterfaceCancelItemListener interfaceCancelItemListener;
    public OrderHistoryAdapter(Context context, List<OrderList_Response> orderListResponseList, InterfaceCancelItemListener interfaceCancelItemListener) {
        this.context = context;
        this.orderListResponseList=orderListResponseList;
        this.interfaceCancelItemListener=interfaceCancelItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderlist_frament, parent, false);
        sharedPrefClass=new SharedPrefClass(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {



        Glide.with(holder.addressImage.getContext()).load(orderListResponseList.get(position).getProductImage()).thumbnail(0.5f)
                .into(holder.addressImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderHistoryDetails.class);
            intent.putExtra("orderlist", (Serializable) orderListResponseList.get(position));
            context.startActivity(intent);
        });

        if(orderListResponseList.get(position).getStatus().equals(0)){
            holder.orderStatusTxt.setText("Order Placed");
        }else if(orderListResponseList.get(position).getStatus().equals(1)){
            holder.orderStatusTxt.setText("Order Complete");
        }else if(orderListResponseList.get(position).getStatus().equals(2)){
            holder.orderStatusTxt.setText("Order Cancelled");
        }

        holder.orderDateTxt.setText(""+orderListResponseList.get(position).getCreatedAt());
        holder.productname.setText(orderListResponseList.get(position).getProductName());
        holder.productprice.setText(""+ "$"+orderListResponseList.get(position).getProductPrice());
        holder.delete.setOnClickListener(v -> {
            String id = String.valueOf(orderListResponseList.get(position).getId());
            RemoveItem_AlertDialog(position, holder,id);
        });
    }


    private void RemoveItem_AlertDialog(int position, ViewHolder holder, String id) {
        MainActivity mainActivity = (MainActivity) view.getContext();
        final LayoutInflater inflater = mainActivity.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.cancel_order_layout, null);
        final ImageView btncancel = alertLayout.findViewById(R.id.cancelImg);
        final EditText  edt_reason = alertLayout.findViewById(R.id.writeReasonEdt);
        final TextView submit=alertLayout.findViewById(R.id.submitBtnLayout);
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setView(alertLayout);
        alert.setCancelable(false);
        // alertLayout.setMinimumHeight(700);
        btncancel.setOnClickListener(v -> {
            dialogs.dismiss();
            holder.delete.setEnabled(true);
        });

        submit.setOnClickListener(v -> {
            dialogs.dismiss();
            String txt_rsn=edt_reason.getText().toString();
            holder.delete.setEnabled(true);
            if(txt_rsn!=null && txt_rsn.trim().length()==0) {
                Toast.makeText(context, "Please enter valid reason", Toast.LENGTH_SHORT).show();
                dialogs.show();
            }else {
                interfaceCancelItemListener.onItemClick(orderListResponseList.get(position),txt_rsn);
                dialogs.dismiss();
            }

        });

        dialogs = alert.create();
        dialogs.show();

    }


    @Override
    public int getItemCount() {
        return orderListResponseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderNumberTxt, orderDateTxt, orderStatusTxt, addressTxt, productprice,productname,mobileno,paymentmode,zipcode;
        ImageView addressImage;
        LinearLayout delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            orderDateTxt = itemView.findViewById(R.id.text_date);
            productprice = itemView.findViewById(R.id.text_price);
            addressImage = itemView.findViewById(R.id.purchaseImage);
            productname = itemView.findViewById(R.id.text_title);
            delete=itemView.findViewById(R.id.rel_delete);
            orderStatusTxt=itemView.findViewById(R.id.statustxt);

        }
    }
}
