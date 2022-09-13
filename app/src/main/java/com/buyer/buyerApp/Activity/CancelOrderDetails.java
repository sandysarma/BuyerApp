package com.buyer.buyerApp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.buyer.buyerApp.ModelResult.CancelListResult;
import com.buyer.buyerApp.ModelResult.CancelList_Response;
import com.buyer.buyerApp.R;

public class CancelOrderDetails extends AppCompatActivity {

    ImageView back_arrow_button;
    CancelList_Response cancelListResult ;
    ImageView productimage;
    ImageView back_btn;
    TextView address,subtotal,paymentmode,mobileno,name,date,zipcode,ordername,productprice,orderno,totalamount,quantity,cancelled_by,reason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_order_details);
        cancelListResult= (CancelList_Response) getIntent().getSerializableExtra("orderlist1");
        Log.e("fsdfdsf","trgregfsd"+cancelListResult.getName());

        productimage=findViewById(R.id.imagePurchase1);
        subtotal=findViewById(R.id.subtotal_right1);

        back_btn=findViewById(R.id.image_payment_btn);
        mobileno=findViewById(R.id.phnno_right1);
        name=findViewById(R.id.txt_name_detail1);
        date=findViewById(R.id.detail_order_date1);
        zipcode=findViewById(R.id.pincode_right1);
        ordername=findViewById(R.id.product_name_detail1);
        productprice=findViewById(R.id.prod_price_det1);
        totalamount=findViewById(R.id.total_amount_right);
        orderno=findViewById(R.id.detal_order1);
        quantity=findViewById(R.id.return_reason_right1);
        address=findViewById(R.id.address_detail1);
        cancelled_by=findViewById(R.id.order_cancel_user);
        reason=findViewById(R.id.reason_cancel_order);
        int qty= Integer.parseInt(String.valueOf(cancelListResult.getQuantity()));
        int price= Integer.parseInt(String.valueOf(cancelListResult.getProductPrice()));
        int totalprice=(qty*price);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Glide.with(CancelOrderDetails.this).load(cancelListResult.getProductImage())
                .thumbnail(0.5f)
                .into(productimage);


        subtotal.setText(""+ "$"+totalprice);
        mobileno.setText(""+cancelListResult.getMoblie());
        name.setText(cancelListResult.getName());
        date.setText(""+cancelListResult.getCreatedAt());

        zipcode.setText(""+cancelListResult.getZipcode());
        ordername.setText(cancelListResult.getProductName());
        productprice.setText(""+ "$"+cancelListResult.getProductPrice());
        orderno.setText(""+cancelListResult.getOrderNumber());
        totalamount.setText("" + "$"+totalprice);
        quantity.setText(""+cancelListResult.getQuantity());
        address.setText(cancelListResult.getAddress());
        cancelled_by.setText(cancelListResult.getName());
        reason.setText(cancelListResult.getResion());
    }
}