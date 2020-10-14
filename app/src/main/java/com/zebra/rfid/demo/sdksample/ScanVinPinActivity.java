package com.zebra.rfid.demo.sdksample;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
public class ScanVinPinActivity extends AppCompatActivity {
    String URL;
    String st;
    String pin1;
    String vin1;
    EditText pinValue;
    EditText vinValue;
    boolean pinFlag = false;
    boolean vinFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_vin_pin);
        @SuppressLint("WrongConstant") SharedPreferences sh = getSharedPreferences("MySharedPref",
                Context.MODE_APPEND);
        URL = sh.getString("server_ip", "");
        URL = URL + "/rfidtagmasters";
        Log.d("Accessing URL -->", URL);
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

        ){
            @Override
            public Map<String, String> getHeaders() {
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",
                        MODE_PRIVATE);
                String token = sharedPreferences.getString("token", "");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "JWT "+token);
                // params.put("content-type", "application/json");
                return params;
            }
        };
        requestQueue.add(objectRequest);
        pinValue = findViewById(R.id.pinValue);
        vinValue = findViewById(R.id.vinValue);
        pinValue.setText("");
        vinValue.setText("");

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        pinValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                Log.d("string scanned", String.valueOf(s));
                if(String.valueOf(s).length() > 0){
                    if(String.valueOf(s).charAt(String.valueOf(s).length() - 1)  == '\n') {
                        pinValue.setText(pinValue.getText().toString().substring(0,pinValue.getText().toString().length()-1));
                        validatePIN();
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            private boolean validatePIN() {
                if (pinValue.getText().toString().trim().isEmpty()) {
                    CharSequence text = "PIN can't be empty";
                    Toast toast = Toast.makeText(context, text, duration);
                    View v = toast.getView();
                    v.getBackground().setColorFilter(Color.parseColor("#ff0000"), PorterDuff.Mode.SRC_IN);
                    TextView t = v.findViewById(android.R.id.message);
                    t.setTextColor(Color.WHITE);
                    toast.show();
                    pinValue.requestFocus();
                    pinFlag = false;
                    vinFlag = false;
                    return false;
                }
                else if (pinValue.getText().toString().length() != 14){
                    CharSequence text = "Invalid PIN Number Scanned";
                    Toast toast = Toast.makeText(context, text, duration);
                    View v = toast.getView();
                    v.getBackground().setColorFilter(Color.parseColor("#ff0000"), PorterDuff.Mode.SRC_IN);
                    TextView t = v.findViewById(android.R.id.message);
                    t.setTextColor(Color.WHITE);
                    toast.show();
                    pinFlag = false;
                    vinFlag = false;
                    pinValue.getText().clear();
                    pinValue.requestFocus();
                    return false;
                }
                else {
                    CharSequence text = "Scanned Successfully!";
                    pinFlag = true;
                    Toast toast = Toast.makeText(context, text, duration);
                    View v = toast.getView();
                    v.getBackground().setColorFilter(Color.parseColor("#07bc0c"), PorterDuff.Mode.SRC_IN);
                    TextView t = v.findViewById(android.R.id.message);
                    t.setTextColor(Color.WHITE);
                    toast.show();
                    pinValue.clearFocus();
                    vinValue.requestFocus();
                    if (vinValue.getText().toString().length() == 17) {
                        vin1 = vinValue.getText().toString();
                        pin1 = pinValue.getText().toString();
                        Intent i = new Intent(ScanVinPinActivity.this, MainActivity.class);
                        i.putExtra("pin1", pin1);
                        i.putExtra("vin1", vin1);
                        i.putExtra("Value", st);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                    else {
                        vinValue.setText("");
                        vinValue.requestFocus();
                        return false;
                    }
                    return true;
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {}
        });

        vinValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if(String.valueOf(s).length() > 0) {
                    if (String.valueOf(s).charAt(String.valueOf(s).length() - 1) == '\n') {
                        vinValue.setText(vinValue.getText().toString().substring(0, vinValue.getText().toString().length()-1));
                        validateVIN();
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            private boolean validateVIN() {
                if (vinValue.getText().toString().trim().isEmpty()) {
                    CharSequence text = "VIN can't be empty";
                    Toast toast = Toast.makeText(context, text, duration);
                    View v = toast.getView();
                    v.getBackground().setColorFilter(Color.parseColor("#ff0000"), PorterDuff.Mode.SRC_IN);
                    TextView t = v.findViewById(android.R.id.message);
                    t.setTextColor(Color.WHITE);
                    toast.show();
                    vinValue.requestFocus();
                    pinFlag = false;
                    vinFlag = false;
                    return false;
                }
                else if ( vinValue.getText().toString().length() != 17 ){
                    CharSequence text = "Invalid VIN Number Scanned";
                    Toast toast = Toast.makeText(context, text, duration);
                    View v = toast.getView();
                    v.getBackground().setColorFilter(Color.parseColor("#ff0000"), PorterDuff.Mode.SRC_IN);
                    TextView t = v.findViewById(android.R.id.message);
                    t.setTextColor(Color.WHITE);
                    toast.show();
                    pinFlag = false;
                    vinFlag = false;
                    vinValue.getText().clear();
                    return false;
                }
                else {
                    CharSequence text = "Scanned Successfully!";
                    vinFlag = true;
                    Toast toast = Toast.makeText(context, text, duration);
                    View v = toast.getView();
                    TextView t = v.findViewById(android.R.id.message);
                    Toast.makeText(context, text, duration);
                    v = toast.getView();
                    v.getBackground().setColorFilter(Color.parseColor("#07bc0c"), PorterDuff.Mode.SRC_IN);
                    t.setTextColor(Color.WHITE);
                    toast.show();
                    if (pinValue.getText().toString().length() == 14) {
                        vin1 = vinValue.getText().toString();
                        pin1 = pinValue.getText().toString();
                        Intent i = new Intent(ScanVinPinActivity.this, MainActivity.class);
                        i.putExtra("pin1", pin1);
                        i.putExtra("vin1", vin1);
                        i.putExtra("Value", st);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                    else {
                        pinValue.setText("");
                        pinValue.requestFocus();
                        return false;
                    }
                    return true;
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {}
        });
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setSelectedItemId(R.id.back);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.back:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }
}