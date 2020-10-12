package com.zebra.rfid.demo.sdksample;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zebra.rfid.api3.ACCESS_OPERATION_CODE;
import com.zebra.rfid.api3.ACCESS_OPERATION_STATUS;
import com.zebra.rfid.api3.InvalidUsageException;
import com.zebra.rfid.api3.MEMORY_BANK;
import com.zebra.rfid.api3.OperationFailureException;
import com.zebra.rfid.api3.TagAccess;
import com.zebra.rfid.api3.TagData;
//import com.zebra.rfid.demo.sdksample.HomeActivity;
import com.zebra.rfid.demo.sdksample.R;
import com.zebra.rfid.demo.sdksample.RFIDHandler;

import java.io.StringWriter;

import static java.lang.Integer.parseInt;

public class ReadTagActivity extends AppCompatActivity implements RFIDHandler.ResponseHandlerInterface {

    EditText rfidTagScanView;
    EditText tagUserDataEditView;
    RFIDHandler rfidLocalHandler;
    TextView rfidReaderStatus;
    Boolean checkStatus = false;
    Button rfidTagReaderButton;
    Button clearButton;
    RFIDHandler rfidHandler;
    String vinRead;
    int counter = 0;
    boolean triggerPressed = true;

    RFIDPerformInevntoryClass rfidPerformInevntoryClass = new RFIDPerformInevntoryClass();
    hexConverstion newHexConverstion = new hexConverstion();
//    ProgressDialog progress = new ProgressDialog(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        rfidLocalHandler.onDestroy();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_tag);

        rfidLocalHandler = new RFIDHandler(this);
        rfidLocalHandler.onRFIDReadCreate(this);

        rfidTagReaderButton = findViewById(R.id.rfidTagReaderButton);
        clearButton = findViewById(R.id.clearButton);
        rfidReaderStatus = findViewById(R.id.rfidReaderStatus);
        tagUserDataEditView = findViewById(R.id.tagUserDataEditView);


        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.back);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.back:
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();

                        return true;
                    case R.id.home:
                        Intent intent2 = new Intent(getApplicationContext(), HomeActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return false;
            }
        });

        rfidTagScanView = findViewById(R.id.editRFIDText);
        rfidTagScanView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                    case EditorInfo.IME_ACTION_NEXT:
                    case EditorInfo.IME_ACTION_GO:
                    case EditorInfo.IME_ACTION_SEND:
                        Log.d("onClick ", v.getText().toString());
                        return true;
                }

                if (event.getAction() == KeyEvent.KEYCODE_ENTER) {

                    Log.d("onEditorAction ", v.getText().toString());

                    return true;
                }
                return false;
            }
        });

        rfidTagReaderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                rfidPerformInevntoryClass.readRFIDTagData(rfidLocalHandler);
                counter = 0;
                readRFID();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rfidTagScanView.setText("");
                tagUserDataEditView.setText("");
                rfidTagScanView.requestFocus();
            }
        });
    }

//    public void startLoader() {
//        progress.show(this,"","Please wait...");
//    }
//
//    public void stopLoader() {
//        progress.hide();
//    }

    @Override
    public void handleTagdata(TagData[] tagData) {
        try {
//            Thread.sleep(2000);
//            if(counter < 2) {
                final StringBuilder sb = new StringBuilder();
                sb.setLength(0);
                for (int index = 0; index < 1; index++) {
                    sb.append(tagData[index].getTagID());
                }
                Thread.sleep(2000);
                if (rfidLocalHandler != null && rfidLocalHandler.reader != null) {
                    String TIDData = readTIDData(sb.toString());
                    if(!TIDData.contains("NA") || TIDData.length() > 0){
                        rfidTagScanView.setText(TIDData);
                        String readUserDataValue = readUserData(sb.toString(), "");
                        String[] separated = readUserDataValue.split(";");
                        String result = separated[0].replaceAll(" ", "");
                        result = result.replaceAll("\\p{C}", "");
                        if(result.length() == 0) {
                            result = "No Data in Tag";
                            tagUserDataEditView.setText(result);
                        }
                        else {
                            //String displayResult = result.substring(4, result.length());
//                                if(result.length() == 18){
//                                    tagUserDataEditView.setText(result);
//                                }
                            if(result.length() > 18){
                                result = result.substring(4, result.length());
                            }
                            else {
                                result = result.substring(1, result.length());
                            }
                            tagUserDataEditView.setText(result);
                        }
                    }
                    else {
                        rfidTagScanView.setText("");
                        tagUserDataEditView.setText("");
                        rfidTagScanView.requestFocus();
                    }
                }
//                runOnUiThread(new Runnable() {
//                    @SuppressLint("LongLogTag")
//                    @Override
//                    public void run() {
//
//                        if (rfidLocalHandler != null && rfidLocalHandler.reader != null) {
//                            String TIDData = readTIDData(sb.toString());
//                            if(!TIDData.contains("NA") || TIDData.length() > 0){
//                                rfidTagScanView.setText(TIDData);
//                                String readUserDataValue = readUserData(sb.toString(), "");
//                                String[] separated = readUserDataValue.split(";");
//                                String result = separated[0].replaceAll(" ", "");
//                                result = result.replaceAll("\\p{C}", "");
//                                if(result.length() == 0) {
//                                    result = "No Data in Tag";
//                                    tagUserDataEditView.setText(result);
//                                }
//                                else {
//                                    //String displayResult = result.substring(4, result.length());
////                                if(result.length() == 18){
////                                    tagUserDataEditView.setText(result);
////                                }
//                                    if(result.length() > 18){
//                                        result = result.substring(4, result.length());
//                                    }
//                                    else {
//                                        result = result.substring(1, result.length());
//                                    }
//                                    tagUserDataEditView.setText(result);
//                                }
//                            }
//                            else {
//                                rfidTagScanView.setText("");
//                                tagUserDataEditView.setText("");
//                                rfidTagScanView.requestFocus();
//                            }
//                        }
//                    }
//                });
//            }
//            else {
//                runOnUiThread(new Runnable() {
//                    @SuppressLint("LongLogTag")
//                    @Override
//                    public void run() {
//                        showToast();
//                        counter = 0;
//                    }
//                });
//            }
        }
        catch (Exception ex) {

        }
    }

    public void showToast() {
        Toast.makeText(this, Html.fromHtml("<font color='#ff4500' ><b> Please ensure only one tag is in the Vicinity and Try Again </b></font>"), Toast.LENGTH_SHORT).show();
    }

    public String readUserData(String tagId, String vinnumber){
//        Toast.makeText(this, "Reading Tag User Memory", Toast.LENGTH_SHORT).show();
        if(rfidLocalHandler != null && rfidLocalHandler.reader != null){
//            rfidPerformInevntoryClass.readRFIDTagData(rfidLocalHandler);
        }
        else {
            rfidLocalHandler.onDestroy();
            rfidLocalHandler = new RFIDHandler(ReadTagActivity.this);
            rfidLocalHandler.onRFIDReadCreate(ReadTagActivity.this);
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
                                resultView = "OK";
                                ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 200);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD,150);
                            } else {
                                resultView = "NOK";
                                ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 200);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT,150);
                            }
                        }
                    }
                }
            }
//            progressDialog.dismiss();
        } catch (InvalidUsageException e) {
//            progressDialog.dismiss();
            e.printStackTrace();
        } catch (OperationFailureException e) {
//            progressDialog.dismiss();
            e.printStackTrace();
        }
        String sendResponse = rfidTagPinNumberValue + ";" + resultView;
        return sendResponse;
    }

    public String readTIDData(String tagId){
//        Toast.makeText(this, "Reading Tag ID", Toast.LENGTH_SHORT).show();
        if(rfidLocalHandler != null && rfidLocalHandler.reader != null){
//            rfidPerformInevntoryClass.readRFIDTagData(rfidLocalHandler);
        }
        else {
            rfidLocalHandler.onDestroy();
            rfidLocalHandler = new RFIDHandler(ReadTagActivity.this);
            rfidLocalHandler.onRFIDReadCreate(ReadTagActivity.this);
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
                            if(rfidTagIDValue.length() >= 8){
                                rfidTagIDValue = rfidTagIDValue.substring(8, 24);
                            }
//                            if(rfidTagIDValue == ""){
//                                rfidTagIDValue = "NA";
//                            }
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

    public void readRFID() {
//        ProgressDialog progressDialog = new ProgressDialog(ReadTagActivity.this);
//        progressDialog.setMessage("Reading RFID Tag");
//        progressDialog.show();
        synchronized (this){
            rfidPerformInevntoryClass.readRFIDTagData(rfidLocalHandler);
        }
//        progressDialog.dismiss();
//        progressDialog.cancel();
    }

    @Override
    public void handleTriggerPress(boolean pressed) {
        if(triggerPressed == true){
            triggerPressed = false;
            counter = 0;
            if(rfidLocalHandler != null && rfidLocalHandler.reader != null){
//            rfidPerformInevntoryClass.readRFIDTagData(rfidLocalHandler);
                this.runOnUiThread(this::readRFID);
            }
            else {
                rfidLocalHandler.onDestroy();
                rfidLocalHandler = new RFIDHandler(ReadTagActivity.this);
                rfidLocalHandler.onRFIDReadCreate(ReadTagActivity.this);
            }
            triggerPressed = true;
        }
    }

    @Override
    public void handleRFIDStatus(String status) {
        rfidReaderStatus.setText(status);
        if (status == "Connected"){
            rfidReaderStatus.setTextColor(Color.parseColor("#07bc0c"));
        }else{
            rfidReaderStatus.setTextColor(Color.parseColor("#FF0000"));
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
        String status = rfidLocalHandler.onResume();
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