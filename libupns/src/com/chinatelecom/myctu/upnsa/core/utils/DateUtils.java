package com.chinatelecom.myctu.upnsa.core.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期格式工具类
 * <p/>
 * User: snowway
 * Date: 2/19/13
 * Time: 4:27 PM
 */
public class DateUtils {

    public static final String FMT_DATE = "yyyy-MM-dd";

    public static final String FMT_TIME = "HH:mm:ss";

    public static final String FMT_DATETIME = FMT_DATE + " " + FMT_TIME;

    public static Date parse(String format, String date) {
        if (date == null || date.trim().equals("")) {
            return null;
        }
        DateFormat df = new SimpleDateFormat(format);
        try {
            return df.parse(date);
        } catch (ParseException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public static Date parseDate(String date) {
        return parse(FMT_DATE, date);
    }

    public static String format(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String formatDate(Date date) {
        return format(date, FMT_DATE);
    }

    public static String formatTime(Date date) {
        return format(date, FMT_TIME);
    }

    public static String formatDateTime(Date date) {
        return format(date, FMT_DATETIME);
    }


    public static Calendar createCalendar(String format, String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parse(format, date));
        return calendar;
    }

    public static Calendar createCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 比较两个日期是否是同一天
     *
     * @param left  左边的日期
     * @param right 右边的日期
     * @return 是否同一天
     */
    public static boolean isSameDay(Date left, Date right) {
        Calendar c1 = createCalendar(left);
        Calendar c2 = createCalendar(right);
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                && c1.get(Calendar.DATE) == c2.get(Calendar.DATE);
    }

    /**
     * 比较两个日期是否是同一周
     *
     * @param left  左边的日期
     * @param right 右边的日期
     * @return 是否同一天
     */
    public static boolean isSameWeek(Date left, Date right) {
        Calendar c1 = createCalendar(left);
        Calendar c2 = createCalendar(right);
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.WEEK_OF_MONTH) == c2.get(Calendar.WEEK_OF_MONTH);
    }

    /**
     * 比较两个日期是否是同一月
     *
     * @param left  左边的日期
     * @param right 右边的日期
     * @return 是否同一天
     */
    public static boolean isSameMonth(Date left, Date right) {
        Calendar c1 = createCalendar(left);
        Calendar c2 = createCalendar(right);
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH);
    }
}
