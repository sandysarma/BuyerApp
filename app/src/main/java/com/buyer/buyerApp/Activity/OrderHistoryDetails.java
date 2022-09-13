package com.buyer.buyerApp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.buyer.buyerApp.ModelResult.OrderList_Response;
import com.buyer.buyerApp.R;

public class OrderHistoryDetails extends AppCompatActivity {


    LinearLayout back_arrow_button;
    OrderList_Response orderListResponse ;
    ImageView productimage;
    ImageView back;
    TextView address,subtotal,paymentmode,mobileno,name,date,zipcode,ordername,productprice,orderno,totalamount,quantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_details);

        address = findViewById(R.id.address_detail);
        productimage = findViewById(R.id.imagePurchase);
        subtotal = findViewById(R.id.subtotal_right);

        mobileno = findViewById(R.id.phnno_right);
        name = findViewById(R.id.txt_name_detail);
        date = findViewById(R.id.detail_order_date);
        zipcode = findViewById(R.id.pincode_right);
        ordername = findViewById(R.id.product_name_detail);
        productprice = findViewById(R.id.prod_price_det);
        totalamount = findViewById(R.id.total_amount_right);
        quantity = findViewById(R.id.return_reason_right);
        back=findViewById(R.id.image_payment_btn);
        orderListResponse= (OrderList_Response) getIntent().getSerializableExtra("orderlist");
        orderno = findViewById(R.id.detal_order);
        int qty = Integer.parseInt(String.valueOf(orderListResponse.getQuantity()));
        int price = Integer.parseInt(String.valueOf(orderListResponse.getProductPrice()));
        int totalprice = (qty * price);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Glide.with(OrderHistoryDetails.this).load(orderListResponse.getProductImage())
                .thumbnail(0.5f)
                .into(productimage);

        address.setText(orderListResponse.getAddress());
        subtotal.setText(""+ "$"+totalprice);
        mobileno.setText(""+orderListResponse.getMoblie());
        name.setText(orderListResponse.getName());
        date.setText(""+orderListResponse.getCreatedAt());
        back_arrow_button = findViewById(R.id.back_arrow_button);
        zipcode.setText(""+orderListResponse.getZipcode());
        ordername.setText(orderListResponse.getProductName());
        productprice.setText(""+ "$"+orderListResponse.getProductPrice());
        orderno.setText(""+orderListResponse.getOrderNumber());
        totalamount.setText(""+ "$"+totalprice);
        quantity.setText(""+orderListResponse.getQuantity());
    }
}