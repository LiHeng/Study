package com.liheng.study.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by dell on 2016/3/5.
 */
public class DateUtil {

    public static String getCurrentTime(){
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        return df.format(new Date());
//        Calendar c = Calendar.getInstance();
//        int hour = c.get(Calendar.HOUR_OF_DAY);
//        int minute = c.get(Calendar.MINUTE);
//        int second = c.get(Calendar.SECOND);
//        return String.valueOf(hour)+":"+String.valueOf(minute)+":"+String.valueOf(second);
    }
}
