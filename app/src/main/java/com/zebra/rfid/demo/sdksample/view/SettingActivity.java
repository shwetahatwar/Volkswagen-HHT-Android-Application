package com.zebra.rfid.demo.sdksample.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zebra.rfid.demo.sdksample.R;
import java.net.Inet4Address;
import java.net.UnknownHostException;

public class SettingActivity extends AppCompatActivity {

    Button setting_save;
    TextView power_antena_value;
    TextView ip_address_value;
    String antenna;
    String ip;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setting_save = findViewById(R.id.setting_save);
        power_antena_value = findViewById(R.id.power_antena_value);
        ip_address_value = findViewById(R.id.ip_address_value);
        String URL = "";
        String MAX_POWER = "";

        @SuppressLint("WrongConstant") SharedPreferences sh = getSharedPreferences("MySharedPref",
                Context.MODE_APPEND);
        String ipAddressText = sh.getString("server_ip", "");
        String antenaPowerText = sh.getString("max_antena", "");
        power_antena_value.setText(antenaPowerText);
        ip_address_value.setText(ipAddressText);

        setting_save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
            @Override
            public void onClick(View view) {
                String oldIPAddressText = sh.getString("server_ip", "");
                String oldAntenaPowerText = sh.getString("max_antena", "");
                antenna = power_antena_value.getText().toString();
                url = ip_address_value.getText().toString();
                ValidateIPv4 val = new ValidateIPv4();

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                boolean flag = true;
                boolean validIPURL = false;

                if(oldIPAddressText.isEmpty()){
                    oldIPAddressText = "oldIPAddressText";
                }
                if(oldAntenaPowerText.isEmpty()){
                    oldAntenaPowerText = "oldAntenaPowerText";
                }
                if(url.isEmpty() || antenna.isEmpty()){
                    CharSequence text = "IP Address or Antena Power cannot be empty";
                    Toast toast = Toast.makeText(context, text, duration);
                    View v = toast.getView();
                    v.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                    TextView t = v.findViewById(android.R.id.message);
                    t.setTextColor(Color.WHITE);
                    toast.show();
                }
                else {
                    if(url.equals(oldIPAddressText)) {
                        if(antenna.equals(oldAntenaPowerText)){
                            CharSequence text = "No Change in IP Address and Antena";
                            Toast toast = Toast.makeText(context, text, duration);
                            View v = toast.getView();
                            v.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                            TextView t = v.findViewById(android.R.id.message);
                            t.setTextColor(Color.WHITE);
                            toast.show();
                        }
                        else {
                            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",
                                    MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            myEdit.putString( "max_antena", power_antena_value.getText().toString());
                            myEdit.commit();
                            CharSequence text = "Antena Power Updated";
                            Toast toast = Toast.makeText(context, text, duration);
                            View v = toast.getView();
                            v.getBackground().setColorFilter(Color.parseColor("#07bc0c"), PorterDuff.Mode.SRC_IN);
                            TextView t = v.findViewById(android.R.id.message);
                            t.setTextColor(Color.WHITE);
                            toast.show();
                        }
                    }
                    else {
                        if (url.contains("//")){
                            ip = url.split("//")[1];
                            if (ip.contains(":")) {
                                ip = ip.split(":")[0];
                            }
                        }else{
                            CharSequence text = "Invalid IP/URL Address!";
                            Toast toast = Toast.makeText(context, text, duration);
                            View v = toast.getView();
                            v.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                            TextView t = v.findViewById(android.R.id.message);
                            t.setTextColor(Color.WHITE);
                            toast.show();
                            Intent intent = new Intent(String.valueOf(SettingActivity.class));
                            this.startActivity(intent);
                        }

                        if(antenna.equals(oldAntenaPowerText)){
                            if (val.isValidInet4Address(ip)) {
                                Log.d("The IP address is valid", ip);

                                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",
                                        MODE_PRIVATE);
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                myEdit.putString( "server_ip", ip_address_value.getText().toString());
                                myEdit.commit();
                                CharSequence text = "The IP address Updated";
                                Toast toast = Toast.makeText(context, text, duration);
                                View v = toast.getView();
                                v.getBackground().setColorFilter(Color.parseColor("#07bc0c"), PorterDuff.Mode.SRC_IN);
                                TextView t = v.findViewById(android.R.id.message);
                                t.setTextColor(Color.WHITE);
                                toast.show();

                            } else {
                                flag = false;
                                // Log.d("The IP invalid", ip);
                                CharSequence text = "Invalid IP Address!";
                                Toast toast = Toast.makeText(context, text, duration);
                                View v = toast.getView();
                                v.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                                TextView t = v.findViewById(android.R.id.message);
                                t.setTextColor(Color.WHITE);
                                toast.show();
                            }
                            if (flag == false){
                                Intent intent = new Intent(String.valueOf(SettingActivity.class));
                                this.startActivity(intent);
                                //return true;
                            }
                            finish();
                        }
                        else {
                            if (val.isValidInet4Address(ip)) {
                                //Log.d("The IP address is valid", ip);

                                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",
                                        MODE_PRIVATE);
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                myEdit.putString( "server_ip", ip_address_value.getText().toString());
                                myEdit.putString( "max_antena", power_antena_value.getText().toString());
                                myEdit.commit();
                                CharSequence text = "The IP address and Antena Power Updated";
                                Toast toast = Toast.makeText(context, text, duration);
                                View v = toast.getView();
                                v.getBackground().setColorFilter(Color.parseColor("#07bc0c"), PorterDuff.Mode.SRC_IN);
                                TextView t = v.findViewById(android.R.id.message);
                                t.setTextColor(Color.WHITE);
                                toast.show();

                            } else {
                                flag = false;
                                // Log.d("The IP invalid", ip);
                                CharSequence text = "Invalid IP Address!";
                                Toast toast = Toast.makeText(context, text, duration);
                                View v = toast.getView();
                                v.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                                TextView t = v.findViewById(android.R.id.message);
                                t.setTextColor(Color.WHITE);
                                toast.show();
                            }
                            if (flag == false){
                                Intent intent = new Intent(String.valueOf(SettingActivity.class));
                                this.startActivity(intent);
                                //return true;
                            }
                            finish();
                        }
                    }
                }
            }

            private void startActivity(Intent intent) {
                intent.putExtra("url", ip_address_value.getText().toString());
            }
        });
    }
}


class ValidateIPv4 {
    public static boolean isValidInet4Address(String ip) {
        try {
            return Inet4Address.getByName(ip)
                    .getHostAddress().equals(ip);
        } catch (Exception ex) {
            // Log.d("Exception in ip ", ex.getLocalizedMessage());
            return false;
        }
    }
    public boolean isValidURL(String url) {
        Log.d("url--", url);
        try {
            (new java.net.URL(url)).openStream().close();
            return true;
        } catch (Exception ex) {
            Log.d("exception --", String.valueOf(ex));
        }
        return false;
    }
}