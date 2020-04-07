package com.ruoyi.web.controller.tool;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QxTool {

    public static String getCurrDate() {
        Date date = new Date();
        DateFormat df = DateFormat.getDateTimeInstance();
        return df.format(date);
    }

    public static String getDate(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date());
    }

    public static String getOrderNum(String deviceId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddSSS");
        return simpleDateFormat.format(new Date()) + getRandom(10, 99) + deviceId;
    }

    public static int getRandom(int start, int end) {
        return (int) (Math.random() * (end - start + 1) + start);
    }

    // 时间戳转换日期
    public static String stampToTime(int stamp) {
        String sd = "";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sd = sdf.format(new Date(stamp));
        return sd;
    }

    public static String stampToTimeInSql(String stamp) {
        return " ltrim(rtrim(convert(char,DATEADD(s," + stamp + " + 8 * 3600,'1970-01-01 00:00:00'),120))) ";
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @param
     * @return
     */
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }


    /**
     * 取得当前时间戳（精确到秒）
     *
     * @return
     */
    public static String timeStamp() {
        long time = System.currentTimeMillis();
        String t = String.valueOf(time / 1000);
        return t;
    }

}
