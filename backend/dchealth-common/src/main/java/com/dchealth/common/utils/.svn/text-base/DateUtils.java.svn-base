package com.dchealth.common.utils;

import java.lang.management.ManagementFactory;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 时间工具类
 * 
 * @author genemax
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	public static final String YYYY = "yyyy";

	public static final String YYYY_MM = "yyyy-MM";

	public static final String YYYY_MM_DD = "yyyy-MM-dd";

	public static final String MM_DD = "MM月dd日";

	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	private static final String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm:ss.S", "yyyy-MM-dd HH:mm", "yyyy-MM",
			"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss",
			"yyyy.MM.dd HH:mm", "yyyy.MM" };

	/**
	 * 获取当前Date型日期
	 * 
	 * @return Date() 当前日期
	 */
	public static Date getNowDate() {
		return new Date();
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
		long nd = 1000L * 24L * 60L * 60L;
		long nh = 1000L * 60L * 60L;
		long nm = 1000L * 60L;
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
			long nd = 1000L * 24L * 60L * 60L;
			long nh = 1000L * 60L * 60L;
			long nm = 1000L * 60L;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			// long ns = 1000;
			// 获得两个时间的毫秒时间差异
			long diff = calendar.getTime().getTime() - new Date().getTime();
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
			if(diff >0 ) {
			return diff;
			}
		}
		return 0;
	}

	public static boolean isDateBetweenTime(String time, int min) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long currentTime = System.currentTimeMillis();
		// 从对象中拿到时间
		long createTime = df.parse(time).getTime();

		Long diff = (currentTime - createTime) / 1000 / 60;
		// 比当前时间还要后，则无效
		if (diff < 0) {
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
	     * @param date
	     * @return
	     */
	    public static String getTimeStr(Date date, String pattern) {
	        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
	        return dateFormat.format(date);
	    }

	    /**
	     * 判断时间字符串是否为默认格式
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
	     * 增加 LocalDateTime ==> Date
	     */
	    public static Date toDate(LocalDateTime temporalAccessor)
	    {
	        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
	        return Date.from(zdt.toInstant());
	    }

	    /**
	     * 增加 LocalDate ==> Date
	     */
	    public static Date toDate(LocalDate temporalAccessor)
	    {
	        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
	        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
	        return Date.from(zdt.toInstant());
	    }

	/**
	 * 根据秒数获取时间串
	 *
	 * @param second (eg: 100s)
	 * @return (eg : 00 : 01 : 40)
	 */
	public static String getTimeStrBySecond(long second) {

		long day = second / 60 / 60 / 24;
		long hour = second / 60 / 60 % 24;
		long minute = second / 60 % 60;
		long seconds = second % 60;

		StringBuilder sb = new StringBuilder();
		if (day != 0) {
			sb.append(day).append("天");
		}
		if (hour != 0) {
			sb.append(hour).append("小时");
		}
		if (minute != 0) {
			sb.append(minute).append("分");
		}
		if (seconds != 0) {
			sb.append(seconds).append("秒");
		}
		return sb.toString();
	}

	public static int getExpire(int expire, String unit) {
		int e = expire;
		if ("seconds".equalsIgnoreCase(unit)) {
			e = expire;
		}
		if ("minute".equalsIgnoreCase(unit)) {
			e = expire * 60;
		}
		if ("hours".equalsIgnoreCase(unit)) {
			e = expire * 60 * 60;
		}
		if ("days".equalsIgnoreCase(unit)) {
			e = (expire * 60 * 60) * 24;
		}
		return e;
	}

	/**
	 * 获取N天前的日期
	 *
	 * @return
	 */
	public static final Date getPreDate(int dateNum) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getNowDate());
		cal.add(Calendar.DATE, -dateNum);
		return cal.getTime();
	}

	/**
	 * 判断时间是否在指定区域
	 * @param time 判定时间
	 * @param rangeStart 区域开始
	 * @param rangeEnd 区域结束
	 * @return 判定结果
	 */
	public static boolean isTimeBetweenRange(Date time, Date rangeStart, Date rangeEnd) {
		if (Objects.isNull(time) || Objects.isNull(rangeStart) || Objects.isNull(rangeEnd))
			return false;
		if ((time.after(rangeStart) && time.before(rangeEnd)) || isSameDay(time, rangeStart) || isSameDay(time, rangeEnd))
			return true;
		return false;
	}

	public static Time getTimeFromDate(String time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		Date d = null;
		try {
			d = format.parse(time);
			return new Time(d.getTime());
		} catch (ParseException e) {
			return null;
		}
	}
}
