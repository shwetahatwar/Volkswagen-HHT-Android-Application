package com.zebra.rfid.demo.sdksample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zebra.rfid.api3.MEMORY_BANK;
import com.zebra.rfid.api3.RFIDReader;
import com.zebra.rfid.api3.ReaderDevice;
import com.zebra.rfid.api3.Readers;
import com.zebra.rfid.api3.TagData;

import java.util.ArrayList;

public class WriteActivity extends AppCompatActivity implements RFIDHandler1.ResponseHandlerInterface{

    TextView tv;
    TextView pinNumberDisplay;
    TextView vinNumberDisplay;
    String pinNumberD;
    String vinNumberD;
    String writeData;
    Button btn2;
    TextView tagNumber;
    String Display;

    private TextView textrfid;
    String checkRFIDData;


    private static Readers readers;
    private static RFIDReader reader;
    private static ArrayList<ReaderDevice> availableRFIDReaderList;
    private static ReaderDevice readerDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        tagNumber = findViewById(R.id.tagNumber);
        pinNumberDisplay = findViewById(R.id.pinNumberDisplay);
        vinNumberDisplay = findViewById(R.id.vinNumberDisplay);

        btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RFIDHandler1 fact = new RFIDHandler1();
                String data = vinNumberDisplay.getText().toString();
                String password = "0";
                Log.d("Data",data.toString());
                fact.writeTag(writeData, MEMORY_BANK.MEMORY_BANK_USER,data,2);
            }
        });

        tv = findViewById(R.id.tv);
        writeData = getIntent().getExtras().getString("epcId");
        tv.setText(writeData);

        pinNumberD = getIntent().getExtras().getString("pinId");
        pinNumberDisplay.setText(pinNumberD);


        vinNumberD = getIntent().getExtras().getString("vinId");
        vinNumberDisplay.setText(vinNumberD);

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
//                textrfid.append(sb.toString());
//                checkRFIDData = textrfid.getText().toString();
//
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
