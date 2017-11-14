package org.easystogu.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class WeekdayUtil {
	public static SimpleDateFormat sdf_yyyy = new SimpleDateFormat("yyyy");
	public static SimpleDateFormat sdf_MM = new SimpleDateFormat("MM");
	public static SimpleDateFormat sdf_dd = new SimpleDateFormat("dd");
	public static SimpleDateFormat sdf_yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat sdf_yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	public static SimpleDateFormat sdf_HHmmss = new SimpleDateFormat("HH-mm-ss");

	public static String currentDate() {
		return sdf_yyyyMMdd.format(new Date()).toString();
	}

	public static String currentDateTime() {
		return sdf_yyyyMMddHHmmss.format(new Date()).toString();
	}

	public static String currentTime() {
		return sdf_HHmmss.format(new Date()).toString();
	}

	public static int currentYear() {
		return Integer.parseInt(sdf_yyyy.format(new Date()).toString());
	}

	public static String currentDay() {
		return sdf_dd.format(new Date()).toString();
	}

	/**
	 * @title 判断两个日期是否在指定工作日内
	 * @detail (只计算周六和周日) 例如：前时间2008-12-05，后时间2008-12-11
	 * @author chanson
	 * @param beforeDate
	 *            前时间
	 * @param afterDate
	 *            后时间
	 * @param deadline
	 *            最多相隔时间
	 * @return 是的话，返回true，否则返回false
	 */
	public static boolean compareWeekday(String beforeDate, String afterDate, int deadline) {
		try {
			Date d1 = sdf_yyyyMMdd.parse(beforeDate);
			Date d2 = sdf_yyyyMMdd.parse(afterDate);

			// 工作日
			int workDay = 0;
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(d1);
			// 两个日期相差的天数
			long time = d2.getTime() - d1.getTime();
			long day = (time / 3600000 / 24) + 1;
			if (day < 0) {
				// 如果前日期大于后日期，将返回false
				return false;
			}
			for (int i = 0; i < day; i++) {
				if (isWeekday(gc)) {
					workDay++;
					// System.out.println(gc.getTime());
				}
				// 往后加1天
				gc.add(Calendar.DATE, 1);
			}
			return workDay <= deadline;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @title 判断是否为工作日
	 * @detail 工作日计算: 1、正常工作日，并且为非假期 2、周末被调整成工作日
	 * @author chanson
	 * @param date
	 *            日期
	 * @return 是工作日返回true，非工作日返回false
	 */
	public static boolean isWeekday(GregorianCalendar calendar) {
		if ((calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
				&& (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)) {
			// 平时
			return !getWeekdayIsHolidayList().contains(sdf_yyyyMMdd.format(calendar.getTime()));
		} else {
			// 周末
			return getWeekendIsWorkDateList().contains(sdf_yyyyMMdd.format(calendar.getTime()));
		}
	}

	/**
	 * @title 获取周六和周日是工作日的情况（手工维护） 注意，日期必须写全： 2009-1-4必须写成：2009-01-04
	 * @author chanson
	 * @return 周末是工作日的列表
	 */
	public static List<String> getWeekendIsWorkDateList() {
		List<String> list = new ArrayList<String>();
		// list.add("2009-01-04");
		return list;
	}

	/**
	 * @title 获取周一到周五是假期的情况（手工维护） 注意，日期必须写全： 2009-1-4必须写成：2009-01-04
	 * @author chanson
	 * @return 平时是假期的列表
	 */
	public static List<String> getWeekdayIsHolidayList() {
		List<String> list = new ArrayList<String>();
		// list.add("2009-01-29");
		return list;
	}

	// 返回某年第几周的所有工作日，年底交叉的分开，一年最多53周，
	public static List<String> getWorkingDaysOfWeek(int year, int week) {
		List<String> dates = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.WEEK_OF_YEAR, week);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		String date = sdf_yyyyMMdd.format(cal.getTime());
		if (date.startsWith(year + "")) {
			dates.add(sdf_yyyyMMdd.format(cal.getTime()));
		}
		cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		date = sdf_yyyyMMdd.format(cal.getTime());
		if (date.startsWith(year + "")) {
			dates.add(sdf_yyyyMMdd.format(cal.getTime()));
		}
		cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		date = sdf_yyyyMMdd.format(cal.getTime());
		if (date.startsWith(year + "")) {
			dates.add(sdf_yyyyMMdd.format(cal.getTime()));
		}
		cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		date = sdf_yyyyMMdd.format(cal.getTime());
		if (date.startsWith(year + "")) {
			dates.add(sdf_yyyyMMdd.format(cal.getTime()));
		}
		cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		date = sdf_yyyyMMdd.format(cal.getTime());
		if (date.startsWith(year + "")) {
			dates.add(sdf_yyyyMMdd.format(cal.getTime()));
		}

		return dates;
	}

	public static List<String> getCurrentWeekDates() {
		Calendar cal = Calendar.getInstance();
		int weekNumber = cal.get(Calendar.WEEK_OF_YEAR);
		return getWorkingDaysOfWeek(new Date().getYear() + 1900, weekNumber);
	}

	public static int getWeekNumber(String date) {
		Calendar cal = Calendar.getInstance();
		String[] ymd = date.split("-");
		cal.set(Calendar.YEAR, Integer.parseInt(ymd[0]));
		cal.set(Calendar.MONTH, Integer.parseInt(ymd[1]) - 1);
		cal.set(Calendar.DATE, Integer.parseInt(ymd[2]));
		int weekNumber = cal.get(Calendar.WEEK_OF_YEAR);

		if (Integer.parseInt(ymd[1]) == 12 && weekNumber == 1) {
			weekNumber = 53;
		}
		return weekNumber;
	}

	public static int getWeekNumber() {
		return getWeekNumber(currentDate());
	}

	// date is like: 2015-02-27
	// 返回某一日所在周的所有工作日
	public static List<String> getWeekWorkingDates(String date) {
		try {
			Calendar cal = Calendar.getInstance();
			String[] ymd = date.split("-");
			cal.set(Calendar.YEAR, Integer.parseInt(ymd[0]));
			cal.set(Calendar.MONTH, Integer.parseInt(ymd[1]) - 1);
			cal.set(Calendar.DATE, Integer.parseInt(ymd[2]));
			int weekNumber = cal.get(Calendar.WEEK_OF_YEAR);

			if (Integer.parseInt(ymd[1]) == 12 && weekNumber == 1) {
				weekNumber = 53;
			}

			return getWorkingDaysOfWeek(Integer.parseInt(ymd[0]), weekNumber);
		} catch (Exception e) {
			System.out.print("Exception for " + date);
			e.printStackTrace();
		}
		return null;
	}

	public static String nextWorkingDate(String today) {
		try {
			long minSecondsPerDay = 24 * 60 * 60 * 1000;
			// System.out.println("today is " + today);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calT = Calendar.getInstance();
			Date todayD = sdf.parse(today);
			Date nextWorkingD = null;
			// check if today is working day or weeken
			calT.setTime(todayD);
			int dayOfWeek = calT.get(Calendar.DAY_OF_WEEK);
			// System.out.println("dayOfWeek is " + dayOfWeek);

			// 周日~周四
			if (dayOfWeek >= 1 && dayOfWeek <= 5) {
				nextWorkingD = new Date(todayD.getTime() + 1 * minSecondsPerDay);
			} else if (dayOfWeek == 6) {
				// if friday
				nextWorkingD = new Date(todayD.getTime() + 3 * minSecondsPerDay);
			} else if (dayOfWeek == 7) {
				// if friday
				nextWorkingD = new Date(todayD.getTime() + 2 * minSecondsPerDay);
			}

			// System.out.println("todayD is " + todayD);
			// System.out.println("nextWorkingD is " + nextWorkingD);
			// System.out.println(sdf.format(nextWorkingD));
			return sdf.format(nextWorkingD);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static String nextDate(String today) {
		return nextNDateString(today, 1);
	}

	public static String nextNDateString(String today, int N) {
		if (N == 0)
			return today;
		try {
			long minSecondsPerDay = 24 * 60 * 60 * 1000;
			// System.out.println("today is " + today);
			Calendar calT = Calendar.getInstance();
			Date todayD = sdf_yyyyMMdd.parse(today);
			Date nextDate = null;
			// check if today is working day or weeken
			calT.setTime(todayD);

			nextDate = new Date(todayD.getTime() + N * minSecondsPerDay);

			// System.out.println("todayD is " + todayD);
			// System.out.println("nextWorkingD is " + nextWorkingD);
			// System.out.println(sdf.format(nextWorkingD));
			return sdf_yyyyMMdd.format(nextDate);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static Date nextNDate(String today, int N) {
		try {
			if (N == 0) {
				return sdf_yyyyMMdd.parse(today);
			}

			long minSecondsPerDay = 24 * 60 * 60 * 1000;
			// System.out.println("today is " + today);
			Calendar calT = Calendar.getInstance();
			Date todayD = sdf_yyyyMMdd.parse(today);
			Date nextDate = null;
			// check if today is working day or weeken
			calT.setTime(todayD);

			nextDate = new Date(todayD.getTime() + N * minSecondsPerDay);

			// System.out.println("todayD is " + todayD);
			// System.out.println("nextWorkingD is " + nextWorkingD);
			// System.out.println(sdf.format(nextWorkingD));
			return nextDate;

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 返回某日后若干个工作日
	public static List<String> nextWorkingDateList(String today, int length) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < length; i++) {
			String nextDate = nextWorkingDate(today);
			list.add(nextDate);
			today = nextDate;
		}
		return list;
	}

	// 返回某日后若干个工作日
	public static String nextNWorkingDate(String today, int N) {
		List<String> list = nextWorkingDateList(today, N);
		if (list != null && list.size() > 0) {
			String endDate = list.get(list.size() - 1);
			return endDate;
		}

		return "";
	}

	// 返回某日至今天的所有工作日,both inclusive
	public static List<String> getWorkingDateListSince(String fromDate) {
		List<String> list = new ArrayList<String>();
		String currDate = currentDate();
		String date = fromDate;
		while (date.compareTo(currDate) <= 0) {
			list.add(date);
			String nextDate = nextWorkingDate(date);
			date = nextDate;
		}
		return list;
	}

	// 判断date1和date2之间的时间跨距, 如果是10日之内，返回true
	public static boolean isDateBetweenNumberofDays(String date1, String date2, int len) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date1D = sdf.parse(date1);
			Date date2D = sdf.parse(date2);
			long date1L = date1D.getTime();
			long date2L = date2D.getTime();
			long lenDaysMilSecs = len * 24 * 60 * 60 * 1000;

			if ((Math.abs(date1L - date2L)) <= lenDaysMilSecs) {
				return true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	// return working date between start ~ end date, both inclusive
	public static List<String> getWorkingDatesBetween(String startDate, String endDate) {
		List<String> list = new ArrayList<String>();
		String curDate = startDate;
		while (curDate.compareTo(endDate) <= 0) {
			list.add(curDate);
			curDate = nextWorkingDate(curDate);
		}
		return list;
	}

	// return true if 09:30~15:00 at working date
	public static boolean isNowAtWorkingDayAndTransactionTime() {
		GregorianCalendar cal = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-ss");
		String timeStr = sdf.format(new Date()).toString();
		if (WeekdayUtil.isWeekday(new GregorianCalendar())) {
			if (timeStr.compareTo("09-25-00") >= 0 && timeStr.compareTo("15-00-00") <= 0) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		System.out.println(WeekdayUtil.currentTime());
	}
}