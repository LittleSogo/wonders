/**
 * Project: GdPlatform
 * Source file: NumberUtils.java
 * Create At 2011-11-23 上午11:23:34
 * Create By 龚云
 */
package coral.base.util;

import org.apache.commons.lang.StringUtils;

/**
 * 基本类型工具类
 * 
 * @author 龚云
 * 
 */
public class BasicTypeUtils {

	/**
	 * 转换字符串为boolean，失败取默认值
	 * 
	 * @param str
	 *            字符串
	 * @param defaultValue
	 *            默认值
	 * @return
	 */
	public static Boolean toBoolean(String str, Boolean defaultValue) {
		if (StringUtils.trimToEmpty(str).isEmpty())
			return defaultValue;
		try {
			return Boolean.parseBoolean(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 转换字符串为Integer，失败取默认值
	 * 
	 * @param str
	 *            字符串
	 * @param defaultValue
	 *            默认值
	 * @return
	 */
	public static Integer toInteger(String str, Integer defaultValue) {
		if (StringUtils.trimToEmpty(str).isEmpty())
			return defaultValue;
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

}
