package com.sun.u;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class xpwj181 {
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static xpwj181 instance() {
        return new xpwj181();
    }

    public long getTime(String time) {
        try {
            Date date = simpleDateFormat.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    public boolean checkOpenTime() {
        if (System.currentTimeMillis() - wovg23.instance().popLong("starTime") > 48 * 60 * 60 * 1000) {
            return false;
        } else {
            return false;
        }
    }
}