/**
 * Project: GdPlatform
 * Source file: RowSetInterceptor.java
 * Create At 2012-3-6 上午09:59:19
 * Create By 龚云
 */
package coral.widget.data;

/**
 * 添加列时的监听器
 * 
 * @author 龚云
 * 
 */
public interface AddColumnListener {

	/**
	 * {@link DataSet}中在处理每一行时添加新列值时的监听器
	 * 
	 * @param columnName
	 *            新增的列名
	 * @param defaultValue
	 *            新增的列的默认值
	 * @param isPk
	 *            新增列是否是主键列
	 * @param rowNo
	 *            当前处理的行号
	 * @param rowSet
	 *            当前处理的行
	 * @param dataSet
	 *            数据集
	 */
	void onAdd(String columnName, Object defaultValue, boolean isPk, int rowNo,
			RowSet rowSet, DataSet dataSet);

}
