/**
 * Project: Coral
 * Source file: DateUtils.java
 * Create At 2013-12-13 上午10:52:12
 * Create By 龚云
 */
package coral.base.util;

import java.util.Calendar;
import java.util.Date;

/**
 * @author 龚云
 * 
 */
public class CalendarUtils {

	public static void clearTime(Calendar c) {
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
	}

	public static Calendar getCalendar(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.setFirstDayOfWeek(Calendar.MONDAY);
		return c;
	}

	public static long getDeltaDay(Calendar src, Calendar target) {
		Calendar sc = (Calendar) src.clone();
		clearTime(sc);
		Calendar tc = (Calendar) target.clone();
		clearTime(tc);
		int cp = sc.compareTo(tc);
		if (cp == 0)
			return 0;
		int delta = 0;
		int increment = cp > 0 ? -1 : 1;
		while (!isSameDay(sc, tc)) {
			sc.add(Calendar.DAY_OF_YEAR, increment);
			delta += increment;
		}
		return delta;
	}

	public static long getDeltaMonth(Calendar src, Calendar target) {
		Calendar sc = (Calendar) src.clone();
		clearTime(sc);
		Calendar tc = (Calendar) target.clone();
		clearTime(tc);
		int cp = sc.compareTo(tc);
		if (cp == 0)
			return 0;
		int delta = 0;
		int increment = cp > 0 ? -1 : 1;
		while (!isSameMonth(sc, tc)) {
			sc.add(Calendar.MONTH, increment);
			delta += increment;
		}
		return delta;
	}

	public static long getDeltaWeek(Calendar src, Calendar target) {
		Calendar sc = (Calendar) src.clone();
		clearTime(sc);
		Calendar tc = (Calendar) target.clone();
		clearTime(tc);
		int cp = sc.compareTo(tc);
		if (cp == 0)
			return 0;
		int delta = 0;
		int increment = cp > 0 ? -1 : 1;
		while (!isSameWeek(sc, tc)) {
			sc.add(Calendar.DAY_OF_YEAR, 7 * increment);
			delta += increment;
		}
		return delta;
	}

	public static long getDeltaYear(Calendar src, Calendar target) {
		Calendar sc = (Calendar) src.clone();
		clearTime(sc);
		Calendar tc = (Calendar) target.clone();
		clearTime(tc);
		int cp = sc.compareTo(tc);
		if (cp == 0)
			return 0;
		int delta = 0;
		int increment = cp > 0 ? -1 : 1;
		while (!isSameYear(sc, tc)) {
			sc.add(Calendar.YEAR, increment);
			delta += increment;
		}
		return delta;
	}

	public static boolean isBeforeYesterday(Calendar src, Calendar target) {
		return isDeltaDay(src, target, -2);
	}

	public static boolean isDeltaDay(Calendar src, Calendar target, int count) {
		if (count != 0) {
			Calendar c = (Calendar) src.clone();
			c.add(Calendar.DAY_OF_YEAR, count);
			return isSameDay(c, target);
		}
		return isSameDay(src, target);
	}

	public static boolean isDeltaMonth(Calendar src, Calendar target, int count) {
		if (count != 0) {
			Calendar c = (Calendar) src.clone();
			c.add(Calendar.MONTH, 1 * count);
			return isSameMonth(c, target);
		}
		return isSameMonth(src, target);
	}

	public static boolean isDeltaWeek(Calendar src, Calendar target, int count) {
		if (count != 0) {
			Calendar c = (Calendar) src.clone();
			c.add(Calendar.DAY_OF_YEAR, 7 * count);
			return isSameWeek(c, target);
		}
		return isSameWeek(src, target);
	}

	public static boolean isDeltaYear(Calendar src, Calendar target, int count) {
		if (count != 0) {
			Calendar c = (Calendar) src.clone();
			c.add(Calendar.YEAR, count);
			return isSameYear(c, target);
		}
		return isSameYear(src, target);
	}

	public static boolean isLastMonth(Calendar src, Calendar target) {
		return isDeltaMonth(src, target, -1);
	}

	public static boolean isLastWeek(Calendar src, Calendar target) {
		return isDeltaWeek(src, target, -1);
	}

	public static boolean isLastYear(Calendar src, Calendar target) {
		return isDeltaYear(src, target, -1);
	}

	public static boolean isSameDay(Calendar src, Calendar target) {
		return (isSameERA(src, target) && isSameYear(src, target) && src
				.get(Calendar.DAY_OF_YEAR) == target.get(Calendar.DAY_OF_YEAR));
	}

	public static boolean isSameERA(Calendar src, Calendar target) {
		return src.get(Calendar.ERA) == target.get(Calendar.ERA);
	}

	public static boolean isSameMonth(Calendar src, Calendar target) {
		return isSameERA(src, target) && isSameYear(src, target)
				&& src.get(Calendar.MONTH) == target.get(Calendar.MONTH);
	}

	public static boolean isSameWeek(Calendar src, Calendar target) {
		if (isSameYear(src, target))
			return src.get(Calendar.WEEK_OF_YEAR) == target
					.get(Calendar.WEEK_OF_YEAR);

		Calendar b = (Calendar) src.clone();// 一周的开始
		clearTime(b);
		int dw = b.getFirstDayOfWeek();
		b.set(Calendar.DAY_OF_WEEK, dw);

		Calendar e = (Calendar) b.clone();// 下一周的开始
		e.add(Calendar.DAY_OF_YEAR, 7);
		return target.getTimeInMillis() < e.getTimeInMillis()
				&& target.getTimeInMillis() >= b.getTimeInMillis();
	}

	public static boolean isSameYear(Calendar src, Calendar target) {
		return src.get(Calendar.YEAR) == target.get(Calendar.YEAR);
	}

	public static boolean isYesterday(Calendar src, Calendar target) {
		return isDeltaDay(src, target, -1);
	}
}
