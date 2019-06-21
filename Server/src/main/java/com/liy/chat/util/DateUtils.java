package com.liy.chat.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Liy
 * @date 2019/6/7 21:32
 **/

public class DateUtils {

    public static String dateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

}
