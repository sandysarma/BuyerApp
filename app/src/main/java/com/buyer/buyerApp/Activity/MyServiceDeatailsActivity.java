package com.buyer.buyerApp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.UrlClass;

public class MyServiceDeatailsActivity extends AppCompatActivity {
    ImageView serviceImage,back_arrow;
    TextView bookingTimeTxt, bookingNumberTxt, status, serviceName, servicesPrice,phoneNumberTxt,
            servicesHiredNoOfDays, descriptionTxt,subTotalTxt,payableAmountTxt,startDateTxt,endDateTxt;
    String strNumberOfDay="", strServicePrice="", strServiceStatus = "", strServiceName="",strPhonenumber="",
            strSubTotal="",strPayableAmount="", strImage="", strStartDate,strDescription="",strBookingNumber="", strBookingTime="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_service_deatails);

        back_arrow = findViewById(R.id.back_arrow);
        bookingTimeTxt = findViewById(R.id.bookingTimeTxt);
        serviceImage = findViewById(R.id.imageSellerSaleOrderProductDetails);
        bookingNumberTxt = findViewById(R.id.bookingNumberTxt);
        status = findViewById(R.id.serviceStatus);
        serviceName = findViewById(R.id.serviceName);
        servicesPrice = findViewById(R.id.servicesPrice);
        phoneNumberTxt = findViewById(R.id.phoneNumberTxt);
        servicesHiredNoOfDays = findViewById(R.id.servicesHiredNoOfDays);
        startDateTxt = findViewById(R.id.startDateTxt);
        endDateTxt = findViewById(R.id.endDateTxt);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        payableAmountTxt = findViewById(R.id.payableAmountTxt);
        subTotalTxt = findViewById(R.id.subTotalTxt);

        back_arrow.setOnClickListener(v -> finish());

        strServiceName = getIntent().getStringExtra("serviceName");
//        Toast.makeText(MyServicesDetailsActivity.this, strServiceName, Toast.LENGTH_LONG).show();

        strNumberOfDay=getIntent().getStringExtra("number_of_days");
        strStartDate=getIntent().getStringExtra("start_date");
        strPayableAmount=getIntent().getStringExtra("total_amount");
        strSubTotal=getIntent().getStringExtra("total_amount");
        strServicePrice=getIntent().getStringExtra("total_amount");
        strPhonenumber=getIntent().getStringExtra("phonenumber");
        strImage=getIntent().getStringExtra("service_image");
        strDescription=getIntent().getStringExtra("des");
        strBookingNumber=getIntent().getStringExtra("bookingno");
        strBookingTime=getIntent().getStringExtra("bookingTime");
        strServiceStatus=getIntent().getStringExtra("status");


        if (strServiceStatus.equals("0")) {
            status.setText("Pending");
            status.setTextColor(getApplicationContext().getResources().getColor(R.color.red));
        } else if (strServiceStatus.equals("1")) {
            status.setText("Completed");
            status.setTextColor(getApplicationContext().getResources().getColor(R.color.appColorGreen));
        } else if (strServiceStatus.equals("2")) {
            status.setText("Rejected");
            status.setTextColor(getApplicationContext().getResources().getColor(R.color.red));
        }else if (strServiceStatus.equals("3")){
            status.setText("Inprogress");
            status.setTextColor(getApplicationContext().getResources().getColor(R.color.colorYellow));
        }

        String image_url = com.buyer.buyerApp.BuildConfig.API_URL + UrlClass.categoryServiceImageUrl + strImage;
        Glide.with(MyServiceDeatailsActivity.this).load(image_url)
                .thumbnail(0.5f)
                .into(serviceImage);

        serviceName.setText(strServiceName);
        startDateTxt.setText(strStartDate);
        servicesHiredNoOfDays.setText(strNumberOfDay);
        payableAmountTxt.setText(strPayableAmount);
        subTotalTxt.setText(strSubTotal);
        servicesPrice.setText(strServicePrice);
        phoneNumberTxt.setText(strPhonenumber);
        descriptionTxt.setText(strDescription);
        bookingNumberTxt.setText(strBookingNumber);
        bookingTimeTxt.setText(strBookingTime);
    }
}