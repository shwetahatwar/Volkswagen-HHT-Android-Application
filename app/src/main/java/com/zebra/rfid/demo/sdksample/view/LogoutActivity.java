package com.zebra.rfid.demo.sdksample.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.zebra.rfid.demo.sdksample.HomeActivity;
import com.zebra.rfid.demo.sdksample.R;

public class LogoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        @SuppressLint("WrongConstant") SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",
                MODE_APPEND);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString( "token", "");
        myEdit.putString( "role", "");
        myEdit.commit();

        Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}