package com.zebra.rfid.demo.sdksample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    Button cp6;
    Button cp8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cp6 = findViewById(R.id.cp6);
        cp8 = findViewById(R.id.cp8);
        cp8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, PinVinnActivity.class);
                startActivity(i);
                finish();
            }
        });
        cp6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, ScanVinPinActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
