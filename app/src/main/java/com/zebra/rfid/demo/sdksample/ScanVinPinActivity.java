package com.zebra.rfid.demo.sdksample;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.zebra.rfid.demo.sdksample.database.ScanPinVin;
import com.zebra.rfid.demo.sdksample.view.ScanPinVinAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ScanVinPinActivity extends AppCompatActivity {

    private ScanPinVinAdapter mAdapter;
    private List<ScanPinVin> notesList = new ArrayList<ScanPinVin>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noNotesView;

    String st;
    String pin1;
    String vin1;
    EditText pin;
    EditText vin;
    TextView readData;
    Button button1;
    Button button3;
    String URL ="http://192.168.0.11:8000/masters";
    String URL1 ="http://192.168.0.11:8000/masters" + vin;
    Integer s;
    Integer s1;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_vin_pin);

        button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ScanVinPinActivity.this,HomeActivity.class);
                startActivity(i);
                finish();
            }
        });
//GET API for masters pin vin epcid
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Request GET response",response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Request response",error.toString());
                    }
                }
        );

        requestQueue.add(objectRequest);

        pin = findViewById(R.id.pin);
        vin = findViewById(R.id.vin);
        readData = findViewById(R.id.readData);
        button1 = findViewById(R.id.button1);


        mButton = findViewById(R.id.button1);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ScanVinPinActivity.this, MainActivity.class);
                pin1 = pin.getText().toString();
                vin1 = vin.getText().toString();
                st = pin.getText().toString() + "#" + vin.getText().toString();
                // st = pin.getText().toString();
                //st = Integer.toHexString(Integer.parseInt(pin.toString())) + "#" + Integer.toHexString(Integer.parseInt(vin.toString()));

                i.putExtra("pin1",pin1);
                i.putExtra("vin1",vin1);
                i.putExtra("Value", st);
                startActivity(i);
                finish();
            }
        });

    }

}
