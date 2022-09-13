package com.buyer.buyerApp.Activity;


import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buyer.buyerApp.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.buyer.buyerApp.Activity.LocationActivity.place;
public class ServiceRequestActivity extends AppCompatActivity {


    RelativeLayout total_amount_layout;
    ImageView back_arrow,startDateImg,locationImage;
    TextView proceedTocheckBtn, priceTxt;
    EditText landSizeTxt, landTyptTxt, numberOfdayTxt, locationRequest, startDateTxt,total_amount;

    String strLandSize = "", strLandType = "", strNumberOfDay = "", strLocation = "", strStartDate = "", strpriceDate = "",
            strCategoryId = "", strServiceId = "", userId = "", userType = "", strTotalAmount = "", strPrice = "", strImage="", strName="",url;
    Date startdate = null;



    List<Address> addresses;
    int index = -1;
    private final static int LOCATION=377;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_to_cart);


        back_arrow = findViewById(R.id.back_arrow);
        proceedTocheckBtn = findViewById(R.id.proceedTocheckBtn);
        landSizeTxt = findViewById(R.id.landSizeTxt);
        landTyptTxt = findViewById(R.id.landTyptTxt);
        numberOfdayTxt = findViewById(R.id.numberOfdayTxt);
        locationRequest = findViewById(R.id.locationRequest);
        startDateTxt = findViewById(R.id.startDateTxt);
        priceTxt = findViewById(R.id.priceTxt);
        startDateImg = findViewById(R.id.startDateImg);
        total_amount_layout = findViewById(R.id.total_amount_layout);
        total_amount = findViewById(R.id.total_amount);
        locationImage = findViewById(R.id.locationImage);

        SharedPreferences sharedPreferences = ServiceRequestActivity.this.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");
        userType = sharedPreferences.getString("userType", "");


        strCategoryId = getIntent().getStringExtra("strCategoryId");
        strServiceId = getIntent().getStringExtra("strServiceId");
        strPrice = getIntent().getStringExtra("strPrice");
        strImage =getIntent().getStringExtra("strImg");
        strName =getIntent().getStringExtra("strName");
        url =getIntent().getStringExtra("url");

        locationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index=-2;
                Intent intent = new Intent(ServiceRequestActivity.this, LocationActivity.class);
                startActivityForResult(intent,LOCATION);
                // startActivity(intent);

            }
        });


        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        priceTxt.setText("$"+ strPrice);
        startDateImg.setOnClickListener(v -> set_StartDate());
        proceedTocheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ServiceRequestActivity.this, "Payment soon", Toast.LENGTH_SHORT).show();

                strLandSize = landSizeTxt.getText().toString();
                strLandType = landTyptTxt.getText().toString();
                strNumberOfDay = numberOfdayTxt.getText().toString();
                strLocation = locationRequest.getText().toString();
                strStartDate = startDateTxt.getText().toString();
                strpriceDate = priceTxt.getText().toString();

                if (strLandSize.equals("")) {
                    Toast toast = Toast.makeText(ServiceRequestActivity.this, getString(R.string.pls_enter_landsize), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (strLandType.equals("")) {
                    Toast toast = Toast.makeText(ServiceRequestActivity.this, getString(R.string.pls_enter_landtype), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (strNumberOfDay.equals("")) {
                    Toast toast = Toast.makeText(ServiceRequestActivity.this, getString(R.string.pls_enter_number_of_day), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (strLocation.equals("")) {
                    Toast toast = Toast.makeText(ServiceRequestActivity.this, getString(R.string.pls_enter_location), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (strStartDate.equals("")) {
                    Toast toast = Toast.makeText(ServiceRequestActivity.this, getString(R.string.pls_select_start_date), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    Intent intent = new Intent(ServiceRequestActivity.this, ProceedToCheckActivity.class);
                    intent.putExtra("strPrice",strPrice);
                    intent.putExtra("strName",strName);
                    intent.putExtra("strImg",strImage);
                    intent.putExtra("strCategoryId",strCategoryId);
                    intent.putExtra("url",url);
                    intent.putExtra("totalAmount",strTotalAmount);
                    intent.putExtra("strServiceId",strServiceId);
                    intent.putExtra("landSize",landSizeTxt.getText().toString());
                    intent.putExtra("landType",landTyptTxt.getText().toString());
                    intent.putExtra("date",startDateTxt.getText().toString());
                    intent.putExtra("noOFDays",numberOfdayTxt.getText().toString());
                    intent.putExtra("location",locationRequest.getText().toString());

                    startActivity(intent);
                }
            }
        });
        numberOfdayTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String s1 = String.valueOf(s);
                if (s1.equals("")) {
                    try {
                    } catch (NumberFormatException e) {
                    }
                } else {
                    double strnumberOfDays = Double.parseDouble(s1);
                    Float fltprice = Float.valueOf(strPrice);
                    double doublPrice = (strnumberOfDays * fltprice);
                    DecimalFormat df = new DecimalFormat("#.##");
                    double doublfinalPrice = Double.parseDouble(df.format(doublPrice));

                    strTotalAmount = String.valueOf(doublfinalPrice);
                    total_amount_layout.setVisibility(View.VISIBLE);
                    total_amount.setText(strPrice + "x" + s1 + "days=" + doublfinalPrice);

                    Log.e("ServiceRequest", " amount  " + strTotalAmount);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }


    private void set_StartDate() {
        final Calendar calendar = Calendar.getInstance();
        int yy = 1997;
        int mm = 10;
        int dd = 20;

        DatePickerDialog datePicker = new DatePickerDialog(ServiceRequestActivity.this, new DatePickerDialog.OnDateSetListener() {
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

    @SuppressLint("SimpleDateFormat")
    private void setStartDate(String date_picks) {
        SimpleDateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            startdate = startDateFormat.parse(date_picks);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        startDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        strStartDate = startDateFormat.format(startdate);
        startDateTxt.setText(strStartDate);
        //Methods();
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(index==-2)
        {
                getLocationAddress(ServiceRequestActivity.this, place);
        }

    }


    public LatLng getLocationAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        LatLng p1 = null;

        if (strAddress == null) {
        } else {
            try {
                addresses = coder.getFromLocationName(strAddress, 5);
                if (addresses == null) {
                    return null;
                }

                Address location = addresses.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude());

                String addressLinePick = String.valueOf(location.getAddressLine(0));

                locationRequest.setText(addressLinePick);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return p1;
    }

}
