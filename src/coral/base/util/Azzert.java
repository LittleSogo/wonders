/**
 * Project: Coral
 * Source file: Assert.java
 * Create At 2013-10-23 下午04:26:12
 * Create By 龚云
 */
package coral.base.util;

import org.apache.commons.lang.StringUtils;

/**
 * @author 龚云
 */
public class Azzert extends org.springframework.util.Assert {

	public static void noEmptyElements(String[] array, String message) {
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				if (StringUtils.trimToEmpty(array[i]).isEmpty()) {
					throw new IllegalArgumentException(message);
				}
			}
		}
	}

	public static String trimedNotEmpty(String str, String message) {
		str = StringUtils.trimToEmpty(str);
		if (str.isEmpty())
			throw new IllegalArgumentException(message);
		return str;
	}

}
