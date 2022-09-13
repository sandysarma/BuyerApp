package com.buyer.buyerApp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.buyer.buyerApp.BuildConfig;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.UrlClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ServiceDetailsActivity extends AppCompatActivity {

    ImageView back_arrow;
    LinearLayout numberOfDaysLayout;

    TextView serviceBtnBook, startDateTxt, endDateTxt, numberOfDaysTxt, serviceNameTxt, priceTxt, descriptionTxt;
    ImageView startDateImg, endDateImg, vehicleDeatailImg;
    Date startdate = null, enddate = null;
    String strName , strPrice , strDescription , strCategoryId , strServiceId , strImage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        back_arrow = findViewById(R.id.back_arrow);
        serviceBtnBook = findViewById(R.id.serviceBtnBook);

        back_arrow.setOnClickListener(v -> finish());

        strName = getIntent().getStringExtra("strName");
        strPrice = getIntent().getStringExtra("strPrice");
        strDescription = getIntent().getStringExtra("strDescription");
        strCategoryId = getIntent().getStringExtra("strCategoryId");
        strServiceId = getIntent().getStringExtra("strServiceId");
        strImage = getIntent().getStringExtra("strImage");


        vehicleDeatailImg = findViewById(R.id.vehicleDeatailImg);
        startDateImg = findViewById(R.id.startDateImg);
        endDateImg = findViewById(R.id.endDateImg);
        startDateTxt = findViewById(R.id.startDateTxt);
        endDateTxt = findViewById(R.id.endDateTxt);
        numberOfDaysTxt = findViewById(R.id.numberOfDaysTxt);
        numberOfDaysLayout = findViewById(R.id.numberOfDaysLayout);
        serviceNameTxt = findViewById(R.id.serviceNameTxt);
        priceTxt = findViewById(R.id.priceTxt);
        descriptionTxt = findViewById(R.id.description);

        String image_url = BuildConfig.API_URL + UrlClass.categoryServiceImageUrl + strImage;
        Glide.with(ServiceDetailsActivity.this).load(image_url)
                .thumbnail(0.5f)
                .into(vehicleDeatailImg);

        serviceNameTxt.setText(strName);
        priceTxt.setText("$" + strPrice);
        descriptionTxt.setText(strDescription);

        serviceBtnBook.setOnClickListener(v -> {

            Intent intent = new Intent(ServiceDetailsActivity.this, ServiceRequestActivity.class);
            intent.putExtra("strCategoryId", strCategoryId);
            intent.putExtra("strServiceId", strServiceId);
            intent.putExtra("strPrice", strPrice);
            intent.putExtra("strName",strName);
            intent.putExtra("strImg",strImage);
            intent.putExtra("url","0");

            startActivity(intent);
        });

        startDateImg.setOnClickListener(v -> set_StartDate());

        endDateImg.setOnClickListener(v -> set_EndDate());
    }
    private void set_StartDate() {
        final Calendar calendar = Calendar.getInstance();
        int yy = 1997;
        int mm = 10;
        int dd = 20;

        DatePickerDialog datePicker = new DatePickerDialog(ServiceDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;
                if (dayOfMonth < 10 && month < 10) {
                    String date_picks = year + "-" + "0" + (monthOfYear + 1) + "-" + "0" + dayOfMonth;
                    setStartDate(date_picks);
                } else if (month < 10 && dayOfMonth > 10) {
                    String date_picks = year + "-" + "0" + (monthOfYear + 1) + "-" + dayOfMonth;
                    setStartDate(date_picks);
                } else if (dayOfMonth >= 10 && month >= 10) {
                    String date_picks = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    setStartDate(date_picks);
                } else if (dayOfMonth >= 10 && month <= 10) {
                    String date_picks = year + "-" + "0" + (monthOfYear + 1) + "-" + dayOfMonth;
                    setStartDate(date_picks);
                } else if (dayOfMonth < 10 && month >= 10) {
                    String date_picks = year + "-" + (monthOfYear + 1) + "-" + "0" + dayOfMonth;
                    setStartDate(date_picks);
                } else {
                    String date_picks = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    setStartDate(date_picks);
                }

            }
        }, yy, mm, dd);
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis());

        datePicker.show();
    }

    private void set_EndDate() {
        final Calendar calendar = Calendar.getInstance();
        int yy = 1997;
        int mm = 10;
        int dd = 20;

        DatePickerDialog datePicker = new DatePickerDialog(ServiceDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;
                if (dayOfMonth < 10 && month < 10) {
                    String date_picks = year + "-" + "0" + (monthOfYear + 1) + "-" + "0" + dayOfMonth;
                    setEndDate(date_picks);
                    Log.e("Dates", "  1");
                } else if (month < 10 && dayOfMonth > 10) {
                    String date_picks = year + "-" + "0" + (monthOfYear + 1) + "-" + dayOfMonth;
                    setEndDate(date_picks);
                    Log.e("Dates", "  2");
                } else if (dayOfMonth >= 10 && month >= 10) {
                    String date_picks = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    setEndDate(date_picks);
                    Log.e("Dates", "  3");
                } else if (dayOfMonth >= 10 && month <= 10) {
                    String date_picks = year + "-" + "0" + (monthOfYear + 1) + "-" + dayOfMonth;
                    setEndDate(date_picks);
                    Log.e("Dates", "  4");
                } else if (dayOfMonth < 10 && month >= 10) {
                    String date_picks = year + "-" + (monthOfYear + 1) + "-" + "0" + dayOfMonth;
                    setEndDate(date_picks);
                    Log.e("Dates", "  5");
                } else {
                    String date_picks = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    setEndDate(date_picks);
                    Log.e("Dates", "  6");
                }
            }
        }, yy, mm, dd);
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis());

        datePicker.show();
    }

    @SuppressLint("SimpleDateFormat")
    private void setStartDate(String date_picks) {
        SimpleDateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            startdate = startDateFormat.parse(date_picks);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        startDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strPickupDate = startDateFormat.format(startdate);
        startDateTxt.setText(strPickupDate);
        Methods();
    }

    @SuppressLint("SimpleDateFormat")
    private void setEndDate(String date_picks) {
        SimpleDateFormat endDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            enddate = endDateFormat.parse(date_picks);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //endDateFormat = new SimpleDateFormat("d MMM YYYY");
        endDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strPickupDate = endDateFormat.format(enddate);
        endDateTxt.setText(strPickupDate);
        Methods();
    }

    public void Methods() {
        if (enddate == null || startdate == null) {
            Log.e("Service", "false");
        } else {
            int retVal = startdate.compareTo(enddate);

            if (retVal == 0) {
                numberOfDaysLayout.setVisibility(View.GONE);
                Log.e("Service", "both dates are equal");
                Toast toast = Toast.makeText(ServiceDetailsActivity.this, getString(R.string.both_date), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (retVal < 0) {
                numberOfDaysLayout.setVisibility(View.VISIBLE);
                Log.e("Service", "startdate is lower then enddate");
                long diff = enddate.getTime() - startdate.getTime();
                long seconds = diff / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                long days = hours / 24;
                Log.e("Service", String.valueOf(days));
                numberOfDaysTxt.setText(String.valueOf(days));
            }
            else if (retVal > 0) {
                numberOfDaysLayout.setVisibility(View.GONE);
                Log.e("Service", "startdate is higher then enddate");
                Toast toast = Toast.makeText(ServiceDetailsActivity.this, getString(R.string.startdate_enddate), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }
}