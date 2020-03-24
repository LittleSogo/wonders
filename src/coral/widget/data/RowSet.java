package coral.widget.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import wfc.service.database.RecordSet;
import wfc.service.util.OrderedHashMap;
import coral.base.data.DataRow;

/**
 * 
 * {@link DataSet}中行数据集中对应的行数据结构
 * 
 * @author 龚云
 * @see DataSet
 * 
 */
public class RowSet {

	private RowSet() {
	}

	/**
	 * 获取行数据
	 * 
	 * @return row
	 */
	public OrderedHashMap<String, Object> getRow() {
		return row;
	}

	/**
	 * 根据列名获取列值
	 * 
	 * @param columnName
	 *            列名
	 * @return 列值
	 */
	public Object getColumnValue(String columnName) {
		return this.row.get(columnName);
	}

	public boolean hasData() {
		return !this.row.isEmpty();
	}

	/**
	 * 设置列值
	 * 
	 * @param columnName
	 *            列名
	 * @param value
	 *            列值
	 */
	public void put(String columnName, Object value) {
		this.row.put(columnName, value);
	}

	/**
	 * 
	 * 转换{@link DataRow}对象为行数据结构
	 * 
	 * @param dataRow
	 *            待转换的行数据
	 * @return RowSet
	 */
	public static RowSet convert(DataRow dataRow) {
		RowSet rowSet = new RowSet();
		for (int i = 0; i < dataRow.size(); i++) {
			String colName = dataRow.getKey(i);
			Object value = dataRow.get(colName);
			rowSet.row.put(colName, value);
		}
		return rowSet;
	}

	/**
	 * 
	 * 以keyColName和valueColName 为列名分别对应以key和value为值构成行数据结构
	 * {keyColName:key = valueColName:value}
	 *
	 * @param key 键
	 * @param value 值
	 * @param keyColName 键列名
	 * @param valueColName 值列名
	 * @return RowSet
	 */
	public static RowSet convert(Object key, Object value, String keyColName,
			String valueColName) {
		RowSet rowSet = new RowSet();
		rowSet.row.put(keyColName, key);
		rowSet.row.put(valueColName, value);
		return rowSet;
	}

	/**
	 * 
	 * 根据OrderedHashMap<String, {@link FieldHelper}>的信息方位参数o以构成行数据结构
	 * 
	 * @param o
	 *            数据对象
	 * @param fhs
	 *            用于获取数据对象信息的{@link FieldHelper}对象
	 * @return RowSet
	 */
	@SuppressWarnings("unchecked")
	public static RowSet convert(Object o,
			OrderedHashMap<String, FieldHelper> fhs) {
		RowSet rowSet = new RowSet();
		try {
			for (Entry<String, FieldHelper> entry : fhs.entrySet()) {
				String colName = entry.getKey();
				FieldHelper fh = entry.getValue();
				if (fh.isPk() && fh.getMethod() == null) {
					rowSet.row.put(colName, UUID.randomUUID().toString());
				} else {
					Object value = fh.getMethod().invoke(o);
					rowSet.row.put(colName, wrapFieldValue(value));
				}
			}
			if(o instanceof HashMap){
				for(Map.Entry<String, String> entry : ((HashMap<String, String>)o).entrySet()){
					rowSet.row.put(entry.getKey(), entry.getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rowSet;
	}

	/**
	 * 转换{@link RecordSet}对象为行数据结构
	 * 
	 * @param recordSet {@link RecordSet}
	 * @return RowSet
	 */
	public static RowSet convert(RecordSet recordSet) {
		RowSet rowSet = new RowSet();
		for (String field : recordSet.FIELD_NAME) {
			rowSet.row.put(field, recordSet.getObject(field));
		}
		return rowSet;
	}

	/**
	 * 
	 * 处理数据对象中的特殊类型字段：<br>
	 * 1.{@link Date}转换为long值
	 * 2.null -> null
	 * 3.other -> other
	 * 
	 * @param o
	 *            数据对象
	 * @return objectValue
	 */
	public static Object wrapFieldValue(Object o) {
		if (o == null)
			return null;
		if (o instanceof Date) {
			o = ((Date) o).getTime();
		}
		return o;
	}

	protected OrderedHashMap<String, Object> row = new OrderedHashMap<String, Object>();

}
