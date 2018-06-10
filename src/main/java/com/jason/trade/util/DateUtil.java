package com.jason.trade.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    /**
     * Date类型转为指定格式的String类型
     *
     * @param source
     * @param pattern
     * @return
     */
    public static String DateToString(Date source, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(source);
    }
    public static String DateToString(Date source) {
        return DateToString(source,"yyyy-MM-dd");
    }
    public static String DateTimeToString(Date source) {
        return DateToString(source,"yyyy-MM-dd HH:mm:ss");
    }

    /**
     *
     * 字符串转换为对应日期
     *
     * @param source
     * @param pattern
     * @return
     */
    public static Date stringToDate(String source,String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date stringToDate(String source) {
        return stringToDate(source,"yyyy-MM-dd");
    }


    public static Date stringToDateTime(String source) {
        return stringToDate(source,"yyyy-MM-dd HH:mm:ss");
    }
}
