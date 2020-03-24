/**
 * Project: GdPlatform
 * Source file: ColumnInfo.java
 * Create At 2012-3-6 下午05:30:56
 * Create By 龚云
 */
package coral.widget.data;

import org.apache.commons.lang.StringUtils;

/**
 * 列信息
 * 
 * @author 龚云
 * 
 */
public class ColumnInfo {

	/**
	 * 构造列信息
	 * 
	 * @param name
	 *            列名
	 * @param defaultValue
	 *            列的默认值
	 * @param isPk
	 *            是否主键列
	 */
	public ColumnInfo(String name, Object defaultValue, boolean isPk) {
		if (StringUtils.trimToEmpty(name).isEmpty())
			throw new NullPointerException(
					"Parameter 'name' cannot be null or empty.");
		this.name = name;
		this.defaultValue = defaultValue == null ? "" : defaultValue;
		this.isPk = isPk;
	}

	/**
	 * 获取列的默认值
	 * 
	 * @return 列的默认值
	 */
	public Object getDefaultValue() {
		return defaultValue;
	}

	/**
	 * 获取列名
	 * 
	 * @return 列名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 列是否是主键列
	 * 
	 * @return 是ture，否false
	 */
	public boolean isPk() {
		return isPk;
	}

	private Object defaultValue;

	private boolean isPk = false;

	private String name;
}
