package com.zebra.rfid.demo.sdksample;

import static java.lang.Integer.parseInt;

public class hexConverstion {

    public static String stringToHex(String str) {
        str = str.replace("\n","");
        StringBuffer hex = new StringBuffer();
        for (char temp : str.toCharArray()) {
            int decimal = (int) temp;
            hex.append(Integer.toHexString(decimal));
        }
        return hex.toString();
    }

    public String hexToString(String hexStr) {
        String result = "";
        final StringBuilder sb = new StringBuilder();
        for (int i=0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i+2);
            sb.append((char) parseInt(str, 16));
        }
        result = sb.toString();
        return result;
    }
}
