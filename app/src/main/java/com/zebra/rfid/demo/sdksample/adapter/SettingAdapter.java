package com.zebra.rfid.demo.sdksample.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zebra.rfid.demo.sdksample.R;
import com.zebra.rfid.demo.sdksample.database.ScanPinVin;
import com.zebra.rfid.demo.sdksample.database.Setting;

import java.util.ArrayList;

public class SettingAdapter {

    private final SettingAdapter.Listener listener;

    public interface Listener {
        void onAdd(int index);
    }

    private final Context context;
    private final ArrayList<Setting> setting;


    public SettingAdapter(Context context, ArrayList<Setting> setting, SettingAdapter.Listener listener) {
        this.context = context;
        this.setting = setting;
        this.listener = listener;
    }
    
}
