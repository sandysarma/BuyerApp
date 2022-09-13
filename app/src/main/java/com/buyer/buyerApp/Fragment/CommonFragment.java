package com.buyer.buyerApp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.buyer.buyerApp.R;


public class CommonFragment extends Fragment {
    String value = "", url = "";
    WebView webview_comman;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

     View view= inflater.inflate(R.layout.fragment_common, container, false);

        assert this.getArguments() != null;
        value = this.getArguments().getString("value");
        webview_comman = view.findViewById(R.id.webview_comman);

        webview_comman.getSettings().setJavaScriptEnabled(true);
        webview_comman.getSettings().setLoadsImagesAutomatically(true);
        webview_comman.getSettings().setBuiltInZoomControls(true);
        webview_comman.getSettings().setSupportZoom(true);

        if (value.equals("about")) {
            url = "https://mobileandwebsitedevelopment.com/SmartFarms/about_usapp";
        } else if (value.equals("privacy")) {
            url = "https://mobileandwebsitedevelopment.com/SmartFarms/privacy";
        } else if (value.equals("other")) {
            url = "http://mobileandwebsitedevelopment.com/SmartFarms/termsandcondition";
        }

        webview_comman.loadUrl(url);

        return view;
    }
}