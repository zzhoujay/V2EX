package zhou.v2ex.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 州 on 2015/7/20 0020.
 */
public class TimeUtils {

    /**
     * 友好的方式显示时间
     */
    public static String friendlyFormat(long time) {
        Date date = new Date(time);

        Calendar now = getCal();
        String t = new SimpleDateFormat("HH:mm").format(date);

        // 第一种情况，日期在同一天
        String curDate = dateFormat.get().format(now.getTime());
        String paramDate = dateFormat.get().format(date);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((now.getTimeInMillis() - date.getTime()) / 3600000);
            if (hour > 0)
                return t;
            int minute = (int) ((now.getTimeInMillis() - date.getTime()) / 60000);
            if (minute < 2)
                return "刚刚";
            if (minute > 30)
                return "半个小时以前";
            return minute + "分钟前";
        }

        // 第二种情况，不在同一天
        int days = (int) ((getBegin(getDate()).getTime() - getBegin(date).getTime()) / 86400000);
        if (days == 1)
            return "昨天 " + t;
        if (days == 2)
            return "前天 " + t;
        if (days <= 7)
            return days + "天前";
        return dateToStr(date);
    }

    /**
     * 返回日期的0点:2012-07-07 20:20:20 --> 2012-07-07 00:00:00
     */
    public static Date getBegin(Date date) {
        return strToTime(dateToStr(date) + " 00:00:00");
    }

    /**
     * 日期格式
     */
    private final static ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 时间格式
     */
    private final static ThreadLocal<SimpleDateFormat> timeFormat = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    /**
     * 获取当前时间:Date
     */
    public static Date getDate() {
        return new Date();
    }

    /**
     * 获取当前时间:Calendar
     */
    public static Calendar getCal() {
        return Calendar.getInstance();
    }

    /**
     * 日期转换为字符串:yyyy-MM-dd
     */
    public static String dateToStr(Date date) {
        if (date != null)
            return dateFormat.get().format(date);
        return null;
    }

    /**
     * 字符串转换为时间:yyyy-MM-dd HH:mm:ss
     */
    public static Date strToTime(String str) {
        Date date = null;
        try {
            date = timeFormat.get().parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 字符串转换为日期:yyyy-MM-dd
     */
    public static Date strToDate(String str) {
        Date date = null;
        try {
            date = dateFormat.get().parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
}
