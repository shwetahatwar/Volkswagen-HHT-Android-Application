package com.zebra.rfid.demo.sdksample;

public class ScanPinVin {

    private String pin;
    private String vin;
    private String tag;

    public ScanPinVin() {
    }

    public ScanPinVin(String pin, String vin, String tag) {
        this.pin = pin;
        this.vin = vin;
        this.tag = tag;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
