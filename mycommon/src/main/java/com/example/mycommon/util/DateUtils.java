package com.example.mycommon.util;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bt on 2017/8/30.
 */

public class DateUtils {
    public static boolean compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return false;
            } else if (dt1.getTime() < dt2.getTime()) {
                return true;
            } else {
                return true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    //获取当前日期
    public static String getStrTime_month_day() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        return df.format(new Date());
    }

    //获取当前日期-年
    public static String getStrTime_year() {
        DateFormat df = new SimpleDateFormat("yyyy");

        return df.format(new Date());
    }

    //获取当前日期-月
    public static String getStrTime_month() {
        DateFormat df = new SimpleDateFormat("MM");

        return df.format(new Date());
    }

    //获取当前日期-日
    public static String getStrTime_day() {
        DateFormat df = new SimpleDateFormat("dd");

        return df.format(new Date());
    }

    //时间戳转字符串
    public static String getStrTime_hh(String timeStamp){
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        long  l = Long.valueOf(timeStamp+"000");
        timeString = sdf.format(new Date(l));//单位秒
        return timeString;
    }

    //时间戳转字符串
    public static String getStrTime_dd(String timeStamp){
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        long  l = Long.valueOf(timeStamp+"000");
        timeString = sdf.format(new Date(l));//单位秒
        return timeString;
    }

    //判断是否在当月
    public static boolean isCurrentMonth(long timeStamp,String date){
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        timeString = sdf.format(new Date(timeStamp));//单位秒

        String month=date.substring(date.indexOf("-")+1,date.lastIndexOf("-"));

        if(timeString.equals(month)){
            return true;
        }
        return false;
    }

    //时间戳转字符串
    public static String getStrTime_yyyy(String timeStamp){
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long  l = Long.valueOf(timeStamp+"000");
        timeString = sdf.format(new Date(l));//单位秒
        return timeString;
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    public static int strYear(String strDate){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);

        long beginTime = strtodate.getTime()/1000;
        long endTime2 =System.currentTimeMillis()/1000;
        if(beginTime>endTime2){
            return -1;
        }
        int time= (int) (endTime2 - beginTime);
        int betweenDays = (int) (time / ( 60 * 60 *24*365)+ 1);
        return betweenDays;
    }
}
