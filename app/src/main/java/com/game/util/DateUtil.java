package com.game.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static DateUtil instance() {
        return new DateUtil();
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
        if (System.currentTimeMillis() - SharePreferenceHelp.instance().popLong("starTime") > 48 * 60 * 60 * 1000) {
            return false;
        } else {
            return true;
        }
    }
}
