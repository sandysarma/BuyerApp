package com.buyer.buyerApp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.buyer.buyerApp.ModelResult.CompleteListResult;
import com.buyer.buyerApp.R;

public class CompleteOrderDetails extends AppCompatActivity {
    LinearLayout back_arrow_button;
    CompleteListResult completeListResult;
    ImageView productimage;
    ImageView back;
    TextView address,subtotal,paymentmode,mobileno,name,date,zipcode,ordername,productprice,orderno,totalamount,quantity;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_order_details);

        address=findViewById(R.id.address_detail);
        productimage=findViewById(R.id.imagePurchase);
        subtotal=findViewById(R.id.subtotal_right);

        mobileno=findViewById(R.id.phnno_right);
        name=findViewById(R.id.txt_name_detail);
        date=findViewById(R.id.detail_order_date);
        zipcode=findViewById(R.id.pincode_right);
        ordername=findViewById(R.id.product_name_detail);
        productprice=findViewById(R.id.prod_price_det);
        totalamount=findViewById(R.id.total_amount_right);
        quantity=findViewById(R.id.return_reason_right);
        back=findViewById(R.id.image_payment_btn);
        completeListResult= (CompleteListResult) getIntent().getSerializableExtra("completelist");
        orderno=findViewById(R.id.detal_order);
        int qty= Integer.parseInt(String.valueOf(completeListResult.getQuantity()));
        int price= Integer.parseInt(String.valueOf(completeListResult.getProductPrice()));
        int totalprice=(qty*price);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Glide.with(CompleteOrderDetails.this).load(completeListResult.getProductImage())
                .thumbnail(0.5f)
                .into(productimage);
        address.setText(completeListResult.getAddress());
        subtotal.setText(""+ "$"+totalprice);
        mobileno.setText(""+completeListResult.getMoblie());
        name.setText(completeListResult.getName());
        date.setText(""+completeListResult.getCreatedAt());
        back_arrow_button = findViewById(R.id.back_arrow_button);
        zipcode.setText(""+completeListResult.getZipcode());
        ordername.setText(completeListResult.getProductName());
        productprice.setText(""+ "$"+completeListResult.getProductPrice());
        orderno.setText(""+completeListResult.getOrderNumber());
        totalamount.setText(""+ "$"+totalprice);
        quantity.setText(""+completeListResult.getQuantity());
    }
}