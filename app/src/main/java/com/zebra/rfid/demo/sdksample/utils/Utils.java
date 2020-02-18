package com.zebra.rfid.demo.sdksample.utils;

import android.util.Log;

public class Utils {

    public static String getUrl(String route) {
        String url = Constants.SERVER_URL + route;
        Log.d("calling api", url);
        return url;
    }

//    public static String getImageUrl(String image) {
//        return Constants.SERVER_URL + image;
//    }
}
