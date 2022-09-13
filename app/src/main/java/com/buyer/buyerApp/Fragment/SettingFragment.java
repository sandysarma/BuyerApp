package com.buyer.buyerApp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.buyer.buyerApp.Activity.ChangePasswordActivity;
import com.buyer.buyerApp.Activity.ProfileActivity;
import com.buyer.buyerApp.R;


public class SettingFragment extends Fragment {

RelativeLayout relativeMyProfile,relativeChangePassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view  =inflater.inflate(R.layout.fragment_setting, container, false);

       relativeMyProfile = view.findViewById(R.id.relativeMyProfile);
       relativeChangePassword = view.findViewById(R.id.relativeChangePassword);

       relativeMyProfile.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent  intent = new Intent(getActivity(), ProfileActivity.class);
               startActivity(intent);
           }
       });


       relativeChangePassword.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent changePassIntent = new Intent(getActivity(), ChangePasswordActivity.class);
               startActivity(changePassIntent);
           }
       });
        return view;
    }
}