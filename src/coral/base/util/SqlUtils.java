/**
 * Project: Coral
 * Source file: SqlTemplateFormat.java
 * Create At 2014-1-26 下午01:05:46
 * Create By 龚云
 */
package coral.base.util;

import org.apache.commons.lang.StringUtils;

/**
 * @author 龚云
 * 
 */
public class SqlUtils {
	public static String format(String sql, String searchListStr,
			String... replaceList) {
		String[] searchList = searchListStr.split(",");
		//return StringUtils.replaceEach(sql, searchList, replaceList);
		return null;
	}

	public static boolean checkSqlSymbol(String str) {
		str = StringUtils.trimToEmpty(str);
		return str.matches("[\\w_]*");
	}

}
