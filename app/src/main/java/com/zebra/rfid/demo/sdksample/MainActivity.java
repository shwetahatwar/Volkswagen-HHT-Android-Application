package com.zebra.rfid.demo.sdksample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zebra.rfid.api3.ACCESS_OPERATION_CODE;
import com.zebra.rfid.api3.ACCESS_OPERATION_STATUS;
import com.zebra.rfid.api3.InvalidUsageException;
import com.zebra.rfid.api3.MEMORY_BANK;
import com.zebra.rfid.api3.OperationFailureException;
import com.zebra.rfid.api3.ReaderDevice;
import com.zebra.rfid.api3.Readers;
import com.zebra.rfid.api3.TagAccess;
import com.zebra.rfid.api3.TagData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements RFIDHandler.ResponseHandlerInterface {

    public TextView statusTextViewRFID = null;
    private TextView textViewBtnData;
    private TextView textrfid;
    private TextView textview5;
    private static Readers readers;
   // private static RFIDReader reader;
    private static ArrayList<ReaderDevice> availableRFIDReaderList;
    private static ReaderDevice readerDevice;
    TextView textRWData;
    private EditText offsetEditText;
    private EditText lengthEditText;

    Toolbar bottom_navigation;

    TextView readData;
    TextView mainpin;
    TextView mainvin;
    String st;
    String st1;
    String checkRFIDData;
    String vinData;
    String pinData;
    Button button2;
    String pin1;
    String vin1;
    String writeData,TagDataShow;

    String URL1 ="http://192.168.0.11:8000/masters" + mainvin;

    private Button mButton;
    TagData tagData = null;
    RFIDHandler rfidHandler;
    final static String TAG = "RFID_SAMPLE";
    boolean checkStatus= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textViewBtnData = findViewById(R.id.textViewBtnData);

        readData = findViewById(R.id.readData);
        mainpin = findViewById(R.id.mainpin);
        mainvin = findViewById(R.id.mainvin);
        st = getIntent().getExtras().getString("Value");
        vinData = getIntent().getExtras().getString("vin1");
        pinData = getIntent().getExtras().getString("pin1");
        readData.setText(st);
        mainpin.setText(pinData);
        mainvin.setText(vinData);

        textview5 = findViewById(R.id.textview5);
        writeData = getIntent().getExtras().getString("writeData", writeData);
        textview5.setText(writeData);

        RequestQueue queue = Volley.newRequestQueue(this);

        // UI
        statusTextViewRFID = findViewById(R.id.textStatus);
        textrfid = findViewById(R.id.textview5);

        // RFID Handler
        rfidHandler = new RFIDHandler();
        rfidHandler.onCreate(this);

        //Read data on rfid reader
        mButton = findViewById(R.id.button1);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                st1 = textview5.getText().toString();
                textViewBtnData.setText(st1);
                //st1 = textview5.getText().toString();
               // textrfid.setText("");
                // checkStatus = false;
                if (checkStatus == false) {
                    rfidHandler.performInventory();
                    rfidHandler.stopInventory();
                    checkStatus = true;
                    // st1 = textview5.getText().toString();

                }

                String url = "http://192.168.0.11:8000/masters/1";
                StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Response", response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override


                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.d("Error.Response", error.toString());
                            }
                        }
                ) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("vinNumber", "123");
                        params.put("pinNumber", "456");
                        params.put("epcId", "789");

                        return params;
                    }

                };

                queue.add(putRequest);

                String tagId = "3005FB63AC1F3681EC880468";
                TagAccess tagAccess = new TagAccess();
                TagAccess.ReadAccessParams readAccessParams = tagAccess.new ReadAccessParams();
                TagData readAccessTag; readAccessParams.setAccessPassword(0);
                // read 4 words
                readAccessParams.setCount(4);
                // user memory bank
                readAccessParams.setMemoryBank(MEMORY_BANK.MEMORY_BANK_USER);
                // start reading from word offset 0
                readAccessParams.setOffset(0);
                // read operation
                TagData tagData = null;
                try {
                    if (rfidHandler !=  null && rfidHandler.reader != null) {
                        tagData = rfidHandler.reader.Actions.TagAccess.readWait(tagId, readAccessParams, null);
                        Log.d("tagData",tagId);
                    }
                } catch (InvalidUsageException e) {
                    e.printStackTrace();
                } catch (OperationFailureException e) {
                    e.printStackTrace();
                }

                if (tagData != null) {
                    ACCESS_OPERATION_CODE readAccessOperation = tagData.getOpCode();
                    if (readAccessOperation != null) {
                        if (tagData.getOpStatus() != null && !tagData.getOpStatus().equals(ACCESS_OPERATION_STATUS.ACCESS_SUCCESS)) {
                            String strErr = tagData.getOpStatus().toString().replaceAll("_", " ");
                            // Toast.makeText(getActivity(), strErr.toLowerCase(), Toast.LENGTH_SHORT).show();
                        } else {
                            if (tagData.getOpCode() == ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ) {
                                //  Toast.makeText(getActivity(), R.string.msg_read_succeed + " "+tagData.getMemoryBankData(), Toast.LENGTH_SHORT).show();
                            } else {
                            }
                        }
                    } else {
                        // Toast.makeText(getActivity(), R.string.err_access_op_failed, Toast.LENGTH_SHORT).show();
                        //  Constants.logAsMessage(Constants.TYPE_DEBUG, "ACCESS READ", "memoryBankData is null");
                    }
                } else {
                    // Toast.makeText(getActivity(), R.string.err_access_op_failed, Toast.LENGTH_SHORT).show();
                }

                }
        });
    }

    private void writeTag() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        rfidHandler.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        String status = rfidHandler.onResume();
        statusTextViewRFID.setText(status);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rfidHandler.onDestroy();
    }


    @Override
    public void handleTagdata(TagData[] tagData) {
        final StringBuilder sb = new StringBuilder();
        for (int index = 0; index < tagData.length; index++) {
            sb.append(tagData[index].getTagID() + "\n");
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textrfid.append(sb.toString());
                checkRFIDData = textrfid.getText().toString();

                // showing read and write data in same screen inside MAINACTIVITY only
                //RFIDHandler fact = new RFIDHandler();
                Log.d("writetag", "button2");
                String EPC = checkRFIDData.toString();
                String data = vinData.toString();
                if (rfidHandler!= null) {
                    rfidHandler.writeTag(EPC, MEMORY_BANK.MEMORY_BANK_USER, data, 2);
                    //Display Data
                    rfidHandler.onDestroy();
                }
//pin vin and epcId Data display goes into WriteActivity
//                Intent i = new Intent(MainActivity.this,WriteActivity.class);
//                i.putExtra("epcId",checkRFIDData);
//                i.putExtra("vinId",vinData);
//                i.putExtra("pinId",pinData);
//                startActivity(i);
//                finish();
            }
        });
    }

    @Override
    public void handleTriggerPress(boolean pressed) {

    }
}
