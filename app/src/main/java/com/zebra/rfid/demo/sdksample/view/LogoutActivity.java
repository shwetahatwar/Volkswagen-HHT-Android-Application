package com.zebra.rfid.demo.sdksample.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zebra.rfid.demo.sdksample.R;

public class LogoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}