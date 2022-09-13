package com.buyer.buyerApp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.buyer.buyerApp.R;

public class VehicleDetailsActivity extends AppCompatActivity {

    ImageView back_arrow,vehicleDeatailImg;
    TextView vehicleName,vehiclePriceTxt,descriptionTxt,vehicleBtnBook;
    private String StrVehicleDescription;
    private String StrvehiclePrice;
    private String StrvehicleName;
    private String getId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);


        back_arrow = findViewById(R.id.back_arrow);
        vehicleDeatailImg = findViewById(R.id.vehicleDeatailImg);
        vehicleBtnBook = findViewById(R.id.vehicleBtnBook);
        vehicleName = findViewById(R.id.vehicleName);
        vehiclePriceTxt = findViewById(R.id.vehiclePriceTxt);
        descriptionTxt = findViewById(R.id.descriptionTxt);



        back_arrow.setOnClickListener(v -> finish());

        String strVehicleImg=getIntent().getStringExtra("strImg");

        Glide.with(VehicleDetailsActivity.this).load(strVehicleImg)
                .thumbnail(0.5f)
                .into(vehicleDeatailImg);

        StrVehicleDescription=getIntent().getStringExtra("vehicledescriptions");
        StrvehiclePrice=getIntent().getStringExtra("strPrice");
        StrvehicleName=getIntent().getStringExtra("strName");
        getId=getIntent().getStringExtra("getId");
        descriptionTxt.setText(StrVehicleDescription);
        vehiclePriceTxt.setText("$"+StrvehiclePrice);
        vehicleName.setText(StrvehicleName);


        vehicleBtnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(VehicleDetailsActivity.this, ServiceRequestActivity.class);
                intent.putExtra("strCategoryId", getId);
                intent.putExtra("strServiceId", getId);
                intent.putExtra("strImg", strVehicleImg);
                intent.putExtra("strName", StrvehicleName);
                intent.putExtra("strPrice", StrvehiclePrice);
                intent.putExtra("url", "0");
                startActivity(intent);
            }
        });
    }
}