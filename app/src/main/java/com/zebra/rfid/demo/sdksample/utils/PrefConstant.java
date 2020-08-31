package com.zebra.rfid.demo.sdksample.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public final class PrefConstant {
    //public static final String SERVER_IP = ;
    @NotNull
    public final String SERVER_IP = "SERVER_IP";
    @NotNull
    public final String MAX_POWER = "MAX_POWER";

    @NotNull
    public final String getSERVER_IP() {
        return this.SERVER_IP;
    }
    @NotNull
    public final String getMAX_POWER() {
        return this.MAX_POWER;
    }
}

