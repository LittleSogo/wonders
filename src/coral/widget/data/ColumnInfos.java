/**
 * Project: GdPlatform
 * Source file: ColumnInfo.java
 * Create At 2012-3-6 下午05:08:35
 * Create By 龚云
 */
package coral.widget.data;

import java.util.HashMap;

/**
 * 列信息集合
 * 
 * @author 龚云
 * 
 */
public class ColumnInfos extends HashMap<String, ColumnInfo> {

	private ColumnInfos() {
	}

	/**
	 * 添加列信息
	 * 
	 * @param columnName
	 *            列名
	 * @param defaultValue
	 *            列的默认值
	 * @param isPk
	 *            是否主键
	 * @return this
	 */
	public ColumnInfos add(String columnName, Object defaultValue, boolean isPk) {
		ColumnInfo columnInfo = new ColumnInfo(columnName, defaultValue, isPk);
		this.put(columnName, columnInfo);
		return this;
	}

	/**
	 * 构造一个列信息集合
	 * 
	 * @param columnName
	 *            列名
	 * @param defaultValue
	 *            列的默认值
	 * @param isPk
	 *            是否主键
	 * @return this
	 */
	public static ColumnInfos getColumnInfo(String columnName,
			Object defaultValue, boolean isPk) {
		ColumnInfos columnInfos = new ColumnInfos();
		return columnInfos.add(columnName, defaultValue, isPk);
	}

	private static final long serialVersionUID = 4023871307969384073L;
}