/**
 * Project: Coral
 * Source file: NumberUtils.java
 * Create At 2013-9-18 下午04:00:24
 * Create By 龚云
 */
package coral.base.util;

import java.math.BigDecimal;

/**
 * @author 龚云
 * 
 */
public class NumberUtils {

	public static BigDecimal getBigDecimal(Integer v) {
		return v == null ? null : new BigDecimal(v + "");
	}

	public static BigDecimal getBigDecimal(Long v) {
		return v == null ? null : new BigDecimal(v + "");
	}

	public static int getInteger(BigDecimal v) {
		return v == null ? 0 : v.intValue();
	}

	public static long getLong(BigDecimal v) {
		return v == null ? 0 : v.longValue();
	}

}
