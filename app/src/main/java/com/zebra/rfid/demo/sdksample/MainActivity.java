package com.zebra.rfid.demo.sdksample;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zebra.rfid.api3.ACCESS_OPERATION_CODE;
import com.zebra.rfid.api3.ACCESS_OPERATION_STATUS;
import com.zebra.rfid.api3.ENUM_TRIGGER_MODE;
import com.zebra.rfid.api3.InvalidUsageException;
import com.zebra.rfid.api3.MEMORY_BANK;
import com.zebra.rfid.api3.OperationFailureException;
import com.zebra.rfid.api3.RFIDReader;
import com.zebra.rfid.api3.ReaderDevice;
import com.zebra.rfid.api3.Readers;
import com.zebra.rfid.api3.TagAccess;
import com.zebra.rfid.api3.TagData;
import com.zebra.rfid.demo.sdksample.view.ScanCompareTagActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements RFIDHandler.ResponseHandlerInterface {

    public TextView statusTextViewRFID = null;
    private TextView textrfid;
    private TextView TagIdView;
    public static RFIDReader reader;
    private RFIDHandler.ResponseHandlerInterface context;
    private static ArrayList<ReaderDevice> availableRFIDReaderList;
    private static ReaderDevice readerDevice;
    private RFIDHandler.EventHandler eventHandler;
    int counter = 0;
    boolean triggerPressed = true;
    boolean isSucess = true;

    RFIDPerformInevntoryClass rfidPerformInevntoryClass = new RFIDPerformInevntoryClass();
    hexConverstion newHexConverstion = new hexConverstion();

    private static Readers readers;

    TextView mainpin;
    TextView mainvin;
    String st;
    String st1;
    String checkRFIDData;
    String vinData;
    String pinData;
    String writeData,TagDataShow;
    String URL;
    String TIDData;

    private Button mButton;
    private Button mWriteTagButton;
    TagData[] tagData = null;
    RFIDHandler rfidHandler;
    final static String TAG = "RFID_SAMPLE";
    boolean checkStatus= false;
    private Object MainActivity;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainpin = findViewById(R.id.mainpin);
        mainvin = findViewById(R.id.mainvin);

        st = getIntent().getExtras().getString("Value");
        vinData = getIntent().getExtras().getString("vin1");
        pinData = getIntent().getExtras().getString("pin1");

        mainpin.setText(pinData);
        mainvin.setText(vinData);

        TagIdView = findViewById(R.id.epcTagID);

        // UI
        statusTextViewRFID = findViewById(R.id.textStatus);

        // RFID Handler
        rfidHandler = new RFIDHandler( MainActivity.this);
        rfidHandler.onCreate(this);

        //Read data on rfid reader
        mButton = findViewById(R.id.readButton);
        mWriteTagButton = findViewById(R.id.writeRFIDTagButton);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.back);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.back:
                        isSucess = true;
                        Intent intent = new Intent(getApplicationContext(), ScanVinPinActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
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

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = 0;
                rfidPerformInevntoryClass.readRFIDTagData(rfidHandler);
            }
        });

        mWriteTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataForEPC();
            }
        });

    }

    public String writeOperation() {
        try{
            if(rfidHandler != null && rfidHandler.reader != null){
            }
            else {
                rfidHandler.onDestroy();
                rfidHandler = new RFIDHandler( MainActivity.this);
                rfidHandler.onCreate(this);
            }
            String vinDataToWrite = "0" + vinData;

            String response = rfidPerformInevntoryClass.writeToTagDataFunction(rfidHandler,checkRFIDData,vinDataToWrite);
            if(response == "Failed to write") {
//                try {
//                    runOnUiThread(new Runnable() {
//                        @SuppressLint("LongLogTag")
//                        @Override
//                        public void run() {
//                            showSuccessToast("Writing to Tag. Please Wait");
//                        }
//                    });
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
            else {
                ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 200);
                toneGen1.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD,150);
            }
            return response;
        }
        catch (Exception ex){
            return "Failed to write";
        }
    }

    public String readTIDData(String tagId){
        if(rfidHandler != null && rfidHandler.reader != null){
        }
        else {
            rfidHandler.onDestroy();
            rfidHandler = new RFIDHandler(MainActivity.this);
            rfidHandler.onRFIDReadCreate(MainActivity.this);
            try{
                reader.Config.setTriggerMode(ENUM_TRIGGER_MODE.RFID_MODE, false);
            }
            catch (InvalidUsageException e) {
                showToast(e.toString());
                e.printStackTrace();
            } catch (OperationFailureException e) {
                showToast(e.toString());
                e.printStackTrace();
            }
        }
        String rfidTagIDValue = "NA";
        TagAccess tagAccess = new TagAccess();
        TagAccess.ReadAccessParams readAccessParams = tagAccess.new ReadAccessParams();
        TagData readAccessTag;
        readAccessParams.setAccessPassword(0);
        readAccessParams.setMemoryBank(MEMORY_BANK.MEMORY_BANK_TID);
        readAccessParams.setOffset(0);
        TagData tagData = null;
        try {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tagData = rfidHandler.reader.Actions.TagAccess.readWait(tagId, readAccessParams, null);
            ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 200);
            toneGen1.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD,150);
            if (tagData != null) {
                ACCESS_OPERATION_CODE readAccessOperation = tagData.getOpCode();
                if (readAccessOperation != null) {
                    if (tagData.getOpStatus() != null && !tagData.getOpStatus().equals(ACCESS_OPERATION_STATUS.ACCESS_SUCCESS)) {
                        String strErr = tagData.getOpStatus().toString().replaceAll("_", " ");
                        showToast("Tag ID Reading Failed");
                        rfidTagIDValue = "NA";

                    } else {
                        if (tagData.getOpCode() == ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ) {
                            rfidTagIDValue = tagData.getMemoryBankData();
                            if(rfidTagIDValue.length() >= 8){
                                rfidTagIDValue = rfidTagIDValue.substring(8, 24);
                            }

                        }
                    }
                }
            }
        } catch (InvalidUsageException e) {
            e.printStackTrace();
        } catch (OperationFailureException e) {
            e.printStackTrace();
        }
        return rfidTagIDValue;
    }

    public void postRequesttoDatabase() {
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            @SuppressLint("WrongConstant") SharedPreferences sh = getSharedPreferences("MySharedPref",
                    Context.MODE_APPEND);
            URL = sh.getString("server_ip", "");
            URL = URL + "/rfidtagmasters";
            StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String respone = writeOperation();
                            if(respone == "Failed to write"){
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                                alertDialogBuilder.setMessage("Failed to write. Please make sure the RFID Tag is in front of Reader and Try Again.");
                                alertDialogBuilder.setPositiveButton("ok",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            isSucess = true;
                                            triggerPressed = true;
                                        }
                                    }
                                );
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }
                            else {
                                showSuccessToast("Data Updated Successfully with TAG ID PIN number and VIN number");
                                isSucess = true;
                                Intent intent = new Intent(MainActivity.this, ScanVinPinActivity.class);
                                startActivity(intent);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                            String json = "Data Already Present in Database";
                            NetworkResponse response = error.networkResponse;
                            if(response != null && response.data != null){
                                switch(response.statusCode){
                                    case 400: {
                                        json = new String(response.data);
                                        json = json.substring(21, json.length() - 3);
                                        break;
                                    }
                                }
                            }
                            if(json.length() <= 0){
                                json = "Please try again";
                            }
                            alertDialogBuilder.setMessage(json);
                            alertDialogBuilder.setPositiveButton("ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        rfidHandler.onDestroy();
                                        isSucess = true;
                                        Intent intent = new Intent(MainActivity.this, ScanVinPinActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            );
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("vinNumber", vinData);
                    params.put("pinNumber", pinData);
                    params.put("epcId", TIDData);

                    return params;
                }

            };
            queue.add(postRequest);
        }
        catch (Exception e) {
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        rfidHandler.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        rfidHandler.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rfidHandler.onDestroy();
    }

    @Override
    public void handleTagdata(TagData[] tagData) {
        try {
            Thread.sleep(2000);
//            if(counter < 2) {
                final StringBuilder sb = new StringBuilder();
                sb.setLength(0);
                if(tagData.length != 0) {
                    for (int index = 0; index < 1; index++) {
                        sb.append(tagData[index].getTagID());
                        tagData[0].getMemoryBank();
                    }
                    Thread.sleep(2000);
                    checkRFIDData = sb.toString();
                    TIDData = readTIDData(sb.toString());
                    TagIdView.setText(TIDData);
                }

//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
////                        checkRFIDData = sb.toString();
////                        TIDData = readTIDData(sb.toString());
////                        TagIdView.setText(TIDData);
//                    }
//                });
//            }
//            else {
//                runOnUiThread(new Runnable() {
//                    @SuppressLint("LongLogTag")
//                    @Override
//                    public void run() {
//                        showToast("Please ensure only one tag is in the Vicinity and Try Again");
//                    }
//                });
//            }
        }
        catch (Exception ex) {

        }
    }

    public void showToast(String error) {
        Toast.makeText(this, Html.fromHtml("<font color='#ff4500' ><b>" + error + "</b></font>"), Toast.LENGTH_SHORT).show();
    }

    public void showSuccessToast(String text){
        Toast.makeText(this, Html.fromHtml("<font color='#07bc0c' ><b>" + text + "</b></font>"), Toast.LENGTH_SHORT).show();
    }

    //on trigger rfid read
    @SuppressLint("LongLogTag")
    public void handleTriggerPress(boolean pressed) {
        if(triggerPressed == true && isSucess == true) {
            isSucess = false;
            triggerPressed = false;
            counter = 0;
            String textrfid = TagIdView.getText().toString();

            if (textrfid.length() > 0 && textrfid.length() == 16) {
                getDataForEPC();
            } else {
                rfidPerformInevntoryClass.readRFIDTagData(rfidHandler);
                isSucess = true;
            }
            triggerPressed = true;
        }
    }

    @Override
    public void handleRFIDStatus(String status) {
        statusTextViewRFID.setText(status);
        if (status == "Connected"){
            statusTextViewRFID.setTextColor(Color.parseColor("#07bc0c"));
        }else{
            statusTextViewRFID.setTextColor(Color.parseColor("#FF0000"));
        }
    }

    public void getDataForEPC() {
        try{
            @SuppressLint("WrongConstant") SharedPreferences sh = getSharedPreferences("MySharedPref",
                    Context.MODE_APPEND);
            URL = sh.getString("server_ip", "");
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            URL = URL + "/rfidtagmasters?epcId=" + TIDData;
            JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject actor = null;
                        String databaseVinNumber = "";
                        String databasePinNumber = "";
                        try {
                            actor = response.getJSONObject(0);
                            databaseVinNumber = actor.getString("vinNumber");
                            databasePinNumber = actor.getString("pinNumber");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("Request GET response",mainvin.getText().toString());
                        String mainVinNumber = mainvin.getText().toString();
                        String mainPinNumber = mainpin.getText().toString();
                        if(databaseVinNumber == "" || databaseVinNumber == null) {
                            Log.e("Not Present in Database","Not Present in Database");
                            postRequesttoDatabase();
                        }
                        else if(mainVinNumber.equals(databaseVinNumber) && mainPinNumber.equals(databasePinNumber)){
                            Log.e("Data Match","Data Matched");
                            try{
                                String respone = writeOperation();
                                if(respone == "Failed to write"){
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                                    alertDialogBuilder.setMessage("Failed to write. Please make sure the RFID Tag is in front of Reader and Try Again.");
                                    alertDialogBuilder.setPositiveButton("ok",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {

                                                }
                                            }
                                    );
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                                else {
                                    showSuccessToast("Data Updated Successfully with TAG ID PIN number and VIN number");
                                    isSucess = true;
                                    Intent intent = new Intent(MainActivity.this, ScanVinPinActivity.class);
                                    startActivity(intent);
                                }
//                                runOnUiThread(new Runnable() {
//                                    @SuppressLint("LongLogTag")
//                                    @Override
//                                    public void run() {
//                                        showSuccessToast("Writing to Tag. Please Wait");
//                                    }
//                                });
                            }
                            catch (Exception ex){

                            }
                        }
                        else {
                            Log.e("Data Not Match","Data Not Matched");
                            postRequesttoDatabase();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Not Present in Database",error.toString());
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                        String json = "Device not connected to Network. Please check your connection and try again";
                        NetworkResponse response = error.networkResponse;
                        if(response != null && response.data != null){
                            switch(response.statusCode){
                                case 400: {
                                    json = new String(response.data);
                                    json = json.substring(21, json.length() - 3);
                                    break;
                                }
                            }
                        }
                        if(json.length() <= 0){
                            json = "Please try again";
                        }
                        alertDialogBuilder.setMessage(json);
                        alertDialogBuilder.setPositiveButton("ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        rfidHandler.onDestroy();
                                        isSucess = true;
                                        Intent intent = new Intent(MainActivity.this, ScanVinPinActivity.class);
                                        startActivity(intent);
                                    }
                                }
                        );
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }
            );
            requestQueue.add(objectRequest);

        }
        catch (Exception ex) {
            isSucess = true;
            showSuccessToast(ex.toString());
        }
    }

    @Override
    public void updateCounter() {
        counter++;
    }
}