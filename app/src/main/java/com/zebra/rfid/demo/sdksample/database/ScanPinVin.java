package com.zebra.rfid.demo.sdksample.database;

public class ScanPinVin {

//    public static final String TABLE_NAME = "scanpinvin";
//
//    public static final String COLUMN_ID = "id";
//    public static final String COLUMN_VIN_NO = "vinno";
//    public static final String COLUMN_PIN_NO = "pinno";
//    public static final String COLUMN_TAG = "tag";

    private int id;
    private String vinno;
    private String pinno;
    private String tag;


    // Create table SQL query
//    public static final String CREATE_TABLE =
//            "CREATE TABLE " + TABLE_NAME + "("
//                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                    + COLUMN_VIN_NO + " TEXT,"
//                    + COLUMN_PIN_NO + "TEXT,"
//                    + COLUMN_TAG + "TEXT"
//                    + ")";

    public ScanPinVin(String s, String vinno, String pinno, String tag) {
    }

    public ScanPinVin(int id, String vinno, String pinno) {
        this.id = id;
        this.vinno = vinno;
        this.pinno = pinno;
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVinno() {
        return vinno;
    }

    public void setVinno(String vinno) {
        this.vinno = vinno;
    }

    public String getPinno() {
        return pinno;
    }

    public void setPinno(String pinno) {
        this.pinno = pinno;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
