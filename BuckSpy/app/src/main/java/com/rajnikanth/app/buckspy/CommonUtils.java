package com.rajnikanth.app.buckspy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class CommonUtils {
    public static String mobileNumber = "";

    public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception ex) {
            ex.getMessage();
            return null;
        }
    }

    public static String getRowGUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static int stringToIntDefault(String A_strNumber, int A_dDefault) {
        int result;
        try {
            result = Integer.parseInt(A_strNumber);
        } catch (Exception ex) // do nothing
        {
            result = A_dDefault;
        }
        return result;
    }

    public static float stringToFloatDefault(String A_strNumber, float A_dDefault) {
        float result;
        try {
            result = Float.parseFloat(A_strNumber);
        } catch (Exception ex) // do nothing
        {
            result = A_dDefault;
        }
        return result;
    }
    public static double stringToDoubleDefault(String A_strNumber, double A_dDefault) {
        double result;
        try {
            result = Double.parseDouble(A_strNumber);
        } catch (Exception ex) // do nothing
        {
            result = A_dDefault;
        }
        return result;
    }
    public static String getMonth(int month) {
        String strmonth = "";
        switch (month) {
            case 1:
                strmonth = "January";
                break;
            case 2:
                strmonth = "February";
                break;
            case 3:
                strmonth = "March";
                break;
            case 4:
                strmonth = "April";
                break;
            case 5:
                strmonth = "May";
                break;
            case 6:
                strmonth = "June";
                break;
            case 7:
                strmonth = "July";
                break;
            case 8:
                strmonth = "August";
                break;
            case 9:
                strmonth = "September";
                break;
            case 10:
                strmonth = "October";
                break;
            case 11:
                strmonth = "November";
                break;
            case 12:
                strmonth = "December";
                break;
        }
        return strmonth;
    }
    public static String isNull(String data) {
        return data == null ? "" : data;
    }

}
