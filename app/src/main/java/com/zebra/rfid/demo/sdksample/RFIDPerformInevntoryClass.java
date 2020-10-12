package com.zebra.rfid.demo.sdksample;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.zebra.rfid.api3.ACCESS_OPERATION_CODE;
import com.zebra.rfid.api3.ACCESS_OPERATION_STATUS;
import com.zebra.rfid.api3.InvalidUsageException;
import com.zebra.rfid.api3.MEMORY_BANK;
import com.zebra.rfid.api3.OperationFailureException;
import com.zebra.rfid.api3.TagAccess;
import com.zebra.rfid.api3.TagData;

import static java.lang.Integer.parseInt;

public class RFIDPerformInevntoryClass extends AppCompatActivity implements RFIDHandler.ResponseHandlerInterface{

    hexConverstion newHexConverstion = new hexConverstion();
    public void readRFIDTagData(RFIDHandler rfidHandler) {
        if (rfidHandler != null && rfidHandler.reader != null) {
            try{
                rfidHandler.performInventory();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
                rfidHandler.stopInventory();
            }
            catch (Exception ex){
//                int length = 3000;
//                Toast.makeText(this,ex.toString(), (int) length);
            }
        }
    }

    public String writeToTagDataFunction(RFIDHandler rfidHandler, String checkRFIDData, String vinData) {
        if (checkRFIDData!= null) {
            vinData = vinData.replace("\n","");
            String storeData = newHexConverstion.stringToHex(vinData);
            String response = rfidHandler.writeTag(checkRFIDData, MEMORY_BANK.MEMORY_BANK_USER, storeData, 2);
            rfidHandler.onDestroy();
            return response;
        }
        else {
            return "";
        }
    }

    @SuppressLint("LongLogTag")
    public String readUserData(RFIDHandler rfidHandler, String tagId, String vinnumber){
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
            if (rfidHandler !=  null && rfidHandler.reader != null) {
                tagData = rfidHandler.reader.Actions.TagAccess.readWait(tagId, readAccessParams, null);
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
//                                tagData.get
                                String result = newHexConverstion.hexToString(originalValue);
                                rfidTagPinNumberValue = result;
                                if (result.contains(vinnumber)) {
                                    resultView = "OK";
                                } else {
                                    resultView = "NOK";
                                }

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
        String sendResponse = rfidTagPinNumberValue + ";" + resultView;
        return sendResponse;
    }

    @Override
    public void handleTagdata(TagData[] tagData) {

    }

    @Override
    public void handleTriggerPress(boolean pressed) {

    }

    @Override
    public void handleRFIDStatus(String status) {

    }

    @Override
    public void updateCounter() {

    }

}
