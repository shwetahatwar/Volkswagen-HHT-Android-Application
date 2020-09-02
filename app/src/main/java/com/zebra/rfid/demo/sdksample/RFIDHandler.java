package com.zebra.rfid.demo.sdksample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zebra.ASCII_SDK.CONFIG_TYPE;
import com.zebra.ASCII_SDK.IMsg;
import com.zebra.rfid.api3.ACCESS_OPERATION_CODE;
import com.zebra.rfid.api3.ACCESS_OPERATION_STATUS;
import com.zebra.rfid.api3.Antennas;
import com.zebra.rfid.api3.ENUM_TRANSPORT;
import com.zebra.rfid.api3.ENUM_TRIGGER_MODE;
import com.zebra.rfid.api3.HANDHELD_TRIGGER_EVENT_TYPE;
import com.zebra.rfid.api3.INVENTORY_STATE;
import com.zebra.rfid.api3.InvalidUsageException;
import com.zebra.rfid.api3.MEMORY_BANK;
import com.zebra.rfid.api3.OperationFailureException;
import com.zebra.rfid.api3.RFIDReader;
import com.zebra.rfid.api3.ReaderDevice;
import com.zebra.rfid.api3.Readers;
import com.zebra.rfid.api3.RfidEventsListener;
import com.zebra.rfid.api3.RfidReadEvents;
import com.zebra.rfid.api3.RfidStatusEvents;
import com.zebra.rfid.api3.SESSION;
import com.zebra.rfid.api3.SL_FLAG;
import com.zebra.rfid.api3.START_TRIGGER_TYPE;
import com.zebra.rfid.api3.STATUS_EVENT_TYPE;
import com.zebra.rfid.api3.STOP_TRIGGER_TYPE;
import com.zebra.rfid.api3.TagAccess;
import com.zebra.rfid.api3.TagData;
import com.zebra.rfid.api3.TagDataArray;
import com.zebra.rfid.api3.TriggerInfo;
import java.util.ArrayList;

import static java.security.AccessController.getContext;

class RFIDHandler implements Readers.RFIDReaderEventHandler {

    final static String TAG = "RFID_SAMPLE";
    public static Object EventHandler;
    private static Readers readers;
    private static ArrayList<ReaderDevice> availableRFIDReaderList;
    private static ReaderDevice readerDevice;
    public static RFIDReader reader;
    private static EventHandler eventHandler;
    private static ResponseHandlerInterface context;
    private AppCompatActivity activityContext;

    private Integer MAX_POWER_CONFIG = 270;

    // In case of RFD8500 change reader name with intended device below from list of paired RFD8500
    String readername = "RFD8500123";
    Context ctx;
    RFIDHandler(Context ctx){
        this.ctx = ctx;
        @SuppressLint("WrongConstant") SharedPreferences sh = this.ctx.getSharedPreferences("MySharedPref",
                Context.MODE_APPEND);
        MAX_POWER = sh.getString("max_antena", "");
//        MAX_POWER =
        Log.d("Max Power -->", MAX_POWER);
    }

    String MAX_POWER = HomeActivity.antena;

//    public class MEMORY_BANK;

    void onCreate(MainActivity activity) {
        context = activity;
        activityContext = activity;
        InitSDK();
    }

    void onRFIDReadCreate(MainActivity mainActivity) {
        context = mainActivity;
        activityContext = mainActivity;
        InitSDK();
    }

    void onRFIDReadCreate(PinVinnActivity pinVinnActivity) {
        context = pinVinnActivity;
        activityContext = pinVinnActivity;
        InitSDK();
    }

    void onRFIDReadCreate(ReadTagActivity activity) {
        context = activity;
        activityContext = activity;
        InitSDK();
    }

    void onPINReadCreate(PinVinnActivity activity) {
        context = activity;
        activityContext = activity;
        InitSDK();
    }

    public synchronized void initialize() {
        if (readers == null || reader == null) {
            InitSDK();
        }
    }

    // following two tests are to try out different configurations features
    public String Test1() {
        // check reader connection
        if (!isReaderConnected())
            return "Not connected";
        // set antenna configurations - reducing power to 200
        try {
            Antennas.AntennaRfConfig config = null;
            config = reader.Config.Antennas.getAntennaRfConfig(1);
            config.setTransmitPowerIndex(100);
            config.setrfModeTableIndex(0);
            config.setTari(0);
            reader.Config.Antennas.setAntennaRfConfig(1, config);
        } catch (InvalidUsageException e) {
            e.printStackTrace();
        } catch (OperationFailureException e) {
            e.printStackTrace();
            return e.getResults().toString() + " " + e.getVendorMessage();
        }
        return "Antenna power Set to 220";
    }

    public String Test2() {
        // check reader connection
        if (!isReaderConnected())
            return "Not connected";
        // Set the singulation control to S2 which will read each tag once only
        try {
            Antennas.SingulationControl s1_singulationControl = reader.Config.Antennas.getSingulationControl(1);
            s1_singulationControl.setSession(SESSION.SESSION_S2);
            s1_singulationControl.Action.setInventoryState(INVENTORY_STATE.INVENTORY_STATE_A);
            s1_singulationControl.Action.setSLFlag(SL_FLAG.SL_ALL);
            reader.Config.Antennas.setSingulationControl(1, s1_singulationControl);
        } catch (InvalidUsageException e) {
            e.printStackTrace();
        } catch (OperationFailureException e) {
            e.printStackTrace();
            return e.getResults().toString() + " " + e.getVendorMessage();
        }
        return "Session set to S2";
    }

    public String Defaults() {
        // check reader connection
        if (!isReaderConnected())
            return "Not connected";;
        try {
            // Power to 270
            Antennas.AntennaRfConfig config = null;
            config = reader.Config.Antennas.getAntennaRfConfig(1);
            Log.d("MAX_POWER-->", MAX_POWER);
            config.setTransmitPowerIndex(Integer.parseInt(MAX_POWER));
            config.setrfModeTableIndex(0);
            config.setTari(0);
            reader.Config.Antennas.setAntennaRfConfig(1, config);
            // singulation to S0
            Antennas.SingulationControl s1_singulationControl = reader.Config.Antennas.getSingulationControl(1);
            s1_singulationControl.setSession(SESSION.SESSION_S0);
            s1_singulationControl.Action.setInventoryState(INVENTORY_STATE.INVENTORY_STATE_A);
            s1_singulationControl.Action.setSLFlag(SL_FLAG.SL_ALL);
            reader.Config.Antennas.setSingulationControl(1, s1_singulationControl);
        } catch (InvalidUsageException e) {
            e.printStackTrace();
        } catch (OperationFailureException e) {
            e.printStackTrace();
            return e.getResults().toString() + " " + e.getVendorMessage();
        }
        return "Default settings applied";
    }

    public boolean isReaderConnected() {
        if (reader != null && reader.isConnected())
            return true;
        else {
            Log.d(TAG, "Reader is not connected");
            return false;
        }
    }

    public String writeTag(String sourceEPC, MEMORY_BANK memory_bank, String targetData, int offset) {
        try {
            TagData tagData = null;
            String tagId = sourceEPC;
            TagAccess tagAccess = new TagAccess();
            TagAccess.WriteAccessParams writeAccessParams = tagAccess.new WriteAccessParams();
            String writeData = targetData; //write data in string
            writeAccessParams.setMemoryBank(memory_bank);
            writeAccessParams.setOffset(offset); // start writing from word offset 0
            writeAccessParams.setWriteData(writeData);
            writeAccessParams.setWriteRetries(3);
            writeAccessParams.setWriteDataLength(writeData.length() / 4);
            boolean useTIDfilter = memory_bank == MEMORY_BANK.MEMORY_BANK_EPC;
            Log.d("Params to write - ", String.valueOf(writeAccessParams));
            Log.d("Params to tagData - ", String.valueOf(tagData));
            reader.Actions.TagAccess.writeWait(tagId, writeAccessParams, null, tagData, true, useTIDfilter);
            Log.d(TAG, "inside writetag data");
            return "Written Successfully";
        } catch (InvalidUsageException e) {
            e.printStackTrace();
            return "Failed to write";
        } catch (OperationFailureException e) {
            Log.d(TAG,e.toString());
            e.printStackTrace();
            return "Failed to write";
        }
    }

    String onResume() {
        return connect();
    }

    void onPause() {
        disconnect();
    }

    void onDestroy() {
        dispose();
    }

    public void InitSDK() {
        Log.d(TAG, "InitSDK ");
        Log.d("readers", String.valueOf(readers));
        if (readers == null) {
            new CreateInstanceTask().execute();
        } else
            new ConnectionTask().execute();
    }




    // Enumerates SDK based on host device
    private class CreateInstanceTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "CreateInstanceTask");
            // Based on support available on host device choose the reader type
            InvalidUsageException invalidUsageException = null;
            try {
                readers = new Readers(activityContext, ENUM_TRANSPORT.SERVICE_SERIAL);
                availableRFIDReaderList = readers.GetAvailableRFIDReaderList();
            } catch (InvalidUsageException e) {
                e.printStackTrace();
                invalidUsageException = e;
            }
            if (invalidUsageException != null) {
                readers.Dispose();
                readers = null;
                if (readers == null) {
                    readers = new Readers(activityContext, ENUM_TRANSPORT.BLUETOOTH);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new ConnectionTask().execute();
        }
    }

    private class ConnectionTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            Log.d(TAG, "ConnectionTask");
            GetAvailableReader();
            if (reader != null)
                return connect();
            return "Failed to find or connect reader";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            context.handleRFIDStatus(result);
        }
    }

    private synchronized void GetAvailableReader() {
        Log.d(TAG, "GetAvailableReader");
        try {
            if (readers != null) {
                readers.attach(this);
                if (readers.GetAvailableRFIDReaderList() != null) {
                    availableRFIDReaderList = readers.GetAvailableRFIDReaderList();
                    if (availableRFIDReaderList.size() != 0) {
                        // if single reader is available then connect it
                        if (availableRFIDReaderList.size() == 1) {
                            readerDevice = availableRFIDReaderList.get(0);
                            reader = readerDevice.getRFIDReader();
                        } else {
                            // search reader specified by name
                            for (ReaderDevice device : availableRFIDReaderList) {
                                if (device.getName().equals(readername)) {
                                    readerDevice = device;
                                    reader = readerDevice.getRFIDReader();
                                }
                            }
                        }
                    }
                }
            }
        } catch (InvalidUsageException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void RFIDReaderAppeared(ReaderDevice readerDevice) {
        Log.d(TAG, "RFIDReaderAppeared " + readerDevice.getName());
        new ConnectionTask().execute();
    }

    @Override
    public void RFIDReaderDisappeared(ReaderDevice readerDevice) {
        Log.d(TAG, "RFIDReaderDisappeared " + readerDevice.getName());
        if (readerDevice.getName().equals(reader.getHostName()))
            disconnect();
    }


    private synchronized String connect() {
        if (reader != null) {
            Log.d(TAG, "connect " + reader.getHostName());
            try {
                if (!reader.isConnected()) {
                    // Establish connection to the RFID Reader
                    reader.connect();
                    ConfigureReader();
                    return "Connected";
                }
            } catch (InvalidUsageException e) {
                e.printStackTrace();
            } catch (OperationFailureException e) {
                e.printStackTrace();
                Log.d(TAG, "OperationFailureException " + e.getVendorMessage());
                String des = e.getResults().toString();
                return "Connection failed" + e.getVendorMessage() + " " + des;
            }
        }
        return "";
    }

    private void ConfigureReader() {
        Log.d(TAG, "ConfigureReader " + reader.getHostName());
        if (reader.isConnected()) {
//            TriggerInfo triggerInfo = new TriggerInfo();
//            triggerInfo.StartTrigger.setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE);
//            triggerInfo.StopTrigger.setTriggerType(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_IMMEDIATE);
            try {
                // receive events from reader
                if (eventHandler == null)
                    eventHandler = new EventHandler();
                reader.Events.addEventsListener(eventHandler);
                // HH event
                reader.Events.setHandheldEvent(true);
                // tag event with tag data
                reader.Events.setTagReadEvent(true);
                reader.Events.setAttachTagDataWithReadEvent(false);
                // set trigger mode as rfid so scanner beam will not come
                // reader.Config.setTriggerMode(ENUM_TRIGGER_MODE.RFID_MODE, false);
                // set start and stop triggers

//                reader.Config.setStartTrigger(triggerInfo.StartTrigger);
//                reader.Config.setStopTrigger(triggerInfo.StopTrigger);

                // power levels are index based so maximum power supported get the last one
                MAX_POWER_CONFIG = reader.ReaderCapabilities.getTransmitPowerLevelValues().length - 1;
                if (MAX_POWER != null){
                    MAX_POWER_CONFIG = Integer.parseInt(MAX_POWER);
                }
                Log.d("MAX_POWER_CONFIG 2->", String.valueOf(MAX_POWER_CONFIG));
                Log.d("User Antena power 2", String.valueOf(MAX_POWER));

                // set antenna configurations
                Antennas.AntennaRfConfig config = reader.Config.Antennas.getAntennaRfConfig(1);
                config.setTransmitPowerIndex(MAX_POWER_CONFIG);
                config.setrfModeTableIndex(0);
                config.setTari(0);

                reader.Config.Antennas.setAntennaRfConfig(1, config);
                // Set the singulation control
                Antennas.SingulationControl s1_singulationControl = reader.Config.Antennas.getSingulationControl(1);
                s1_singulationControl.setSession(SESSION.SESSION_S0);
                s1_singulationControl.Action.setInventoryState(INVENTORY_STATE.INVENTORY_STATE_A);
                s1_singulationControl.Action.setSLFlag(SL_FLAG.SL_ALL);
                reader.Config.Antennas.setSingulationControl(1, s1_singulationControl);

                // delete any prefilters
                reader.Actions.PreFilters.deleteAll();
                //
            } catch (InvalidUsageException | OperationFailureException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void disconnect() {
        Log.d(TAG, "disconnect " + reader);
        try {
            Log.d(TAG,"inside rfid handler java");
            if (reader != null) {
                reader.Events.removeEventsListener(eventHandler);
                reader.disconnect();
                activityContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        context.handleRFIDStatus("Disconnected");
//                        textView.setText("Disconnected");
                    }
                });
            }
        } catch (InvalidUsageException e) {
            e.printStackTrace();
        } catch (OperationFailureException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void dispose() {
        try {
            if (readers != null) {
                reader = null;
                readers.Dispose();
                readers = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized void performInventory() {
        // check reader connection
        if (!isReaderConnected())
            return;
        try {
            reader.Config.setTriggerMode(ENUM_TRIGGER_MODE.RFID_MODE, false);
            reader.Actions.Inventory.perform();
//            reader.Actions.Inventory.stop();
            reader.Config.setTriggerMode(ENUM_TRIGGER_MODE.BARCODE_MODE, false);
        } catch (InvalidUsageException e) {
//            int length = 3000;
//            Toast.makeText(this.activityContext,e.toString(), (int) length);
            e.printStackTrace();
        } catch (OperationFailureException e) {
//            int length = 3000;
//            Toast.makeText(this.activityContext,e.toString(), (int) length);
            e.printStackTrace();
        }
    }

    synchronized void stopInventory() {
        // check reader connection
        if (!isReaderConnected())
            return;
        try {
            reader.Actions.Inventory.stop();
            reader.Config.setTriggerMode(ENUM_TRIGGER_MODE.BARCODE_MODE, false);
        } catch (InvalidUsageException e) {
//            int length = 3000;
//            Toast.makeText(this.activityContext,e.toString(), (int) length);
            e.printStackTrace();
        } catch (OperationFailureException e) {
//            int length = 3000;
//            Toast.makeText(this.activityContext,e.toString(), (int) length);
            e.printStackTrace();
        }
    }

    public class ToastMessege{
        public void makeToast(String msg) {
//            Toast toast = Toast.makeText(activityContext, msg, Toast.LENGTH_LONG);
//            toast.show();
        }
    }

    // Read/Status Notify handler
    // Implement the RfidEventsLister class to receive event notifications
    public class EventHandler implements RfidEventsListener {
        // Read Event Notification
        int count = 0;
        public void eventReadNotify(RfidReadEvents e) {
            // Recommended to use new method getReadTagsEx for better performance in case of large tag population
            TagDataArray myTags = reader.Actions.getReadTagsEx(10);
            context.updateCounter();
//            if (count == 2){
//                Log.d("inside if ->", String.valueOf(count));
//                new ToastMessege().makeToast("Multiple tag read by reader");
//                count = 0;
//                return;
//            }
            if (myTags != null) {
                for (int index = 0; index < 1; index++) {
                    Log.d(TAG, "Tag ID ---> " + myTags.getTags()[index].getTagID());
                    count = count + 1;
                    Log.d("counter 2", String.valueOf(count));
                    if (myTags.getTags()[index].getOpCode() == ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ &&
                            myTags.getTags()[index].getOpStatus() == ACCESS_OPERATION_STATUS.ACCESS_SUCCESS) {
                        if (myTags.getTags()[index].getMemoryBankData().length() > 0) {
                            Log.d(TAG, " Mem Bank Data " + myTags.getTags()[index].getMemoryBankData());
                            myTags.getTags()[index].getTagID();
                        }
                    }
                    if (myTags.getTags()[index].isContainsLocationInfo()) {
                        short dist = myTags.getTags()[index].LocationInfo.getRelativeDistance();
                        Log.d(TAG, "Tag relative distance " + dist);
                    }
                }
                // possibly if operation was invoked from async task and still busy
                // handle tag data responses on parallel thread thus THREAD_POOL_EXECUTOR
                new AsyncDataUpdate().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, myTags.getTags());
//                context.handleTagdata(myTags);
            }
        }

        // Status Event Notification
        public void eventStatusNotify(RfidStatusEvents rfidStatusEvents) {
            // Log.d(TAG, "Status Notification: " + rfidStatusEvents.StatusEventData.getStatusEventType());
            if (rfidStatusEvents.StatusEventData.getStatusEventType() == STATUS_EVENT_TYPE.HANDHELD_TRIGGER_EVENT) {
                if (rfidStatusEvents.StatusEventData.HandheldTriggerEventData.getHandheldEvent() == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_RELEASED) {
                    if (rfidStatusEvents.StatusEventData.HandheldTriggerEventData.getHandheldEvent() == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_PRESSED) {
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... voids) {
                                context.handleTriggerPress(true);
                                return null;
                            }
                        }.execute();
                    }
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            context.handleTriggerPress(false);
                            return null;
                        }
                    }.execute();
                }
            }
        }
    }

    static class AsyncDataUpdate extends AsyncTask<TagData[], Void, Void> {
        @Override
        protected Void doInBackground(TagData[]... params) {
            Log.d("---TagData[]->", String.valueOf(params));
            context.handleTagdata(params[0]);
            return null;
        }
    }

    interface ResponseHandlerInterface {
        void handleTagdata(TagData[] tagData);

        void handleTriggerPress(boolean pressed);

        void handleRFIDStatus(String status);

        void updateCounter();

    }

}
