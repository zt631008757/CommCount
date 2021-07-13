package com.android.baselibrary.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2018/8/15.
 * 时间工具类
 */

public class DateUtil {

    public static String DateToStr(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String time = "";
        try {
            time = formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    //比较两个日期 是否是同一天
    public static boolean isSameDay(String time1, String time2) {
        boolean isSameDay = false;
        SimpleDateFormat formatter_simple = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = formatter_simple.parse(time1);
            Date date2 = formatter_simple.parse(time2);
            if (date1.getTime() == date2.getTime()) {
                isSameDay = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSameDay;
    }

    //比较两个日期 是否是同一天
    public static boolean isSameDay(long time1, long time2) {
        boolean isSameDay = false;
        SimpleDateFormat formatter_simple = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String date1 = formatter_simple.format(new Date(time1));
            String date2 = formatter_simple.format(new Date(time2));
            if (date1.equals(date2)) {
                isSameDay = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSameDay;
    }


    //动态列表时间
    public static String getTimestampString(long time) {
        Date date = new Date(time);
        String timeStr;
        if (isSameDay(date)) {   //今天
            timeStr = "今天";
        } else if (isYesterday(date)) {   //昨天
            timeStr = "昨天";
        } else {   // 其他时间展示日期
            timeStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
        }
        return timeStr;
    }

    //是否是今天
    private static boolean isSameDay(Date var0) {
        Date var2 = new Date();
        return (var0.getYear() == var2.getYear() && var0.getMonth() == var2.getMonth() && var0.getDay() == var2.getDay());
    }

    //是否是昨天
    private static boolean isYesterday(Date var0) {
        Date var2 = new Date();
        return (var0.getYear() == var2.getYear() && var0.getMonth() == var2.getMonth() && var0.getDay() + 1 == var2.getDay());
    }

    //是否是今年
    private static boolean isThisyear(Date var0) {
        Date var2 = new Date();
        return (var0.getYear() == var2.getYear());
    }


    public static String strToFullStr(String strDate) {
        SimpleDateFormat formatter_simple = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter_full = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date strtodate = null;
        String str = "";
        try {
            strtodate = formatter_simple.parse(strDate);
            str = formatter_full.format(strtodate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    public static long strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date strtodate = null;
        long time = 0;
        try {
            strtodate = formatter.parse(strDate);
            time = strtodate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }


    public static String formatDateToMD(String str, String formaStr1, String formaStr2) {
        SimpleDateFormat sf1 = new SimpleDateFormat(formaStr1);
        SimpleDateFormat sf2 = new SimpleDateFormat(formaStr2);
        String formatStr = "";
        try {
            formatStr = sf2.format(sf1.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatStr;
    }

    public static String formatDateWith_T(String str) {
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatStr = "";
        try {
            formatStr = sf2.format(sf1.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatStr;
    }

    public static String getDateFromTimeLine(long timeLong) {
        if (timeLong == 0) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(timeLong));
    }

    public static String getDateFromTimeLine(long timeLong, String FormatStr) {
        if (timeLong == 0) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(FormatStr);
        return format.format(new Date(timeLong));
    }

    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        return hours > 0 ? String.format("%2d小时%2d分钟%2d秒", hours, minutes, seconds) : String.format("%2d分钟%2d秒", minutes, seconds);
    }
}
