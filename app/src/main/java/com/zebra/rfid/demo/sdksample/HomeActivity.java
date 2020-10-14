package com.zebra.rfid.demo.sdksample;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.zebra.rfid.demo.sdksample.ReadTagActivity;
import com.zebra.rfid.demo.sdksample.utils.Constants;
import com.zebra.rfid.demo.sdksample.utils.PrefConstant;
import com.zebra.rfid.demo.sdksample.utils.PrefRepository;
import com.zebra.rfid.demo.sdksample.view.LogoutActivity;
import com.zebra.rfid.demo.sdksample.view.ScanCompareTagActivity;
import com.zebra.rfid.demo.sdksample.view.SettingActivity;

import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import butterknife.internal.ListenerClass;

import static android.graphics.Color.GREEN;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button cp6;
    Button cp8;
    View cp8Layout;
    View cp6Layout;
    View cp10Layout;
    Button cp10;
    private Button[] btn = new Button[3];
    private Button btn_focus;
    private int[] btn_id = {R.id.btn0, R.id.cp6, R.id.cp10};
    String URL = "";
    String role = "";
    TextView ipaddressvalue;
    String ipvaluetoset;
    public static String antena = "";


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("ŠAVWIPL");

        ipaddressvalue = findViewById(R.id.ip_address_value_home);
        @SuppressLint("WrongConstant") SharedPreferences sh = getSharedPreferences("MySharedPref",
                Context.MODE_APPEND);
        URL = sh.getString("server_ip", "");
        role = sh.getString("role", "").toString();
        ipaddressvalue.setText(URL);
        antena = sh.getString("max_antena", "");

        ipvaluetoset = ipaddressvalue.getText().toString();
        Log.d("ip address value 54-->", ipvaluetoset);
        Log.d("max antena value -->", antena);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

//        URL = URL+"/masters";
        URL = URL + "/rfidtagmasters";
        JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(JSONArray response) {
                        ipaddressvalue.setTextColor(Color.parseColor("#07bc0c"));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Request response",error.toString());
                        ipaddressvalue.setTextColor(Color.parseColor("#FF0000"));
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

        // To set role wise visibility
        cp8 = findViewById(R.id.cp8);
        cp10Layout = findViewById(R.id.cp10Layout);
        cp8Layout = findViewById(R.id.cp8LinearLayout);
        cp6Layout = findViewById(R.id.cp6Layout);
        if (role.matches("Admin")) {
            cp8Layout.setVisibility(View.VISIBLE);
            cp10Layout.setVisibility(View.VISIBLE);
            cp6Layout.setVisibility(View.VISIBLE);
        }else if (role.matches("CP6")) {
            cp10Layout.setVisibility(View.VISIBLE);
            cp6Layout.setVisibility(View.VISIBLE);
        }else if (role.matches("CP8")) {
            cp8Layout.setVisibility(View.VISIBLE);
        }

        cp8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                @SuppressLint("WrongConstant") SharedPreferences sh = getSharedPreferences("MySharedPref",
                        Context.MODE_APPEND);
                URL = sh.getString("server_ip", "");

                Log.d("url-->", URL);
                if (URL == "" || URL == null){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
                    alertDialogBuilder.setMessage("Please enter Server IP and MAX Power");
                    alertDialogBuilder.setPositiveButton("yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                                    startActivity(intent);
                                }
                            });
                    alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) { finish();}
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else {
                    Intent i = new Intent(HomeActivity.this, ScanCompareTagActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }
            }
        });

        for (int i = 0; i < btn.length; i++) {
            btn[i] = (Button) findViewById(btn_id[i]);
            btn[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        @SuppressLint("WrongConstant") SharedPreferences sh = getSharedPreferences("MySharedPref",
                Context.MODE_APPEND);
        URL = sh.getString("server_ip", "");
        role = sh.getString("role", "");
        Log.d("Accessing URL -->", URL);

        switch (v.getId()) {
                case R.id.btn0:
                    break;

                case R.id.cp6:
                    if (URL == "" || URL == null){
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
                        alertDialogBuilder.setMessage("Please enter Server IP and MAX Power");
                        alertDialogBuilder.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                                        startActivity(intent);
                                    }
                                });
                        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { finish();}
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }else {
                        Intent intent = new Intent(HomeActivity.this, ScanVinPinActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    break;

                case R.id.cp10:
                    if (URL == "" || URL == null){
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
                        alertDialogBuilder.setMessage("Please enter Server IP and MAX Power");
                        alertDialogBuilder.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                                        startActivity(intent);
                                    }
                                });
                        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { finish();}
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }else {
                        Intent intent1 = new Intent(HomeActivity.this, ReadTagActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);
                        finish();
                    }
                    break;
            }
       }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu_main1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_logout:
                Intent intent1 = new Intent(this, LogoutActivity.class);
                this.startActivity(intent1);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
