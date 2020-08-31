package com.zebra.rfid.demo.sdksample.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;


import com.zebra.rfid.demo.sdksample.MainActivity;

import java.util.Map;

public class Utils {

    private static String MAX_POWER;
    private static String URL;
    Context ctx;
    public Utils(Context ctx){
        this.ctx = ctx;
    }

    void onCreate(MainActivity activity) {

        @SuppressLint("WrongConstant") SharedPreferences sh = ctx.getSharedPreferences("MySharedPref",
                Context.MODE_APPEND);
        MAX_POWER = sh.getString("max_power", "");
        URL = sh.getString("server_ip", "");
        Log.d("Max Power -->", MAX_POWER);
        Log.d("URL Power -->", URL);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    public static String getUrl(String route) {
        String url = Constants.SERVER_URL + route;
        return url;
    }

}