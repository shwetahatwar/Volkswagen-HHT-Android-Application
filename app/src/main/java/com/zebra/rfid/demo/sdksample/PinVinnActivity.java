package com.zebra.rfid.demo.sdksample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PinVinnActivity extends AppCompatActivity {

    Button buttonCp8;
    Button buttonBack;
    TextView textViewCp8;
    EditText textviewCp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_vinn);
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PinVinnActivity.this,HomeActivity.class);
                startActivity(i);
                finish();
            }
        });

//        readData = findViewById(R.id.readData);
//        st = getIntent().getExtras().getString("Value");
//        readData.setText(st);




        buttonCp8 =findViewById(R.id.buttonCp8);
        buttonCp8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PinVinnActivity.this,DisplayDbActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
