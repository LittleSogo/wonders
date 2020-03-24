/**
 * Project: GdPlatform
 * Source file: StringUtil.java
 * Create At 2012-7-10 上午09:59:32
 * Create By 龚云
 */
package coral.base.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

import org.apache.commons.lang.StringUtils;

import wfc.service.config.Config;
import wfc.service.util.StringHelper;

/**
 * @author 龚云
 */
public class StringUtil {

	/**
	 * 去除str的前半部分(prefix)
	 * 
	 * @param str
	 * @param prefix
	 * @return
	 */
	public static String cutePrefix(String str, String prefix) {
		if (str.startsWith(prefix)) {
			String subStr = str.substring(str.indexOf(prefix) + prefix.length());
			return subStr;
		}
		return null;
	}

	public static int getInt(String val, int defaultVal) {
		try {
			return Integer.parseInt(val);
		} catch (Exception e) {

		}
		return defaultVal;
	}

	/**
	 * 合并多个字符串
	 * 
	 * @param strs
	 * @return
	 */
	public static String merge(String... strs) {
		return merge(false, strs);
	}

	/**
	 * 合并多个字符串并过滤null
	 * 
	 * @param strs
	 * @return
	 */
	public static String mergeNoNull(String... strs) {
		return merge(true, strs);
	}

	/**
	 * 将字符串按分隔符分隔为列表（连续的2个分隔符视为转义符），分隔符不能为'\'
	 * 
	 * @param str 源字符串
	 * @param splitChar 分隔符，分隔符不能为'\'
	 * @param filtSame 是否过滤相同项
	 * @return 分割后的列表
	 */
	public static List<String> splitToList(String str, char splitChar, boolean filtSame) {
		if ('\\' == splitChar)
			throw new RuntimeException("Split char cannot be '\\'.");
		List<String> l = new ArrayList<String>();
		Set<String> s = new HashSet<String>();
		if (str == null)
			return l;
		boolean isConvert = false;
		char[] cs = str.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cs.length; i++) {
			char c = cs[i];
			if (c == splitChar) {
				if (isConvert) {
					sb.append(splitChar);
				} else {
					addToList(l, sb.toString(), s, filtSame);
					sb = new StringBuilder();
				}
				isConvert = false;
			} else if (c == '\\') {
				if (i < cs.length - 1 && !isConvert) {
					char nc = cs[i + 1];
					if (nc == splitChar || nc == '\\') {
						isConvert = true;
					} else {
						sb.append(c);
						isConvert = false;
					}
				} else {
					sb.append('\\');
					addToList(l, sb.toString(), s, filtSame);
					sb = new StringBuilder();
				}
			} else {
				sb.append(c);
				isConvert = false;
			}
			if (i == cs.length - 1) {
				addToList(l, sb.toString(), s, filtSame);
			}
		}
		return l;
	}

	/**
	 * 若在给定编码{@link SystemConstant#DEFAULT_CHARSET}
	 * 下字符串的字节长度大于给定的最大字节长度则截取，若size==0返回空字符串，<br>
	 * 若size&lt;0返回null，否则返回原字符串
	 * 
	 * @param s 源字符串
	 * @param size 需要满足的最大字节长度
	 * @return 截取后字符串
	 */
	public static String substringBySize(String s, int size) {
		return substringBySize(s, size, SystemConstant.DEFAULT_CHARSET);
	}

	/**
	 * 若在给定编码下字符串的字节长度大于给定的最大字节长度则截取，若size==0返回空字符串，<br>
	 * 若size&lt;0返回null，否则返回原字符串
	 * 
	 * @param s 源字符串
	 * @param size 需要满足的最大字节长度
	 * @param charset 截取时采用的字符编码
	 * @return 截取后字符串
	 */
	public static String substringBySize(String s, int size, String charset) {
		int len = 0;
		try {
			if (s == null)
				return null;
			if (size < 0)
				return null;
			if (size == 0)
				return "";
			if (subStringByChar) {
				len = s == null ? 0 : s.length();
				if (len <= size)
					return s;
				return s.substring(0, size);
			} else {
				byte[] b = s.getBytes(charset);
				len = s == null ? 0 : b.length;
				if (len <= size)
					return s;
				return new String(Arrays.copyOfRange(b, 0, size), charset);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static String toBinaryUnitStyle(BigDecimal d, int precision) {
		String pattern = (precision > 0 ? "0." : "0") + StringUtils.leftPad("", precision, '#');
		return BinaryHelper.getInstance().BIT.toAutoSize(d, pattern);
	}

	public static String toBinaryUnitStyle(long d, int precision) {
		String pattern = (precision > 0 ? "0." : "0") + StringUtils.leftPad("", precision, '#');
		return BinaryHelper.getInstance().BIT.toAutoSize((double) d, pattern);
	}

	/**
	 * 将数组按指定分隔符合并为字符串，若str中出现分隔符，则用'\'转义，'\'本身转义为'\\'
	 * 
	 * @param strs 原字符串
	 * @param splitChar 分隔符
	 * @return 合并后结果
	 */
	public static String toString(String[] strs, char splitChar) {
		if (splitChar == '\\')
			throw new RuntimeException("Split char cannot be '\\'.");
		if (strs == null || strs.length == 0)
			return null;
		StringBuilder sb = new StringBuilder();
		String sp = splitChar + "";
		for (String str : strs) {
			str = StringUtils.replace(str, "\\", "\\\\");
			str = StringUtils.replace(str, sp, "\\" + sp);
			if (sb.length() > 0)
				sb.append(splitChar);
			sb.append(str);
		}
		return sb.toString();
	}

	private static void addToList(List<String> l, String str, Set<String> s, boolean filtSame) {
		if (!filtSame) {
			l.add(str);
		} else if (!s.contains(str)) {
			l.add(str);
			s.add(str);
		}
	}

	/**
	 * 合并多个字符串
	 * 
	 * @param filterNull 是否过滤null
	 * @param strs 多个字符串
	 * @return
	 */
	private static String merge(boolean filterNull, String... strs) {
		if (strs == null || strs.length == 0)
			return null;
		StringBuilder sb = new StringBuilder();
		for (String str : strs) {
			sb.append(filterNull ? StringHelper.filterNull(str) : str);
		}
		return sb.toString();
	}

	public static final long b = 1;

	public static final long B = b * 8L;

	public static final long KB = B * 1024L;

	public static final long MB = KB * 1024L;

	public static final long GB = MB * 1024L;

	public static final long TB = GB * 1024L;

	public static final long PB = TB * 1024L;

	private static boolean subStringByChar = "y".equalsIgnoreCase(Config.get("subStringByChar"));
}
