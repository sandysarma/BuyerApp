package com.buyer.buyerApp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.buyer.buyerApp.Activity.CancelOrderDetails;
import com.buyer.buyerApp.Class.InterfaceCancelListItem;
import com.buyer.buyerApp.MainActivity;
import com.buyer.buyerApp.ModelResult.CancelListResult;
import com.buyer.buyerApp.ModelResult.CancelList_Response;
import com.buyer.buyerApp.R;

import java.util.List;

public class AdapterCancelOrderList extends RecyclerView.Adapter<AdapterCancelOrderList.VH> {
    Context context;
    List<CancelList_Response> orderCancelResponseList;
    View view = null;
    InterfaceCancelListItem interfaceCancelListItem;
    AlertDialog dialogs;

    public AdapterCancelOrderList(Context context, List<CancelList_Response> orderCancelResponseList, InterfaceCancelListItem interfaceCancelListItem) {
     this.context=context;
     this.orderCancelResponseList=orderCancelResponseList;
     this.interfaceCancelListItem=interfaceCancelListItem;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapterordercancel, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Glide.with(holder.addressImage.getContext()).load(orderCancelResponseList.get(position).getProductImage()).thumbnail(0.5f)
                .into(holder.addressImage);

        holder.itemView.setOnClickListener(v -> {
            Intent ii = new Intent(context, CancelOrderDetails.class);
            ii.putExtra("orderlist1", orderCancelResponseList.get(position));
            context.startActivity(ii);
        });

        holder.orderDateTxt.setText(""+orderCancelResponseList.get(position).getCreatedAt());
        holder.productname.setText(orderCancelResponseList.get(position).getProductName());
        holder.productprice.setText(""+ "$"+orderCancelResponseList.get(position).getProductPrice());
        if(orderCancelResponseList.get(position).getStatus().equals(2)){
            holder.orderstatus.setText("Cancelled");
        }
        Log.e("fadsfads","fsddrar"+orderCancelResponseList.get(position).getProductPrice());

        holder.delete_button.setOnClickListener(v -> {
            String id = String.valueOf(orderCancelResponseList.get(position).getId());
            RemoveItem_AlertDialog(position, holder,id);
        });

    }

    private void RemoveItem_AlertDialog(int position, VH holder, String id) {
        MainActivity mainActivity = (MainActivity) view.getContext();
        final LayoutInflater inflater = mainActivity.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.clear_all_cartlist_item, null);
        final TextView tvMessage = alertLayout.findViewById(R.id.textViewMessage);
        final TextView btnDelete = alertLayout.findViewById(R.id.btnd_delete);
        final TextView btncancel = alertLayout.findViewById(R.id.btn_cancel);
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(context.getString(R.string.removee_item));
        tvMessage.setText(context.getString(R.string.Are_you_sure_to_remove_this_order));
        alert.setView(alertLayout);
        alert.setCancelable(false);

        btncancel.setOnClickListener(v -> {
            dialogs.dismiss();
            holder.delete_button.setEnabled(true);
        });

        btnDelete.setOnClickListener(v -> {
            dialogs.dismiss();
            holder.delete_button.setEnabled(true);
            interfaceCancelListItem.onItemClick(orderCancelResponseList.get(position));
        });

        dialogs = alert.create();
        dialogs.show();
    }

    @Override
    public int getItemCount() {
        return orderCancelResponseList.size();
    }


    public class VH extends RecyclerView.ViewHolder {
        TextView  orderDateTxt, productprice, productname,mobileno,paymentmode,orderstatus;
        ImageView addressImage;
        RelativeLayout delete_button;
        public VH(@NonNull View itemView) {
            super(itemView);

            orderDateTxt = itemView.findViewById(R.id.text_date1);
            productprice = itemView.findViewById(R.id.text_price1);
            addressImage = itemView.findViewById(R.id.purchaseImage1);
            productname = itemView.findViewById(R.id.text_title1);
            delete_button=itemView.findViewById(R.id.rel_delete);
            orderstatus=itemView.findViewById(R.id.statustxt);
        }
    }
}
