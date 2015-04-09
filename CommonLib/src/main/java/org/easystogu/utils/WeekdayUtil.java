package org.easystogu.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class WeekdayUtil {

	public static String currentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date()).toString();
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d1 = sdf.parse(beforeDate);
			Date d2 = sdf.parse(afterDate);

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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if ((calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
				&& (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)) {
			// 平时
			return !getWeekdayIsHolidayList().contains(sdf.format(calendar.getTime()));
		} else {
			// 周末
			return getWeekendIsWorkDateList().contains(sdf.format(calendar.getTime()));
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

	public static boolean isCurrentTimeWorkingDayInDealTime() {
		GregorianCalendar cal = new GregorianCalendar();
		return ((cal.getTime().getHours() >= 9) && (cal.getTime().getHours() <= 15))
				&& WeekdayUtil.isWeekday(new GregorianCalendar());
	}

	// 返回某年第几周的所有工作日，年底交叉的分开，一年最多53周，
	public static List<String> getWorkingDaysOfWeek(int year, int week) {
		List<String> dates = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.WEEK_OF_YEAR, week);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		String date = sdf.format(cal.getTime());
		if (date.startsWith(year + "")) {
			dates.add(sdf.format(cal.getTime()));
		}
		cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		date = sdf.format(cal.getTime());
		if (date.startsWith(year + "")) {
			dates.add(sdf.format(cal.getTime()));
		}
		cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		date = sdf.format(cal.getTime());
		if (date.startsWith(year + "")) {
			dates.add(sdf.format(cal.getTime()));
		}
		cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		date = sdf.format(cal.getTime());
		if (date.startsWith(year + "")) {
			dates.add(sdf.format(cal.getTime()));
		}
		cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		date = sdf.format(cal.getTime());
		if (date.startsWith(year + "")) {
			dates.add(sdf.format(cal.getTime()));
		}

		return dates;
	}

	public static List<String> getCurrentWeekDates() {
		Calendar cal = Calendar.getInstance();
		int weekNumber = cal.get(Calendar.WEEK_OF_YEAR);
		return getWorkingDaysOfWeek(new Date().getYear() + 1900, weekNumber);
	}

	// date is like: 2015-02-27
	// 返回某一日所在周的所有工作日
	public static List<String> getWeekWorkingDates(String date) {
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
	}

	public static void main(String[] args) {

		List<String> dates = WeekdayUtil.getWorkingDaysOfWeek(2015, 15);
		for (String date : dates) {
			System.out.println(date);
		}
	}
}