package com.pramaindia.role_management.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String formatDate(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        return sdf.format(date);
    }

    public static Date parseDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
            return sdf.parse(dateString);
        } catch (Exception e) {
            return null;
        }
    }
}
