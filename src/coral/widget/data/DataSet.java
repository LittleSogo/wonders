package coral.widget.data;

import coral.base.data.DataPage;
import coral.base.data.DataRow;
import coral.widget.data.FieldHelper.Mode;
import coral.widget.utils.MetaDataJsonConverter;
import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.apache.commons.lang.StringUtils;
import wfc.facility.tool.autocode.PaginationArrayList;
import wfc.service.database.RecordSet;
import wfc.service.util.OrderedHashMap;
import wfc.service.util.OrderedHashSet;

import javax.persistence.Column;
import java.util.*;
import java.util.Map.Entry;

/**
 * 结构化的数据集，主要便于前台Ext展现，常搭配{@link MetaDataJsonConverter}使用
 * 
 * @author 龚云
 * @see MetaDataJsonConverter
 * @see FieldHelper
 * @see RowSet
 */
public class DataSet {

	private DataSet() {

	}

	/**
	 * 添加一列信息
	 * 
	 * @param columnName 添加的列名
	 * @param defaultValue 添加的列的默认值
	 * @param isPk 添加的列是否是主键
	 * @param addColumnListener 添加列时的监听器
	 * @return this
	 */
	public DataSet addColumn(String columnName, Object defaultValue, boolean isPk, AddColumnListener addColumnListener) {
		return addColumns(ColumnInfos.getColumnInfo(columnName, defaultValue, isPk), addColumnListener);
	}

	/**
	 * 添加多列信息
	 * 
	 * @param columnInfos 列信息集合
	 * @param addColumnListener 添加列时的监听器
	 * @return this
	 */
	public DataSet addColumns(ColumnInfos columnInfos, AddColumnListener addColumnListener) {
		boolean hasNewPk = false;
		for (Entry<String, ColumnInfo> entry : columnInfos.entrySet()) {
			String columnName = entry.getKey();
			ColumnInfo columnInfo = entry.getValue();
			if (columnName.isEmpty())
				throw new NullPointerException("Parameter 'columnName' could not be null or empty.");
			if (fieldList.contains(columnName))
				throw new IllegalArgumentException("The columnName is already in this DataSet.");
			hasNewPk |= columnInfo.isPk();
			if (columnInfo.isPk()) {
				this.pkList.add(columnName);
			}
			fieldList.add(columnName);
		}
		String genPkCol = FieldHelper.GENERATE_PK_COL_NAME;
		boolean hasGenPkCol = pkList.contains(genPkCol);
		if (hasNewPk) {
			this.rowMap.clear();
			this.pkList.remove(genPkCol);
			this.fieldList.remove(genPkCol);
		}
		for (int i = 0; i < rows.size(); i++) {
			RowSet rowSet = rows.get(i);
			if (hasGenPkCol && hasNewPk)
				rowSet.row.remove(genPkCol);
			for (Entry<String, ColumnInfo> entry : columnInfos.entrySet()) {
				String columnName = entry.getKey();
				ColumnInfo columnInfo = entry.getValue();
				Object defaultValue = columnInfo.getDefaultValue();
				rowSet.getRow().put(columnName, defaultValue);
				if (addColumnListener != null)
					addColumnListener.onAdd(columnName, defaultValue, columnInfo.isPk(), i, rowSet, this);
			}
			if (hasNewPk) {
				PrimaryColumns primaryMap = PrimaryColumns.getPrimaryMap(pkList, rowSet);
				this.rowMap.put(primaryMap, rowSet);
			}
		}
		return this;
	}

	/**
	 * 当前页
	 */
	public Integer getCurrentPage() {
		return currentPage;
	}

	/**
	 * 数据集中所有列的集合
	 */
	public OrderedHashSet<String> getFieldList() {
		return fieldList;
	}

	/**
	 * 下一页
	 */
	public Integer getNextPage() {
		return nextPage;
	}

	/**
	 * 数据集中所有主键列的集合
	 */
	public OrderedHashSet<String> getPkList() {
		return pkList;
	}

	/**
	 * 前一页
	 */
	public Integer getPreviousPage() {
		return previousPage;
	}

	/**
	 * 根据获取对应的行，若{@link #getPkList()}为空，则返回null。
	 * 
	 * @param pkValue 主键集合，Map<String, Object><主键，值>
	 * @return 行对象
	 */
	public RowSet getRowSet(PrimaryColumns pkValue) {
		if (this.pkList == null || this.pkList.isEmpty())
			return null;
		return this.rowMap.get(pkValue);
	}

	/**
	 * 数据集中所有行的集合
	 */
	public List<RowSet> getRows() {
		return rows;
	}

	/**
	 * 总页数
	 */
	public Integer getTotalPageCount() {
		return totalPageCount;
	}

	/**
	 * 总记录数
	 */
	public Integer getTotalRecordCount() {
		return totalRecordCount;
	}

	/**
	 * 删除一列信息，若DataSet不包含指定的列则不作任何处理。
	 * 
	 * @param columnName 删除的列名
	 * @param removeColumnListener 删除列时的监听器
	 * @return this
	 */
	public DataSet removeColumn(String columnName, RemoveColumnListener removeColumnListener) {
		return removeColumns(new String[] { columnName }, removeColumnListener);
	}

	/**
	 * 删除多列信息，未包含在原列集合中的列将自动忽略，不作处理
	 * 
	 * @param columnNames 删除的列名集合
	 * @param removeColumnListener 删除列时的监听器
	 * @return this
	 */
	public DataSet removeColumns(String[] columnNames, RemoveColumnListener removeColumnListener) {
		if (columnNames == null || columnNames.length == 0)
			return this;
		Set<String> columnNameSet = new HashSet<String>();
		for (String columnName : columnNames) {
			String col = StringUtils.trimToEmpty(columnName);
			if (!col.isEmpty() && fieldList.contains(col)) {
				columnNameSet.add(columnName);
			}
		}
		if (columnNameSet.isEmpty())
			return this;
		if (columnNameSet.size() >= fieldList.size())
			throw new RuntimeException("实际删除的列的数量必须小于集合的总列数，即至少保证集合中有一列信息。");
		boolean hasPk = false;
		for (String columnName : columnNameSet) {
			fieldList.remove(columnName);
			boolean isPk = this.pkList.contains(columnName);
			if (isPk) {
				this.pkList.remove(columnName);
				hasPk = true;
			}
		}
		if (hasPk)
			this.rowMap.clear();
		boolean isGeneratePk = this.pkList.isEmpty();
		String genPkCol = FieldHelper.GENERATE_PK_COL_NAME;
		if (isGeneratePk) {
			this.fieldList.add(genPkCol);
			this.pkList.add(genPkCol);
		}
		for (int i = 0; i < rows.size(); i++) {
			RowSet rowSet = rows.get(i);
			if (isGeneratePk)
				rowSet.put(genPkCol, UUID.randomUUID().toString());
			for (String columnName : columnNameSet) {
				Object columnValue = rowSet.getRow().remove(columnName);
				if (removeColumnListener != null)
					removeColumnListener.onRemove(columnName, columnValue, i, rowSet, this);
			}
			if (hasPk) {
				PrimaryColumns primaryMap = PrimaryColumns.getPrimaryMap(pkList, rowSet);
				this.rowMap.put(primaryMap, rowSet);
			}
		}
		return this;
	}

	public DataSet renameColumn(String oldColumnName, String newColumnName, RenameColumnListener listener) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(oldColumnName, newColumnName);
		return renameColumns(map, listener);
	}

	@SuppressWarnings("unchecked")
	public DataSet renameColumns(Map<String, String> columnMap, RenameColumnListener listener) {
		if (columnMap.isEmpty())
			return this;
		BidiMap renameColMap = new DualHashBidiMap();
		String genPkCol = FieldHelper.GENERATE_PK_COL_NAME;
		for (Entry<String, String> entry : columnMap.entrySet()) {
			String oldColName = StringUtils.trimToEmpty(entry.getKey());
			String newColName = StringUtils.trimToEmpty(entry.getValue());
			if (oldColName.isEmpty() || newColName.isEmpty() || !fieldList.contains(oldColName)
					|| renameColMap.containsValue(newColName))
				continue;
			if (genPkCol.contains(oldColName))
				throw new RuntimeException("请不要重命名自动生成的主键列名：" + genPkCol);
			renameColMap.put(oldColName, newColName);
		}
		if (renameColMap.isEmpty())
			return this;
		boolean hasPk = false;
		for (Entry<String, String> entry : ((Map<String, String>) renameColMap).entrySet()) {
			String oldColName = entry.getKey();
			String newColName = entry.getValue();
			fieldList.remove(oldColName);
			fieldList.add(newColName);
			boolean isPk = this.pkList.contains(oldColName);
			if (isPk) {
				this.pkList.remove(oldColName);
				this.pkList.add(newColName);
				hasPk = true;
			}
		}
		if (hasPk)
			this.rowMap.clear();
		for (int i = 0; i < rows.size(); i++) {
			RowSet rowSet = rows.get(i);
			for (Entry<String, String> entry : ((Map<String, String>) renameColMap).entrySet()) {
				String oldColName = entry.getKey();
				String newColName = entry.getValue();
				Object columnValue = rowSet.getRow().remove(oldColName);
				rowSet.put(newColName, columnValue);
				if (listener != null)
					listener.onRename(oldColName, newColName, columnValue, i, rowSet, this);
			}
			if (hasPk) {
				PrimaryColumns primaryMap = PrimaryColumns.getPrimaryMap(pkList, rowSet);
				this.rowMap.put(primaryMap, rowSet);
			}
		}
		return this;
	}

	/**
	 * 添加行到[主键，行]集合
	 * 
	 * @param rowSet 行[列, 值]
	 */
	protected void addRowMap(RowSet rowSet) {
		PrimaryColumns primaryMap = PrimaryColumns.getPrimaryMap(pkList, rowSet);
		if (primaryMap != null)
			rowMap.put(primaryMap, rowSet);
	}

	/**
	 * 当前页
	 */
	private Integer currentPage = 1;

	/**
	 * 下一页
	 */
	private Integer nextPage = 1;

	/**
	 * 前一页
	 */
	private Integer previousPage = 1;

	/**
	 * 总页数
	 */
	private Integer totalPageCount = 1;

	/**
	 * 总记录数
	 */
	private Integer totalRecordCount = 1;

	/**
	 * 数据集中所有行的集合
	 */
	private List<RowSet> rows = new ArrayList<RowSet>();

	/**
	 * <主键，行>映射集合
	 */
	private Map<PrimaryColumns, RowSet> rowMap = new HashMap<PrimaryColumns, RowSet>();

	/**
	 * 数据集中所有列的集合
	 */
	private OrderedHashSet<String> fieldList = new OrderedHashSet<String>();

	/**
	 * 数据集中所有主键列的集合
	 */
	private OrderedHashSet<String> pkList = new OrderedHashSet<String>();

	/**
	 * 解析dataPage为数据集
	 * 
	 * @param dataPage {@link DataPage}
	 * @return this
	 */
	public static DataSet convert(DataPage dataPage) {
		DataSet dataSet = new DataSet();
		dataSet.pkList = dataPage.getPkList();
		dataSet.fieldList = dataPage.getFieldList();
		dataSet.currentPage = dataPage.getPageIndex();
		dataSet.nextPage = dataPage.getNextPageIndex();
		dataSet.previousPage = dataPage.getPrePageIndex();
		dataSet.totalPageCount = dataPage.getPageCount();
		dataSet.totalRecordCount = dataPage.getTotalCount();
		for (DataRow dataRow : dataPage) {
			if (dataRow != null) {
				RowSet rowSet = RowSet.convert(dataRow);
				if (rowSet.hasData()) {
					dataSet.addRowMap(rowSet);
					dataSet.rows.add(rowSet);
				}
			}
		}
		return dataSet;
	}

	/**
	 * 转换数据集{@link List}为数据集，采用默认的列解析模式，参见 DEFAULT_MODE
	 * 
	 * @param list 待转换的{@link List}数据集
	 * @param klass 数据集中包含的数据对象的所属类
	 * @return this
	 */
	public static DataSet convert(List<?> list, Class<?> klass) {
		return convert(list, klass, DEFAULT_MODE);
	}

	/**
	 * 转换数据集{@link List}为数据集
	 * 
	 * @param list 待转换的{@link List}数据集
	 * @param klass 数据集中包含的数据对象的所属类
	 * @param superKlass 数据集中包含的数据对象需要解析终止于超类
	 * @param mode 反射对象获取列名是使用的策略，默认为解析{@link Column}模式
	 * @return this
	 */
	public static DataSet convert(List<?> list, Class<?> klass, Class<?> superKlass, Mode mode) {
		OrderedHashMap<String, FieldHelper> fhs = FieldHelper.getFieldHelpers(klass, superKlass, mode);
		if (list instanceof PaginationArrayList<?>)
			return convert((PaginationArrayList<?>) list, klass, fhs, mode);
		return convert(list, klass, fhs, mode);
	}

	/**
	 * 转换数据集{@link List}为数据集
	 * 
	 * @param list 待转换的{@link List}数据集
	 * @param klass 数据集中包含的数据对象的所属类
	 * @param superKlass 数据集中包含的数据对象需要解析终止于超类
	 * @param fhs 用于获取数据集中的数据对象值的field配置集合OrderedHashMap<String,
	 *            {@link FieldHelper}>，key为列名
	 * @param mode 反射对象获取列名是使用的策略，默认为解析{@link Column}模式
	 * @return this
	 */
	public static DataSet convert(List<?> list, Class<?> klass, Class<?> superKlass,
			OrderedHashMap<String, FieldHelper> fhs, Mode mode) {
		if (fhs == null)
			fhs = FieldHelper.getFieldHelpers(klass, superKlass, mode);
		DataSet dataSet = new DataSet();
		for (Entry<String, FieldHelper> entry : fhs.entrySet()) {
			String columnName = entry.getKey();
			FieldHelper fh = entry.getValue();
			if (fh.isPk())
				dataSet.pkList.add(columnName);
			dataSet.fieldList.add(columnName);
		}
		dataSet.currentPage = 1;
		dataSet.nextPage = 1;
		dataSet.previousPage = 1;
		dataSet.totalPageCount = 1;
		dataSet.totalRecordCount = list.size();
		for (Object o : list) {
			if (o != null) {
				RowSet rowSet = RowSet.convert(o, fhs);
				if (rowSet.hasData()) {
					dataSet.addRowMap(rowSet);
					dataSet.rows.add(rowSet);
				}
			}
		}
		return dataSet;
	}

	/**
	 * 转换数据集{@link List}为数据集
	 * 
	 * @param list 待转换的{@link List}数据集
	 * @param klass 数据集中包含的数据对象的所属类
	 * @param mode 反射对象获取列名是使用的策略，默认为解析{@link Column}模式
	 * @return this
	 */
	public static DataSet convert(List<?> list, Class<?> klass, Mode mode) {
		return convert(list, klass, klass, mode);
	}

	/**
	 * 转换数据集{@link List}为数据集，采用默认的列解析模式，参见 DEFAULT_MODE
	 * 
	 * @param list 待转换的{@link List}数据集
	 * @param klass 数据集中包含的数据对象的所属类
	 * @param fhs 用于获取数据集中的数据对象值的field配置集合OrderedHashMap<String,
	 *            {@link FieldHelper}>，key为列名
	 * @return this
	 */
	public static DataSet convert(List<?> list, Class<?> klass, OrderedHashMap<String, FieldHelper> fhs) {
		if (list instanceof PaginationArrayList<?>)
			return convert((PaginationArrayList<?>) list, klass, fhs, DEFAULT_MODE);
		return convert(list, klass, fhs, DEFAULT_MODE);
	}

	/**
	 * 转换数据集{@link List}为数据集
	 * 
	 * @param list 待转换的{@link List}数据集
	 * @param klass 数据集中包含的数据对象的所属类
	 * @param fhs 用于获取数据集中的数据对象值的field配置集合OrderedHashMap<String,
	 *            {@link FieldHelper}>，key为列名
	 * @param mode 反射对象获取列名是使用的策略，默认为解析{@link Column}模式
	 * @return this
	 */
	public static DataSet convert(List<?> list, Class<?> klass, OrderedHashMap<String, FieldHelper> fhs, Mode mode) {
		return convert(list, klass, klass, fhs, mode);
	}

	/**
	 * 解析List<String[]>为数据集，数据集包含2列，key为主键列，value为值列，根据String[]数组长度分为3中情况：<br>
	 * 1.长度为1时，key和value相同；<br>
	 * 2.长度大于等于2时，key为数组下标为0的String，value为下标为1的String；<br>
	 * 3.长度小于1时不作转换
	 * 
	 * @see #convert(List, String, String)
	 * @param list 待转换的数据集合
	 * @return this
	 */
	public static DataSet convert(List<String[]> list) {
		return convert(list, "key", "value");
	}

	/**
	 * 解析List<String[]>为数据集，数据集包含2列，key为主键列，value为值列，根据String[]数组长度分为3中情况：<br>
	 * 1.长度为1时，key和value相同；<br>
	 * 2.长度大于等于2时，key为数组下标为0的String，value为下标为1的String；<br>
	 * 3.长度小于1时不作转换
	 * 
	 * @param list 待转换的数据集合
	 * @param keyColName 键的列名
	 * @param valueColName 值的列名
	 * @return this
	 */
	public static DataSet convert(List<String[]> list, String keyColName, String valueColName) {
		DataSet dataSet = new DataSet();
		OrderedHashSet<String> pkList = new OrderedHashSet<String>();
		OrderedHashSet<String> fieldList = new OrderedHashSet<String>();
		pkList.add(keyColName);
		fieldList.add(keyColName);
		fieldList.add(valueColName);
		dataSet.pkList = pkList;
		dataSet.fieldList = fieldList;
		dataSet.currentPage = 1;
		dataSet.nextPage = 1;
		dataSet.previousPage = 1;
		dataSet.totalPageCount = 1;
		dataSet.totalRecordCount = list.size();
		for (String[] values : list) {
			if (values == null || values.length == 0)
				continue;
			RowSet rowSet = null;
			if (values.length >= 2)
				rowSet = RowSet.convert(values[0], values[1], keyColName, valueColName);
			else if (values.length == 1)
				rowSet = RowSet.convert(values[0], values[0], keyColName, valueColName);
			if (rowSet != null && rowSet.hasData()) {
				dataSet.addRowMap(rowSet);
				dataSet.rows.add(rowSet);
			}
		}
		return dataSet;
	}

	/**
	 * 转换map集合为数据集，主键列（列名为key）为map的key，值列（列名为value），map实现若为
	 * {@link OrderedHashMap}则安顺序转换，否则乱序
	 * 
	 * @param map 待转换的数据集合
	 * @return this
	 */
	public static DataSet convert(Map<?, ?> map) {
		return convert(map, "key", "value");
	}

	/**
	 * 转换map集合为数据集，主键列（列名由keyColName指定）为map的key，值列（列名由valueColName指定）
	 * 
	 * @param map 待转换的map集合
	 * @param keyColName 主键列名
	 * @param valueColName 值列列名
	 * @return this
	 */
	public static DataSet convert(Map<?, ?> map, String keyColName, String valueColName) {
		DataSet dataSet = new DataSet();
		OrderedHashSet<String> pkList = new OrderedHashSet<String>();
		OrderedHashSet<String> fieldList = new OrderedHashSet<String>();
		pkList.add(keyColName);
		fieldList.add(keyColName);
		fieldList.add(valueColName);
		dataSet.pkList = pkList;
		dataSet.fieldList = fieldList;
		dataSet.currentPage = 1;
		dataSet.nextPage = 1;
		dataSet.previousPage = 1;
		dataSet.totalPageCount = 1;
		dataSet.totalRecordCount = map.size();
		if (map instanceof OrderedHashMap<?, ?>) {
			OrderedHashMap<?, ?> om = (OrderedHashMap<?, ?>) map;
			for (int i = 0; i < om.size(); i++) {
				RowSet rowSet = RowSet.convert(om.getKey(i), om.getValue(i), keyColName, valueColName);
				if (rowSet.hasData()) {
					dataSet.addRowMap(rowSet);
					dataSet.rows.add(rowSet);
				}
			}
		} else {
			for (Entry<?, ?> entry : map.entrySet()) {
				RowSet rowSet = RowSet.convert(entry.getKey(), entry.getValue(), keyColName, valueColName);
				if (rowSet.hasData()) {
					dataSet.addRowMap(rowSet);
					dataSet.rows.add(rowSet);
				}
			}
		}
		return dataSet;
	}

	/**
	 * 转换数据集{@link PaginationArrayList}为数据集
	 * 
	 * @param list 待转换的{@link PaginationArrayList}数据集
	 * @param klass 数据集中包含的数据对象的所属类
	 * @param superKlass 数据集中包含的数据对象需要解析终止于超类
	 * @param fhs 用于获取数据集中的数据对象值的field配置集合OrderedHashMap<String,
	 *            {@link FieldHelper}>，key为列名
	 * @param mode 反射对象获取列名是使用的策略，默认为解析{@link Column}模式
	 * @return this
	 */
	public static DataSet convert(PaginationArrayList<?> list, Class<?> klass, Class<?> superKlass,
			OrderedHashMap<String, FieldHelper> fhs, Mode mode) {
		if (fhs == null)
			fhs = FieldHelper.getFieldHelpers(klass, superKlass, mode);
		DataSet dataSet = new DataSet();
		for (Entry<String, FieldHelper> entry : fhs.entrySet()) {
			String columnName = entry.getKey();
			FieldHelper fh = entry.getValue();
			if (fh.isPk())
				dataSet.pkList.add(columnName);
			dataSet.fieldList.add(columnName);
		}
		dataSet.currentPage = list.getCurrentPage();
		dataSet.nextPage = list.getNextPage();
		dataSet.previousPage = list.getPrePage();
		dataSet.totalPageCount = list.getTotalPageCount();
		dataSet.totalRecordCount = list.getTotalItemCount();
		for (Object o : list) {
			if (o != null) {
				RowSet rowSet = RowSet.convert(o, fhs);
				if (rowSet.hasData()) {
					dataSet.addRowMap(rowSet);
					dataSet.rows.add(rowSet);
				}
			}
		}
		return dataSet;
	}

	/**
	 * 转换数据集{@link PaginationArrayList}为数据集
	 * 
	 * @param list 待转换的{@link PaginationArrayList}数据集
	 * @param klass 数据集中包含的数据对象的所属类
	 * @param fhs 用于获取数据集中的数据对象值的field配置集合OrderedHashMap<String,
	 *            {@link FieldHelper}>，key为列名
	 * @param mode 反射对象获取列名是使用的策略，默认为解析{@link Column}模式
	 * @return this
	 */
	public static DataSet convert(PaginationArrayList<?> list, Class<?> klass, OrderedHashMap<String, FieldHelper> fhs,
			Mode mode) {
		return convert(list, klass, klass, fhs, mode);
	}

	/**
	 * 解析{@link RecordSet}为数据集
	 * 
	 * @param recordSet {@link RecordSet}
	 * @param pks 主键
	 * @return this
	 */
	public static DataSet convert(RecordSet recordSet, String... pks) {
		DataSet dataSet = new DataSet();
		if (pks != null)
			dataSet.pkList.addArray(pks);
		dataSet.fieldList.addArray(recordSet.FIELD_NAME);
		dataSet.currentPage = recordSet.CURRENT_PAGE;
		dataSet.nextPage = recordSet.CURRENT_PAGE == recordSet.TOTAL_PAGE ? recordSet.CURRENT_PAGE
				: recordSet.CURRENT_PAGE + 1;
		dataSet.previousPage = recordSet.CURRENT_PAGE == 1 ? 1 : recordSet.CURRENT_PAGE - 1;
		dataSet.totalPageCount = recordSet.TOTAL_PAGE;
		dataSet.totalRecordCount = recordSet.TOTAL_RECORD_COUNT;
		while (recordSet.next()) {
			RowSet rowSet = RowSet.convert(recordSet);
			if (rowSet.hasData()) {
				dataSet.addRowMap(rowSet);
				dataSet.rows.add(rowSet);
			}
		}
		return dataSet;
	}

	/**
	 * 反射对象获取列名是使用的策略，默认为解析{@link Column}模式
	 */
	public static Mode DEFAULT_MODE = Mode.COLUMN_ANNOTATION;

	/**
	 * 对于{@link #convert(List)}, {@link #convert(Map))}使用的默认键的列名
	 */
	public static final String DEFAULT_KEY_COLUMN_NAME = "key";

	/**
	 * 对于{@link #convert(List)}, {@link #convert(Map))}使用的默认值的列名
	 */
	public static final String DEFAULT_VALUE_COLUMN_NAME = "value";

}
