package com.buyer.buyerApp.Util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefClass {
    SharedPreferences.Editor  sharedPreferences;
    SharedPreferences prefs ;

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static final String NAME = "name";
    public static final String PHONENO = "phoneno";
    public static final String ZIPCODE = "zipcode";
    public static final String ADDRESS = "addresss";
    public static final String Qty = "qty";
    public static final String IMAGEURL = "imageurl";
    public static final String PRODUCTPRICE = "productprice";
    public static final String QUANTITYLIST = "quantitylist";
    public static final String PRODUCTIDLIST = "productidlist";

    public SharedPrefClass(Context context){
        sharedPreferences = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE).edit();

        prefs = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
    }

    public void putString(String key,String value){
        sharedPreferences.putString(key,value);
        sharedPreferences.apply();
    }

    public String getString(String key){
       String value = prefs.getString(key,"");

       return value;
    }

    public void putInt(String key,int value){
        sharedPreferences.putInt(key,value);
        sharedPreferences.apply();
    }

    /*public void putFloat(Float key, float value){
        sharedPreferences.putFloat(key,value);
        sharedPreferences.apply();
    }

    public Float getFloat(String key){
        float value=prefs.getFloat(key,0);
        return value;
    }*/

   /* public int getInt(int key){
        int value = prefs.getInt(key,0);

        return value;
    }*/

    public void putBoolean(String key,Boolean value){
        sharedPreferences.putBoolean(key,value);
        sharedPreferences.apply();
    }

    public Boolean getBoolean(String key){
        Boolean value = prefs.getBoolean(key,false);

        return value;
    }

}
