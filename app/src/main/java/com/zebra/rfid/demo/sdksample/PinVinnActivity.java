package com.zebra.rfid.demo.sdksample;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.nfc.Tag;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.zebra.rfid.api3.ACCESS_OPERATION_CODE;
import com.zebra.rfid.api3.ACCESS_OPERATION_STATUS;
import com.zebra.rfid.api3.InvalidUsageException;
import com.zebra.rfid.api3.MEMORY_BANK;
import com.zebra.rfid.api3.OperationFailureException;
import com.zebra.rfid.api3.Readers;
import com.zebra.rfid.api3.TagAccess;
import com.zebra.rfid.api3.TagData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.zebra.rfid.api3.Readers;
import com.zebra.rfid.demo.sdksample.view.ScanCompareTagActivity;


public class PinVinnActivity extends AppCompatActivity implements RFIDHandler.ResponseHandlerInterface  {
    Button buttonBack;
    Button readTagButton;
    String pinValue;
    TextView tagIdValue;
    TextView vinNumberValue;
    TextView pinNumberValue;
    LinearLayout dbContentValue;
    TextView rfidTagPinNumberValue;
    TextView resultView;
    TextView tagPinNumberValue;
    TextView rfid_user_memory_value;
    private RFIDHandler rfidLocalHandler;
    private Boolean checkStatus = false;
    private TextView readerStatusValue;
    private static Readers readers;
    int counter = 0;
    boolean triggerPressed = true;
    RFIDPerformInevntoryClass rfidPerformInevntoryClass = new RFIDPerformInevntoryClass();
    hexConverstion newHexConverstion = new hexConverstion();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        rfidLocalHandler.onDestroy();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_vinn);
        buttonBack = findViewById(R.id.buttonBack);

        rfidLocalHandler = new RFIDHandler(this);
        rfidLocalHandler.onPINReadCreate(this);

        rfid_user_memory_value = findViewById(R.id.rfid_user_memory_value);
        tagPinNumberValue = findViewById(R.id.tagPinNumberValue);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.back);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.back:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        pinNumberValue = findViewById(R.id.pinNumberValue);
        pinNumberValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0) {
                }
            }
        });

        pinNumberValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pinNumberValue.getText().length() != 0) {
                }
            }
        });

        pinNumberValue = findViewById(R.id.pinNumberValue);
        pinValue = getIntent().getExtras().getString("pin");
        pinNumberValue.setText(pinValue);
        this.fetchDataFromDatabase();

        dbContentValue  = findViewById(R.id.dbContentValue);
        tagIdValue = findViewById(R.id.tagIdValue);
        vinNumberValue = findViewById(R.id.vinNumberValue);
        pinNumberValue = findViewById(R.id.pinNumberValue);
        readerStatusValue = findViewById(R.id.readerStatusValue);

        rfidTagPinNumberValue = findViewById(R.id.tagPinNumber);
        resultView = findViewById(R.id.compareResultViewValue);

        readTagButton = findViewById(R.id.readTagAndCompareButton);
        readTagButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
            @Override
            public void onClick(View view) {
                counter = 0;
                rfidPerformInevntoryClass.readRFIDTagData(rfidLocalHandler);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private void fetchDataFromDatabase() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String pinnumber = "";
        String URL = "";
        if (pinNumberValue.getText().length() > 0) {
            pinnumber = pinNumberValue.getText().toString();

            @SuppressLint("WrongConstant") SharedPreferences sh = getSharedPreferences("MySharedPref",
                    Context.MODE_APPEND);
            URL = sh.getString("server_ip", "");
//            URL = URL + "/masters?pinNumber=" + pinnumber;
            URL = URL + "/rfidtagmasters?pinNumber=" + pinnumber;
            Log.d("Accessing URL -->", URL);

            JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() > 0) {
                            dbContentValue.setVisibility(View.VISIBLE);
                            try {
                                JSONObject object = response.getJSONObject(0);
                                tagIdValue.setText(object.getString("epcId"));
                                vinNumberValue.setText(object.getString("vinNumber"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;
                            CharSequence text = "Tag Id and Vin Number Values Comes from Database!";
                            Toast toast = Toast.makeText(context, text, duration);
                            View v = toast.getView();
                            v.getBackground().setColorFilter(Color.parseColor("#07bc0c"), PorterDuff.Mode.SRC_IN);
                            TextView t = v.findViewById(android.R.id.message);
                            t.setTextColor(Color.WHITE);
                            toast.show();
                        }
                        else {
//                            Context context = getApplicationContext();
//                            int duration = Toast.LENGTH_SHORT;
//                            CharSequence text = "Scanned PIN Number Not Present in Database!";
//                            Toast toast = Toast.makeText(context, text, duration);
//                            View v = toast.getView();
//                            v.getBackground().setColorFilter(Color.parseColor("#07bc0c"), PorterDuff.Mode.SRC_IN);
//                            TextView t = v.findViewById(android.R.id.message);
//                            t.setTextColor(Color.WHITE);
//                            toast.show();
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PinVinnActivity.this);
                            alertDialogBuilder.setMessage("Scanned PIN Number Not Present in Database!");
                            alertDialogBuilder.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            rfidLocalHandler.onDestroy();
                                            Intent intent = new Intent(PinVinnActivity.this, ScanCompareTagActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                            );
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
//                            rfidLocalHandler.onDestroy();
//                            Intent intent = new Intent(PinVinnActivity.this, ScanCompareTagActivity.class);
//                            startActivity(intent);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Context context = getApplicationContext();
//                        int duration = Toast.LENGTH_SHORT;
//                        CharSequence text = "Tag Id and Vin Number Values not present in Database!";
//                        Toast toast = Toast.makeText(context, text, duration);
//                        View v = toast.getView();
//                        v.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
//                        TextView t = v.findViewById(android.R.id.message);
//                        t.setTextColor(Color.WHITE);
//                        toast.show();
//                        pinNumberValue.setText("");
//                        rfidLocalHandler.onDestroy();
//                        Intent intent = new Intent(PinVinnActivity.this, ScanCompareTagActivity.class);
//                        startActivity(intent);
                        String json = "Device not connected to Network. Please check your connection and try again";
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PinVinnActivity.this);
                        NetworkResponse response = error.networkResponse;
                        if(response != null && response.data != null){
                            switch(response.statusCode){
                                case 400: {
                                    json = "Scanned KNR Number Not Present in Database!";
                                    break;
                                }
                            }
                            //Additional cases
                        }
                        alertDialogBuilder.setMessage(json);
                        alertDialogBuilder.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        rfidLocalHandler.onDestroy();
                                        Intent intent = new Intent(PinVinnActivity.this, ScanCompareTagActivity.class);
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
    }

    @Override
    public void handleTagdata(TagData[] tagData) {
        try{
            Thread.sleep(2000);
            if(counter < 2) {
                final StringBuilder sb = new StringBuilder();
                sb.setLength(0);
                for (int index = 0; index < 1; index++) {
                    sb.append(tagData[index].getTagID());
                }
//        tagPinNumberValue.setText(sb.toString());
                runOnUiThread(new Runnable() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void run() {
                        if (rfidLocalHandler != null && rfidLocalHandler.reader != null)
                        {
                            String vinnumber = vinNumberValue.getText().toString();
                            String TIDData = readTIDData(sb.toString());
                            if(!TIDData.contains("NA") || TIDData.length() > 0){
                                tagPinNumberValue.setText(TIDData);
                                String readUserDataValue = readUserData(sb.toString(),vinnumber);
                                String[] separated = readUserDataValue.split(";");
                                String result = separated[0].replaceAll(" ", "");
                                result = result.replaceAll("\\p{C}", "");
                                if(result.length() == 0) {
                                    result = "No Data in Tag";
                                    rfid_user_memory_value.setText(result);
                                }
                                else {
                                    if(result.length() > 18){
                                        result = result.substring(4, result.length());
                                    }
                                    else {
                                        result = result.substring(1, result.length());
                                    }
                                    rfid_user_memory_value.setText(result);
                                }

                                resultView.setText(separated[1]);

                                if (separated[1].contains("Match")){
                                    updateDataCP8();
                                    resultView.setTextColor(Color.parseColor("#07bc0c"));
                                }else{
                                    resultView.setTextColor(Color.parseColor("#FF0000"));
                                }
                            }
                            else {
                                tagPinNumberValue.setText("No Tag Read");
                            }
                        }

                    }
                });
            }
            else {
                runOnUiThread(new Runnable() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void run() {
                        showToast();
                    }
                });
            }
        }
        catch (Exception ex) {

        }
    }

    public void updateDataCP8() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String pinnumber = pinNumberValue.getText().toString();
        String vinnumber = vinNumberValue.getText().toString();
        String tagId = tagIdValue.getText().toString();

        @SuppressLint("WrongConstant") SharedPreferences sh = getSharedPreferences("MySharedPref",
                Context.MODE_APPEND);
        String URL = sh.getString("server_ip", "");
//            URL = URL + "/masters?pinNumber=" + pinnumber;
        URL = URL + "/rfidtagmasters?vinNumber=" + vinnumber + "&pinNumber=" + pinnumber + "&epcId=" + tagId;
        Log.d("Accessing URL -->", URL);

        JsonArrayRequest objectRequest = new JsonArrayRequest(
            Request.Method.GET, URL,null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if (response.length() > 0) {
                        dbContentValue.setVisibility(View.VISIBLE);
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;
                        CharSequence text = "Validation Completed";
                        Toast toast = Toast.makeText(context, text, duration);
                        View v = toast.getView();
                        v.getBackground().setColorFilter(Color.parseColor("#07bc0c"), PorterDuff.Mode.SRC_IN);
                        TextView t = v.findViewById(android.R.id.message);
                        t.setTextColor(Color.WHITE);
                        toast.show();
                    }
                    else {
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;
                        CharSequence text = "Validation Failed at Database. Please try again";
                        Toast toast = Toast.makeText(context, text, duration);
                        View v = toast.getView();
                        v.getBackground().setColorFilter(Color.parseColor("#07bc0c"), PorterDuff.Mode.SRC_IN);
                        TextView t = v.findViewById(android.R.id.message);
                        t.setTextColor(Color.WHITE);
                        toast.show();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    CharSequence text = "Validation Failed at Database. Please try again";
                    Toast toast = Toast.makeText(context, text, duration);
                    View v = toast.getView();
                    v.getBackground().setColorFilter(Color.parseColor("#07bc0c"), PorterDuff.Mode.SRC_IN);
                    TextView t = v.findViewById(android.R.id.message);
                    t.setTextColor(Color.WHITE);
                    toast.show();
                }
            }
        );
        requestQueue.add(objectRequest);
    }

    public void showToast() {
        Toast.makeText(this, Html.fromHtml("<font color='#ff4500' ><b> Please ensure only one tag is in the Vicinity and Try Again </b></font>"), Toast.LENGTH_SHORT).show();
    }

    public String readTIDData(String tagId){
        if(rfidLocalHandler != null && rfidLocalHandler.reader != null){
//            rfidPerformInevntoryClass.readRFIDTagData(rfidLocalHandler);
        }
        else {
            rfidLocalHandler.onDestroy();
            rfidLocalHandler = new RFIDHandler(PinVinnActivity.this);
            rfidLocalHandler.onRFIDReadCreate(PinVinnActivity.this);
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
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tagData = rfidLocalHandler.reader.Actions.TagAccess.readWait(tagId, readAccessParams, null);
            if (tagData != null) {
                ACCESS_OPERATION_CODE readAccessOperation = tagData.getOpCode();
                if (readAccessOperation != null) {
                    if (tagData.getOpStatus() != null && !tagData.getOpStatus().equals(ACCESS_OPERATION_STATUS.ACCESS_SUCCESS)) {
                        String strErr = tagData.getOpStatus().toString().replaceAll("_", " ");
                        Toast.makeText(this, strErr, Toast.LENGTH_SHORT).show();
                        rfidTagIDValue = strErr.toString();

                    } else {
                        if (tagData.getOpCode() == ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ) {
                            rfidTagIDValue = tagData.getMemoryBankData();
                            rfidTagIDValue = rfidTagIDValue.substring(8, 24);
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

    public String readUserData(String tagId, String vinnumber){
        if(rfidLocalHandler != null && rfidLocalHandler.reader != null){
//            rfidPerformInevntoryClass.readRFIDTagData(rfidLocalHandler);
        }
        else {
            rfidLocalHandler.onDestroy();
            rfidLocalHandler = new RFIDHandler(PinVinnActivity.this);
            rfidLocalHandler.onPINReadCreate(PinVinnActivity.this);
        }
        String rfidTagPinNumberValue = "NA";
        String resultView = "NA";
        TagAccess tagAccess = new TagAccess();
        TagAccess.ReadAccessParams readAccessParams = tagAccess.new ReadAccessParams();
        TagData readAccessTag;
        readAccessParams.setAccessPassword(0);
        readAccessParams.setMemoryBank(MEMORY_BANK.MEMORY_BANK_USER);
        readAccessParams.setOffset(0);
        TagData tagData = null;
        try {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tagData = rfidLocalHandler.reader.Actions.TagAccess.readWait(tagId, readAccessParams, null);
            if (tagData != null) {
                ACCESS_OPERATION_CODE readAccessOperation = tagData.getOpCode();
                if (readAccessOperation != null) {
                    if (tagData.getOpStatus() != null && !tagData.getOpStatus().equals(ACCESS_OPERATION_STATUS.ACCESS_SUCCESS)) {
                        String strErr = tagData.getOpStatus().toString().replaceAll("_", " ");
                        Toast.makeText(this, strErr, Toast.LENGTH_SHORT).show();
                        rfidTagPinNumberValue = strErr.toString();

                    } else {
                        if (tagData.getOpCode() == ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ) {
                            String originalValue = tagData.getMemoryBankData();
                            String result = newHexConverstion.hexToString(originalValue);
                            rfidTagPinNumberValue = result;
                            if (result.contains(vinnumber)) {
                                if(tagIdValue.getText().toString().contains(tagPinNumberValue.getText().toString())){
                                    resultView = "Match";
                                }
                                else {
                                    resultView = "Not OK";
                                }
                            } else {
                                resultView = "Not OK";
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
        rfidTagPinNumberValue = rfidTagPinNumberValue.replaceAll(" ", "");
        rfidTagPinNumberValue = rfidTagPinNumberValue.replaceAll("\\p{C}", "");
        if(rfidTagPinNumberValue.length() <= 0 ){
            rfidTagPinNumberValue = "NA";
        }
        String sendResponse = rfidTagPinNumberValue + ";" + resultView;
        return sendResponse;
    }

    @Override
    public void handleTriggerPress(boolean pressed) {
        if(triggerPressed == true) {
            triggerPressed = false;
            counter = 0;
            if(rfidLocalHandler != null && rfidLocalHandler.reader != null){
                rfidPerformInevntoryClass.readRFIDTagData(rfidLocalHandler);
            }
            else {
                rfidLocalHandler.onDestroy();
                rfidLocalHandler = new RFIDHandler(PinVinnActivity.this);
                rfidLocalHandler.onPINReadCreate(PinVinnActivity.this);
            }
            triggerPressed = true;
        }
    }
    @Override
    public void handleRFIDStatus(String status) {
        readerStatusValue.setText(status);
        if (status == "Connected"){
            readerStatusValue.setTextColor(Color.parseColor("#07bc0c"));
        }else{
            readerStatusValue.setTextColor(Color.parseColor("#FF0000"));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        rfidLocalHandler.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        rfidLocalHandler.onResume();
//        String status = rfidLocalHandler.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rfidLocalHandler.onDestroy();

    }

    @Override
    public void updateCounter() {
        counter++;
    }
}