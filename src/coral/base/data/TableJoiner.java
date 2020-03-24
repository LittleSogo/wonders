/**
 * Project: Coral
 * Source file: TableUtils.java 
 * Create At 2011-9-23 下午05:21:21
 * Create By 龚云
 */
package coral.base.data;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import wfc.service.database.Conditions;
import wfc.service.database.DB;
import wfc.service.database.RecordSet;
import wfc.service.database.SQL;
import wfc.service.util.OrderedHashSet;

/**
 * 连接查询sql生成器
 * 
 * @author 龚云
 * 
 */
public class TableJoiner {

	public TableJoiner(Class<?> klass1, Class<?> klass2, String column) {
		add(klass1, klass2, column);
	}

	public TableJoiner(Class<?> klass1, String column1, Class<?> klass2,
			String column2) {
		add(klass1, column1, klass2, column2, null);
	}

	public TableJoiner(Class<?> klass1, String column1, Class<?> klass2,
			String column2, String columnAlias) {
		add(klass1, column1, klass2, column2, columnAlias);
	}

	public TableJoiner add(Class<?> klass1, Class<?> klass2, String column) {
		return add(klass1, column, klass2, column, null);
	}

	public TableJoiner add(Class<?> klass1, String column1, Class<?> klass2,
			String column2) {
		return add(klass1, column1, klass2, column2, null);
	}

	public TableJoiner add(Class<?> klass1, String column1, Class<?> klass2,
			String column2, String columnAlias) {
		JoinColumnInfo joinColumnInfo = new JoinColumnInfo(klass1, column1,
				klass2, column2);
		Set<Class<?>> joinClassSet = new HashSet<Class<?>>();
		joinClassSet.add(klass1);
		joinClassSet.add(klass2);
		joinColumnInfoSet.add(joinColumnInfo);
		classSet.addAll(joinClassSet);
		addToTableMap(klass1);
		addToTableMap(klass2);
		// 连接字段的别名
		addColumnAlias(klass1, column1, columnAlias);
		addColumnAlias(klass2, column2, columnAlias);
		return this;
	}

	public TableJoiner addColumnAlias(Class<?> klass, String column,
			String alias) {
		if (StringUtils.trimToEmpty(column).isEmpty()
				|| StringUtils.trimToEmpty(alias).isEmpty())
			return this;
		String tableCol = getTableColumnStr(tableMap.get(klass), column);
		columnAliasMap.put(tableCol, alias);
		return this;
	}

	public void addCustomCols(String... columns) {
		if (columns != null) {
			for (String column : columns) {
				if (StringUtils.trimToEmpty(column).isEmpty())
					continue;
				customCols.add(column);
			}
		}
	}

	public void addExcludeCols(Class<?> klass, String... columns) {
		if (columns != null) {
			for (String column : columns) {
				if (StringUtils.trimToEmpty(column).isEmpty())
					continue;
				String tableCol = getTableColumnStr(tableMap.get(klass), column);
				excludeCols.add(tableCol);
			}
		}
	}

	public void addIncludeCols(Class<?> klass, String... columns) {
		if (columns != null) {
			for (String column : columns) {
				if (StringUtils.trimToEmpty(column).isEmpty())
					continue;
				String tableCol = getTableColumnStr(tableMap.get(klass), column);
				includeCols.add(tableCol);
			}
		}
	}

	public Connection getCon() {
		return con;
	}

	public SelColumnMode getSelColumnMode() {
		return selColumnMode;
	}

	public boolean isDistinct() {
		return distinct;
	}

	public RecordSet query(Conditions conds, String suffix) {
		return query(null, conds, suffix, -1, -1);
	}

	public RecordSet query(Conditions conds, String suffix, int pageSize,
			int currentPage) {
		return query(null, conds, suffix, pageSize, currentPage);
	}

	public RecordSet query(Connection con, Conditions conds, String suffix) {
		return query(con, conds, suffix, -1, -1);
	}

	public RecordSet query(Connection con, Conditions conds, String suffix,
			int pageSize, int currentPage) {
		boolean isInnerCon = false;
		try {
			if (con == null) {
				if (this.con == null) {
					con = DB.getConnection();
					isInnerCon = true;
				} else {
					con = this.con;
				}
			}
			Object[] obj = null;
			if (conds != null) {
				obj = conds.getObjectArray();
			}
			String sql = toSqlString(conds, suffix);
			if (pageSize == -1 && currentPage == -1)
				return SQL.execute(con, sql, obj);
			return SQL.execute(con, sql, pageSize, currentPage, obj);
		} finally {
			if (isInnerCon)
				DB.closeConnection(con);
		}
	}

	public TableJoiner setCon(Connection con) {
		this.con = con;
		return this;
	}

	public TableJoiner setDistinct(boolean distinct) {
		this.distinct = distinct;
		return this;
	}

	public TableJoiner setSelColumnMode(SelColumnMode selColumnMode) {
		this.selColumnMode = selColumnMode;
		return this;
	}

	public String toSqlString() {
		String columnsStr = getColumns();
		String tableStr = getTables();
		String whereStr = getWhereStr();
		String distinctStr = distinct ? "DISTINCT " : "";
		return new StringBuffer(" SELECT ").append(distinctStr).append(
				columnsStr).append(" FROM ").append(tableStr).append(" WHERE ")
				.append(whereStr).toString();
	}

	public String toSqlString(Conditions conds, String suffix) {
		return toSqlString(toSqlString(), conds, suffix);
	}

	protected void addToTableMap(Class<?> klass) {
		if (tableMap.containsKey(klass))
			return;
		Table table = klass.getAnnotation(Table.class);
		if (table == null || StringUtils.trimToEmpty(table.name()).isEmpty())
			throw new IllegalArgumentException(
					"添加的类必须标注javax.persistence.Table，且表名不能为空");
		tableMap.put(klass, table.name());
	}

	protected String getColumns() {
		OrderedHashSet<String> columnSet = new OrderedHashSet<String>();
		if (selColumnMode == SelColumnMode.CUSTOM) {
			columnSet.addAll(customCols);
		} else {
			Set<String> readedColumnSet = new HashSet<String>();
			Set<String> readedAliasSet = new HashSet<String>();
			for (Class<?> klass : classSet) {
				Field[] fields = klass.getDeclaredFields();
				for (Field f : fields) {
					Column c = f.getAnnotation(Column.class);
					if (c != null) {
						String annoColName = c.name();
						String colName = annoColName;
						String tableColName = getTableColumnStr(tableMap
								.get(klass), colName);
						colName = tableColName;
						boolean isAlias = false;
						if (columnAliasMap.containsKey(tableColName)) {
							String alias = StringUtils
									.trimToEmpty(columnAliasMap
											.get(tableColName));
							if (!alias.isEmpty()) {
								colName = colName + " AS " + alias;
								if (readedAliasSet.contains(alias))
									continue;
								readedAliasSet.add(alias);
								isAlias = true;
							}
						}
						boolean toAdd = false;
						switch (this.selColumnMode) {
							case INCLUDE:
								if (includeCols.isEmpty()
										|| includeCols.contains(tableColName)) {
									toAdd = true;
								}
								break;
							case EXCLUDE:
								if (excludeCols.isEmpty()
										|| !excludeCols.contains(tableColName)) {
									toAdd = true;
								}
								break;
							case ALL:
								toAdd = true;
								break;
						}
						if (toAdd) {
							if (!isAlias) {
								if (readedColumnSet.contains(annoColName))
									continue;
								readedColumnSet.add(annoColName);
							}
							columnSet.add(colName);
						}
					}
				}
			}
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < columnSet.size(); i++) {
			if (sb.length() > 0)
				sb.append(SPLIT_CHAR);
			sb.append(columnSet.get(i));
		}
		return sb.toString();
	}

	protected String getTables() {
		StringBuffer sb = new StringBuffer();
		for (Entry<Class<?>, String> tableMapEntry : tableMap.entrySet()) {
			String tableName = tableMapEntry.getValue();
			if (sb.length() > 0)
				sb.append(SPLIT_CHAR);
			sb.append(tableName);
		}
		return sb.toString();
	}

	protected String getWhereStr() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < joinColumnInfoSet.size(); i++) {
			JoinColumnInfo jc = joinColumnInfoSet.get(i);
			if (sb.length() > 0)
				sb.append(WHERE_AND);
			String colCondStr1 = getTableColumnStr(
					tableMap.get(jc.tableClass1), jc.column1);
			String colCondStr2 = getTableColumnStr(
					tableMap.get(jc.tableClass2), jc.column2);
			sb.append(" ").append(colCondStr1).append(" = ")
					.append(colCondStr2);
		}
		return sb.toString();
	}

	private String getTableColumnStr(String tableName, String columnName) {
		return new StringBuffer(tableName).append(DOT_CHAR).append(columnName)
				.toString();
	}

	protected Set<Class<?>> classSet = new HashSet<Class<?>>();

	protected Map<String, String> columnAliasMap = new HashMap<String, String>();

	protected OrderedHashSet<JoinColumnInfo> joinColumnInfoSet = new OrderedHashSet<JoinColumnInfo>();

	protected Map<Class<?>, String> tableMap = new HashMap<Class<?>, String>();

	private Connection con;

	private Set<String> excludeCols = new HashSet<String>();

	private Set<String> includeCols = new HashSet<String>();

	private Set<String> customCols = new HashSet<String>();

	private SelColumnMode selColumnMode = SelColumnMode.ALL;

	private boolean distinct = false;

	public static String toSqlString(String sql, Conditions conds, String suffix) {
		if (suffix == null)
			suffix = EMPTY;
		String condsStr = EMPTY;
		Conditions andConds = Conditions.newAndConditions();
		if (conds != null) {
			andConds.add(conds);
			condsStr = andConds.toString();
		}
		return new StringBuffer(sql)
				// conditions
				.append(condsStr.isEmpty() ? EMPTY : WHERE_AND)
				.append(condsStr)
				// suffix
				.append(" ").append(suffix).toString();
	}

	protected static final String DOT_CHAR = ".";

	protected static final String EMPTY = StringUtils.EMPTY;

	protected static final String SPLIT_CHAR = ", ";

	protected static final String WHERE_AND = " AND ";

	public static enum SelColumnMode {
		ALL, EXCLUDE, INCLUDE, CUSTOM;
	}

	protected static class JoinColumnInfo {
		public JoinColumnInfo(Class<?> tableClass1, String column1,
				Class<?> tableClass2, String column2) {
			this.column1 = StringUtils.trimToEmpty(column1);
			this.column2 = StringUtils.trimToEmpty(column2);
			this.tableClass1 = tableClass1;
			this.tableClass2 = tableClass2;
			if (this.column1.isEmpty() || this.column2.isEmpty()
					|| this.tableClass1 == null || this.tableClass2 == null)
				throw new NullPointerException(
						"All of the arguments cannot be null.");
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (!(obj instanceof JoinColumnInfo))
				return false;
			JoinColumnInfo o = (JoinColumnInfo) obj;
			if (!o.column1.equals(column1))
				return false;
			if (!o.column2.equals(column2))
				return false;
			if (!o.tableClass1.equals(tableClass1))
				return false;
			if (!o.tableClass2.equals(tableClass2))
				return false;
			return true;
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			int h = 0;
			h += column1.hashCode();
			h += column2.hashCode();
			h += tableClass1.hashCode();
			h += tableClass2.hashCode();
			return h;
		}

		String column1;

		String column2;

		Class<?> tableClass1;

		Class<?> tableClass2;
	}

}
