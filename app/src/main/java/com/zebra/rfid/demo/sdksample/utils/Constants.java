package com.zebra.rfid.demo.sdksample.utils;

import android.app.Notification;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.content.SharedPreferences;
import android.widget.TextView;


//@RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
public class Constants {

    public static String SERVER_IP ;
    public static String MAX_POWER;
    // private static String SERVER_URL;

    public static String SERVER_URL =  "http://"+SERVER_IP+":3000";

//    public String getServerURL() {
//        return "http://"+this.getIP()+":3000";
//    }
//
//    public String getIP() {
//        return SERVER_IP;
//    }
//
//    public void setIP(String SERVER_IP) {
//        Log.d("---ip->", SERVER_IP);
//        this.SERVER_IP = SERVER_IP;
//    }

    // routes
    public static final String ROUTE_SCANPINVIN = "scanpinvin";
   // public static final String ROUTE_CART = "cart";
}
