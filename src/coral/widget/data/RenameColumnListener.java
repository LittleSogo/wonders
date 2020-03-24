/**
 * Project: GdPlatform
 * Source file: RemoveColumnListener.java
 * Create At 2012-3-6 上午11:19:09
 * Create By 龚云
 */
package coral.widget.data;

/**
 * 删除列时的监听器
 * 
 * @author 龚云
 */
public interface RenameColumnListener {

	/**
	 * {@link coral.widget.data.DataSet}中在处理每一行时删除某列值之后执行的监听器
	 * 
	 * @param oldColumnName
	 *            删除的列的名称
	 * @param newColumnName
	 *            删除的列的名称
	 * @param columnValue
	 *            删除的列的值
	 * @param rowNo
	 *            当前处理的行号
	 * @param row
	 *            当前行
	 * @param dataSet
	 *            数据集
	 */
	void onRename(String oldColumnName, String newColumnName, Object columnValue, int rowNo, RowSet row, DataSet dataSet);

}
