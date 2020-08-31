package com.zebra.rfid.demo.sdksample.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zebra.rfid.api3.ENUM_TRIGGER_MODE;
import com.zebra.rfid.api3.InvalidUsageException;
import com.zebra.rfid.api3.OperationFailureException;
import com.zebra.rfid.api3.SetAttribute;
import com.zebra.rfid.demo.sdksample.HomeActivity;
import com.zebra.rfid.demo.sdksample.MainActivity;
import com.zebra.rfid.demo.sdksample.PinVinnActivity;
import com.zebra.rfid.demo.sdksample.R;
import com.zebra.rfid.demo.sdksample.ScanVinPinActivity;
import com.zebra.rfid.api3.RFIDReader;
import com.zebra.rfid.api3.ReaderDevice;
import com.zebra.rfid.api3.Readers;

public class ScanCompareTagActivity extends AppCompatActivity {

    String stPin;
    String pin;
    EditText pinCompare;
    EditText textviewCp;
    public static RFIDReader reader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("String tag", "---------------------");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_compare_tag);

        pinCompare = findViewById(R.id.pinCompare);
        int duration = Toast.LENGTH_SHORT;

        pinCompare.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(String.valueOf(s).length() > 0){
                    if(String.valueOf(s).charAt(String.valueOf(s).length() - 1)  == '\n') {
                        pinCompare.setText(pinCompare.getText().toString().substring(0,pinCompare.getText().toString().length()-1));
                        validatePIN();
                    }
                }
            }

            private boolean validatePIN() {
                if (pinCompare.getText().toString().trim().isEmpty()) {
                    CharSequence text = "PIN can't be empty";
                    Toast toast = Toast.makeText(ScanCompareTagActivity.this, text, duration);
                    View v = toast.getView();
                    v.getBackground().setColorFilter(Color.parseColor("#ff0000"), PorterDuff.Mode.SRC_IN);
                    TextView t = v.findViewById(android.R.id.message);
                    t.setTextColor(Color.WHITE);
                    toast.show();
                    pinCompare.requestFocus();
                    return false;
                }
                else if (pinCompare.getText().toString().length() != 14){
                    CharSequence text = "Invalid PIN Number Scanned";
                    Toast toast = Toast.makeText(ScanCompareTagActivity.this, text, duration);
                    View v = toast.getView();
                    v.getBackground().setColorFilter(Color.parseColor("#ff0000"), PorterDuff.Mode.SRC_IN);
                    TextView t = v.findViewById(android.R.id.message);
                    t.setTextColor(Color.WHITE);
                    toast.show();
                    pinCompare.getText().clear();
                    pinCompare.requestFocus();
                    return false;
                }
                else {
                    Intent i = new Intent(ScanCompareTagActivity.this, PinVinnActivity.class);
                    pin = pinCompare.getText().toString();
                    //stPin = pinCompare.getText().toString();
                    i.putExtra("pin",pin);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                    return true;
                }
            }
        });

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setSelectedItemId(R.id.back);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Log.d("String tag", "navigation");
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



