/**
 * Project: GdPlatform
 * Source file: PrimaryKey.java
 * Create At 2012-3-6 下午12:58:04
 * Create By 龚云
 */
package coral.widget.data;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import wfc.service.util.OrderedHashSet;

/**
 * 主键对象，用于{@link DataSet#getRowSet(PrimaryColumns)} )}方法通过它快速获取某行的记录{@link RowSet}
 * 
 * @author 龚云
 * 
 */
public class PrimaryColumns extends HashMap<String, Object> {

	private PrimaryColumns() {
	}

	private PrimaryColumns(OrderedHashSet<String> pkList, RowSet rowSet) {
		if (pkList != null && rowSet != null) {
			for (String pk : pkList) {
				this.put(pk, rowSet.getRow().get(pk));
			}
		}
	}

	/**
	 * 加入主键信息
	 * 
	 * @param pkColName
	 *            主键名
	 * @param pkColValue
	 *            主键值
	 * @return this
	 */
	public PrimaryColumns add(String pkColName, String pkColValue) {
		if (!StringUtils.trimToEmpty(pkColName).isEmpty()) {
			this.put(pkColName, pkColValue);
		}
		return this;
	}

	/**
	 * 根据主键列表和杭信息构造主键对象
	 * 
	 * @param pkList
	 *            主键名列表
	 * @param rowSet
	 *            主键所对应的行记录
	 * @return 主键对象
	 */
	public static PrimaryColumns getPrimaryMap(OrderedHashSet<String> pkList,
			RowSet rowSet) {
		PrimaryColumns map = new PrimaryColumns(pkList, rowSet);
		return map.size() == 0 ? null : map;
	}

	/**
	 * 根据主键名和主键值构造主键对象
	 * 
	 * @param pkColName
	 *            主键名
	 * @param pkColValue
	 *            主键值
	 * @return 主键对象
	 */
	public static PrimaryColumns getPrimaryMap(String pkColName, String pkColValue) {
		PrimaryColumns map = new PrimaryColumns();
		return map.add(pkColName, pkColValue);
	}

	private static final long serialVersionUID = 2543907533188810852L;
}
