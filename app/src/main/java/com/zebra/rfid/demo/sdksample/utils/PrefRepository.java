package com.zebra.rfid.demo.sdksample.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public final class PrefRepository {
    private final HashMap prefs = new HashMap();
    @NotNull
    private static final PrefRepository singleInstance = new PrefRepository();
    public static final PrefRepository.Companion Companion = new Companion((DefaultConstructorMarker)null);
    //private Object Intrinsics;

    public final void setKeyValue(@NotNull String key, @NotNull String value) {
        (( Map )this.prefs).put(key, value);
    }
    @NotNull
    public final String getValueOrDefault(@NotNull String key, @NotNull String defaultValue) {

//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        String imgSett = prefs.getString(keyChannel, "");

        String keyValue = (String)this.prefs.get(key);
        String url = "";
        // Log.d("--keyValue--->", keyValue);
//        if (keyValue == null) {
//            keyValue = defaultValue;
//        }
        return keyValue;
    }

    public final void serializePrefs(@NotNull Context context) {
        //Intrinsics.checkParameterIsNotNull(context, "context");
        SharedPreferences shared = context.getSharedPreferences("default", 0);
        if (shared != null) {
            SharedPreferences sharedPref = shared;
            SharedPreferences.Editor editor = sharedPref.edit();
            Set var10 = this.prefs.keySet();
            //Intrinsics.checkExpressionValueIsNotNull(var10, "prefs.keys");
            Iterable var4 = (Iterable)var10;
            boolean $i$f$forEach = false;
            $i$f$forEach = false;
            String it;
            String var10002;
            for(Iterator var6 = var4.iterator(); var6.hasNext(); editor.putString(it, var10002)) {
                Object element$iv = var6.next();
                it = (String)element$iv;
                boolean var9 = false;
                var10002 = (String)this.prefs.get(it);
                if (var10002 == null) {
                    var10002 = "";
                }
            }
            editor.commit();
        }
    }
    public final void deserializePrefs(@NotNull Context context) {
        // Intrinsics.checkParameterIsNotNull(context, "context");
        SharedPreferences var10000 = context.getSharedPreferences("default", 0);
        if (var10000 != null) {
            SharedPreferences sharedPref = var10000;
            Iterable $this$forEach$iv = (Iterable)sharedPref.getAll().keySet();
            Iterator var5 = $this$forEach$iv.iterator();
            while(var5.hasNext()) {
                Object element$iv = var5.next();
                String it = (String)element$iv;
                Map var14 = (Map)this.prefs;
                String var9 = sharedPref.getString(it, "");
                Map var11 = var14;
                boolean var12 = false;
                String var15 = var9;
                if (var9 == null) {
                    var15 = "";
                }
                String var13 = var15;
                var11.put(it, var13);
            }
        }
    }

    public static final class Companion {
        @NotNull
        public final PrefRepository getSingleInstance() {
            return PrefRepository.singleInstance;
        }
        private Companion() {
        }
        // $FF: synthetic method
        public Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

