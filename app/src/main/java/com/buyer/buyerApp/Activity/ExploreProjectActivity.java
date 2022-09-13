package com.buyer.buyerApp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.buyer.buyerApp.BuildConfig;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.UrlClass;

public class ExploreProjectActivity extends AppCompatActivity {
    ImageView exploreImage;
    TextView occasionTxt,projectNameTxt,exploreStatus, priceTxt, descriptionTxt, startDateTxt, locationtxt, completeDateTxt;

    ImageView back_arrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_project);

        exploreImage = findViewById(R.id.exploreImage);
        occasionTxt =  findViewById(R.id.occasionTxt);
        projectNameTxt =  findViewById(R.id.projectNameTxt);
        exploreStatus =  findViewById(R.id.exploreStatus);
        priceTxt =  findViewById(R.id.priceTxt);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        startDateTxt = findViewById(R.id.startDateTxt);
        completeDateTxt = findViewById(R.id.completeDateTxt);
        locationtxt = findViewById(R.id.locationtxt);

        back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(v -> finish());


        String strImage=getIntent().getStringExtra("explore_image");

        String image_url = BuildConfig.API_URL+ UrlClass.projectImageUrl + strImage;

        Glide.with(ExploreProjectActivity.this).load(image_url)
                .thumbnail(0.5f)
                .into(exploreImage);

        String statustxt = getIntent().getStringExtra("status");
        if (statustxt.equals("0")) {
            exploreStatus.setText("Pending");
            exploreStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.red));
        } else if (statustxt.equals("1")) {
            exploreStatus.setText("Completed");
            exploreStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.appColorGreen));
        } else if (statustxt.equals("2")) {
            exploreStatus.setText("Rejected");
            exploreStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.red));
        } else if(statustxt.equals("3")){
            exploreStatus.setText("Inprocess");
            exploreStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.colorYellow));
        }


        String projeectName = getIntent().getStringExtra("exploreprojectName");
        String location = getIntent().getStringExtra("locationtxt");
        String StartDate=getIntent().getStringExtra("startDate");
        String completetDate=getIntent().getStringExtra("completeDate");
        String StrPrice=getIntent().getStringExtra("priceTxt");
        String StrDescription=getIntent().getStringExtra("descriptionTxt");
        projectNameTxt.setText(projeectName);
        startDateTxt.setText(StartDate);
        completeDateTxt.setText(completetDate);
        priceTxt.setText(StrPrice);
        descriptionTxt.setText(Html.fromHtml(StrDescription));
        locationtxt.setText(location);

    }
}