package com.dchealth.cro.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dchealth.cro.common.reflect.LocaleUtil;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间工具类
 *
 * @author genemax
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    private static final Logger L = LoggerFactory.getLogger(DateUtils.class);
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static final String MM_DD = "MM月dd日";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static String YYYYMMDD = "yyyyMMdd";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final int CALENDAR_WEEK_OF_MONTH = 4;
    public static final int CALENDAR_MONTH = 2;
    public static final int CALENDAR_DATE = 5;
    
    public static final int SECONDS_PER_MINUTE = 60;
    public static final int MINUTES_PER_HOUR = 60;
    public static final int HOURS_PER_DAY = 24;
    public static final int SECONDS_PER_DAY = (HOURS_PER_DAY * MINUTES_PER_HOUR * SECONDS_PER_MINUTE);

    public static final long   DAY_MILLISECONDS = SECONDS_PER_DAY * 1000L;

    private static String[] parsePatterns = {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss",
            "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String getGBKTime() {
        return dateTimeNow("yyyy年MM月dd日");
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        if(date == null){
            return "";
        }
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String dateTimeDays(final Date date) {
        return parseDateToStr(MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final String parseDateToStrFull(final Date date) {
        return new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).format(date);
    }

    public static final String parseDateToStrShort(Date date, String parseFormat) {
        return new SimpleDateFormat(parseFormat).format(date);
    }

    public static final String parseDateToStrShort(final Date date) {
        return new SimpleDateFormat(YYYY_MM_DD).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String dateTimeToFormatStr(final String format, final String ts) {
        try {
            Date date = new SimpleDateFormat(format).parse(ts);
            return new SimpleDateFormat(format).format(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    public static final String dateNowTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, YYYY_MM_DD);
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date dateRoll(Date date, int i, int d) {
        // 获取Calendar对象并以传进来的时间为准
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 将现在的时间滚动固定时长,转换为Date类型赋值
        calendar.add(i, d);
        // 转换为Date类型再赋值
        date = calendar.getTime();
        return date;
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 计算两个时间差
     */
    public static String getConsultShowTime(Date date) {
        if (date != null) {
            long nd = 1000 * 24 * 60 * 60;
            long nh = 1000 * 60 * 60;
            long nm = 1000 * 60;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            // long ns = 1000;
            // 获得两个时间的毫秒时间差异
            long diff = calendar.getTime().getTime() - new Date().getTime();
            // 计算差多少天
            // 计算差多少小时
            long hour = diff % nd / nh;
            // 计算差多少分钟
            long min = diff % nd % nh / nm;
            // 计算差多少秒//输出结果
            // long sec = diff % nd % nh % nm / ns;
            return hour + "小时" + min + "分";
        }
        return null;
    }

    /**
     * 计算两个时间差
     */
    public static String getConsultShowTimeThreeDays(Date date) {
        if (date != null) {
            long nd = 1000 * 24 * 60 * 60;
            long nh = 1000 * 60 * 60;
            long nm = 1000 * 60;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, 3);
            // long ns = 1000;
            // 获得两个时间的毫秒时间差异
            long diff = calendar.getTime().getTime() - new Date().getTime();
            // 计算差多少天
            long day = diff / nd;
            // 计算差多少小时
            long hour = diff % nd / nh;
            // 计算差多少分钟
            long min = diff % nd % nh / nm;
            // 计算差多少秒//输出结果
            // long sec = diff % nd % nh % nm / ns;
            if (day > 0) {
                return day + "天" + hour + "小时" + min + "分";
            } else {
                return hour + "小时" + min + "分";
            }
        }
        return null;
    }

    /**
     * 计算两个时间差
     */
    public static long getConsultFutherTime(Date date) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            // long ns = 1000;
            // 获得两个时间的毫秒时间差异
            long diff = calendar.getTime().getTime() - new Date().getTime();

            // 计算差多少秒//输出结果
            // long sec = diff % nd % nh % nm / ns;
            if (diff > 0) {
                return diff;
            }
        }
        return 0;
    }

    public static int getBetweenMonths(Date date) {
        if (date != null) {
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(new Date());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year2 = calendar2.get(Calendar.YEAR);
            int year = calendar.get(Calendar.YEAR);
            if (year2 < year) {
                return 0;
            }
            int month2 = calendar2.get(Calendar.MONTH);
            int month = calendar.get(Calendar.MONTH);
            return (year2 - year) * 12 + (month2 - month);
        }
        return 0;

    }

    public static boolean isDateBetweenTime(String time, int min) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long currentTime = System.currentTimeMillis();
        L.error("系统时间：" + df.format(new Date()));
        // 从对象中拿到时间
        long createTime = df.parse(time).getTime();

        Long diff = (currentTime - createTime) / 1000 / 60;
        // 比当前时间还要后，则无效
        if (diff < -1000) {
            return false;
        }
        // 超时
        int count = diff.intValue() - min;
        if (count > 0) {
            return false;
        }
        return true;

    }

    /**
     * 相差几年
     *
     * @param date
     * @return
     */
    public static String getBetweenYear(Date date) {
        // 当前日历时间
        Calendar currentCalendar = Calendar.getInstance();
        // 日历时间
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(date);
        // 当前时间单位
        int cYear = currentCalendar.get(Calendar.YEAR);
        int sYear = startCalendar.get(Calendar.YEAR);
        if ((cYear - sYear) < 150 && (cYear - sYear) >= 0) {
            return String.valueOf(cYear - sYear);
        }
        return null;

    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1, Date date2) {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
        return days;
    }

    /**
     * 将时间转换成显示的时间格式
     *
     * @param addTime
     * @return
     */
    public static String getShowTime(Date addTime) {
        // 当前日历时间
        Calendar currentCalendar = Calendar.getInstance();
        // 发布日历时间
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(addTime);
        // 当前时间单位
        int cYear = currentCalendar.get(Calendar.YEAR);
        int cMonth = currentCalendar.get(Calendar.MONTH);
        int cDate = currentCalendar.get(Calendar.DAY_OF_MONTH);
        int cHour = currentCalendar.get(Calendar.HOUR_OF_DAY);
        int cMinuter = currentCalendar.get(Calendar.MINUTE);
        // 发布时间单位
        int sYear = startCalendar.get(Calendar.YEAR);
        int sMonth = startCalendar.get(Calendar.MONTH);
        int sDate = startCalendar.get(Calendar.DAY_OF_MONTH);
        int sHour = startCalendar.get(Calendar.HOUR_OF_DAY);
        int sMinuter = startCalendar.get(Calendar.MINUTE);
        if (cYear > sYear) {
            return parseDateToStrFull(addTime);
        } else if (cMonth > sMonth) {
            return (cMonth - sMonth) > 3 ? parseDateToStrFull(addTime) : (cMonth - sMonth) + "个月前";
        } else if (cDate > sDate) {
            return (cDate - sDate) + "天前";
        } else if (cHour > sHour) {
            return (cHour - sHour) + "小时前";
        } else if (cMinuter >= sMinuter) {
            return (cMinuter - sMinuter) < 1 ? "刚刚" : (cMinuter - sMinuter) + "分钟前";
        }
        return "";
    }

    private static final String DEFAULT_CONVERT_PATTERN = "yyyyMMddHHmmssSSS";

    /**
     * 获取当前时间字符串(默认格式:yyyyMMddHHmmssSSS)
     *
     * @return
     */
    public static String getCurrentTimeStrDefault() {
        return getCurrentTimeStr(DEFAULT_CONVERT_PATTERN);
    }

    /**
     * 获取指定时间字符串(默认格式:yyyyMMddHHmmssSSS)
     *
     * @param date
     * @return
     */
    public static String getTimeStrDefault(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_CONVERT_PATTERN);
        return dateFormat.format(date);
    }

    /**
     * 获取当前时间字符串
     *
     * @param pattern 转换格式
     * @return
     */
    public static String getCurrentTimeStr(String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(new Date());
    }

    /**
     * 获取指定时间字符串
     *
     * @param date
     * @return
     */
    public static String getTimeStr(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     * 判断时间字符串是否为默认格式
     *
     * @param dateTimeStr
     * @return
     */
    public static boolean isValidDefaultFormat(String dateTimeStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_CONVERT_PATTERN);
        try {
            dateFormat.parse(dateTimeStr);
            return true;
        } catch (Exception e) {
            // 如果抛出异常，说明格式不正确
            return false;
        }
    }

    /**
     * 剩余分钟数
     *
     * @param date 结束点日期
     * @return 分钟数
     */
    public static final long minutesRemaining(Date date) {
        return (date.getTime() - System.currentTimeMillis()) / 1000 / 60;
    }

    /**
     * 剩余小时
     *
     * @param date 结束点日期
     * @return 小时数
     */
    public static final long remainingHours(Date date) {
        return minutesRemaining(date) / 60;
    }

    /**
     * 剩余天数
     *
     * @param date 结束点日期
     * @return 天数
     */
    public static final long remainingDays(Date date) {
        return remainingHours(date) / 24;
    }

    /***
     * 日期比较【comp1<comp2 ：return true】
     * @param comp1
     * @param comp2
     * @return
     */
    public static Boolean dateCompare(Date comp1, Date comp2) {
        return comp1.before(comp2);
    }

    /**
     * 传入date ,parttern,calendarUnit,数量
     *
     * @param date
     * @param pattern
     * @param calendarUnit
     * @param amount
     * @return 增加后的时间
     * @throws ParseException
     */
    public static Date addDateCalculate(Date date, String pattern, int calendarUnit, int amount) throws ParseException {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(calendarUnit, amount); // Calendar.WEEK_OF_MONTH
        return rightNow.getTime();
    }


    /**
     * 判断两个日期是否同一天
     */
    public static Boolean judgeTwoTimeIsOneDay(Date time1, Date time2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(time1).equals(fmt.format(time2));
    }
    
    public static Date getJavaDate(double date) {
        return getJavaDate(date, false, null, false);
    }
    
    public static Date getJavaDate(double date, boolean use1904windowing, TimeZone tz, boolean roundSeconds) {
        Calendar calendar = getJavaCalendar(date, use1904windowing, tz, roundSeconds);
        return calendar == null ? null : calendar.getTime();
    }
    
    public static Calendar getJavaCalendar(double date, boolean use1904windowing, TimeZone timeZone, boolean roundSeconds) {
        if (!isValidExcelDate(date)) {
            return null;
        }
        int wholeDays = (int)Math.floor(date);
        int millisecondsInDay = (int)((date - wholeDays) * DAY_MILLISECONDS + 0.5);
        Calendar calendar;
        if (timeZone != null) {
            calendar = LocaleUtil.getLocaleCalendar(timeZone);
        } else {
            calendar = LocaleUtil.getLocaleCalendar(); // using default time-zone
        }
        setCalendar(calendar, wholeDays, millisecondsInDay, use1904windowing, roundSeconds);
        return calendar;
    }
    
    
    public static boolean isValidExcelDate(double value)
    {
        return (value > -Double.MIN_VALUE);
    }
    
    
    public static void setCalendar(Calendar calendar, int wholeDays,
            int millisecondsInDay, boolean use1904windowing, boolean roundSeconds) {
        int startYear = 1900;
        int dayAdjust = -1; // Excel thinks 2/29/1900 is a valid date, which it isn't
        if (use1904windowing) {
            startYear = 1904;
            dayAdjust = 1; // 1904 date windowing uses 1/2/1904 as the first day
        }
        else if (wholeDays < 61) {
            // Date is prior to 3/1/1900, so adjust because Excel thinks 2/29/1900 exists
            // If Excel date == 2/29/1900, will become 3/1/1900 in Java representation
            dayAdjust = 0;
        }
        calendar.set(startYear,0, wholeDays + dayAdjust, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, millisecondsInDay);
        if (calendar.get(Calendar.MILLISECOND) == 0) {
            calendar.clear(Calendar.MILLISECOND);
        }
        if (roundSeconds) {
            calendar.add(Calendar.MILLISECOND, 500);
            calendar.clear(Calendar.MILLISECOND);
        }
    }


}
