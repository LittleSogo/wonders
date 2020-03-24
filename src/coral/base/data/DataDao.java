package coral.base.data;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import wfc.service.database.Condition;
import wfc.service.database.Conditions;
import wfc.service.database.RecordSet;
import wfc.service.database.SQL;
import wfc.service.util.OrderedHashSet;

public class DataDao {

	private Connection con = null;

	private String tableName;

	public DataDao(String tableName) {
		this.tableName = tableName;
	}

	public DataDao(Connection con, String tableName) {
		this.con = con;
		this.tableName = tableName;
	}

	public void add(DataRow dataRow) {
		StringBuffer fields = new StringBuffer();
		StringBuffer values = new StringBuffer();
		List<Object> valueList = new ArrayList<Object>();
		for (int i = 0; i < dataRow.size(); i++) {
			String fieldName = dataRow.getKey(i);
			Object fieldValue = dataRow.getValue(i);
			if (i > 0) {
				fields.append(",");
				values.append(",");
			}
			fields.append(fieldName);
			values.append("?");
			valueList.add(fieldValue);
		}
		String sql = "insert into " + tableName + "(" + fields.toString()
				+ ") values (" + values.toString() + ")";
		if (con == null) {
			SQL.execute(sql, valueList.toArray());
		} else {
			SQL.execute(con, sql, valueList.toArray());
		}
	}

	public void update(DataRow dataRow) {
		Conditions conds = Conditions.newAndConditions();
		OrderedHashSet<String> pkList = dataRow.getPkList();
		for (int i = 0; i < pkList.size(); i++) {
			String pk = pkList.get(i);
			conds.add(new Condition(pk, Condition.OT_EQUAL, dataRow.get(pk)));
		}
		update(dataRow, conds);
	}

	public int update(DataRow dataRow, Conditions conds) {
		StringBuffer fieldValues = new StringBuffer();
		List<Object> valueList = new ArrayList<Object>();
		for (int i = 0; i < dataRow.size(); i++) {
			String fieldName = dataRow.getKey(i);
			Object fieldValue = dataRow.getValue(i);
			if (i > 0) {
				fieldValues.append(",");
			}
			fieldValues.append(fieldName + " = ?");
			valueList.add(fieldValue);
		}
		String sql;
		if (conds == null || "".equals(conds.toString())) {
			sql = "update " + tableName + " set " + fieldValues;

		} else {
			sql = "update " + tableName + " set " + fieldValues + " where "
					+ conds.toString();
			valueList.addAll(conds.getObjectList());
		}
		if (con == null) {
			return SQL.execute(sql, valueList.toArray()).TOTAL_RECORD_COUNT;
		} else {
			return SQL.execute(con, sql, valueList.toArray()).TOTAL_RECORD_COUNT;
		}
	}

	public int delete(Conditions conds) {
		String sql;
		if (conds == null || "".equals(conds.toString())) {
			sql = "delete from " + tableName;
			if (con == null) {
				return SQL.execute(sql).TOTAL_RECORD_COUNT;
			} else {
				return SQL.execute(con, sql).TOTAL_RECORD_COUNT;
			}
		} else {
			sql = "delete from " + tableName + " where " + conds.toString();
			if (con == null) {
				return SQL.execute(sql, conds.getObjectArray()).TOTAL_RECORD_COUNT;
			} else {
				return SQL.execute(con, sql, conds.getObjectArray()).TOTAL_RECORD_COUNT;
			}
		}

	}

	public void delete(DataRow... dataRows) {
		Conditions conds = Conditions.newOrConditions();
		for (DataRow dataRow : dataRows) {
			Conditions subConds = Conditions.newAndConditions();
			OrderedHashSet<String> pkList = dataRow.getPkList();
			for (int i = 0; i < pkList.size(); i++) {
				String pk = pkList.get(i);
				subConds.add(new Condition(pk, Condition.OT_EQUAL, dataRow
						.get(pk)));
			}
			conds.add(conds);
		}
		delete(conds);
	}

	public void delete(DataRow dataRow) {
		Conditions conds = Conditions.newAndConditions();
		OrderedHashSet<String> pkList = dataRow.getPkList();
		for (int i = 0; i < pkList.size(); i++) {
			String pk = pkList.get(i);
			conds.add(new Condition(pk, Condition.OT_EQUAL, dataRow.get(pk)));
		}
		delete(conds);
	}

	public DataPage query(String fieldNames, Conditions conds, String suffix,
			int pageSize, int pageIndex) {
		RecordSet rs;
		if (con == null) {
			rs = SQL.query(tableName, fieldNames, conds, suffix, pageSize,
					pageIndex);
		} else {
			rs = SQL.query(con, tableName, fieldNames, conds, suffix, pageSize,
					pageIndex);
		}
		DataPage dataPage = new DataPage(rs.CURRENT_PAGE, rs.CURRENT_PAGE_SIZE,
				rs.TOTAL_RECORD_COUNT);
		OrderedHashSet<String> fieldList = dataPage.getFieldList();
		for (int i = 0; i < rs.FIELD_NAME.length; i++) {
			fieldList.add(rs.FIELD_NAME[i]);
		}
		while (rs.next()) {
			DataRow dataRow = new DataRow();
			for (int i = 0; i < rs.FIELD_NAME.length; i++) {
				Object obj = rs.getObject(rs.FIELD_NAME[i]);
				dataRow.set(rs.FIELD_NAME[i], obj);
			}
			dataPage.add(dataRow);
		}
		return dataPage;
	}

	public DataPage query(Conditions conds, String suffix, int pageSize,
			int pageIndex) {
		return query("*", conds, suffix, pageSize, pageIndex);
	}

	public List<DataRow> query(Conditions conds, String suffix) {
		RecordSet rs;
		if (con == null) {
			rs = SQL.query(tableName, "*", conds, suffix);
		} else {
			rs = SQL.query(con, tableName, "*", conds, suffix);
		}
		List<DataRow> dataRowList = new ArrayList<DataRow>();
		while (rs.next()) {
			DataRow dataRow = new DataRow();
			for (int i = 0; i < rs.FIELD_NAME.length; i++) {
				Object obj = rs.getObject(rs.FIELD_NAME[i]);
				if (obj instanceof String) {
					obj = rs.getString(rs.FIELD_NAME[i]);
				} else if (obj instanceof Double || obj instanceof Float
						|| obj instanceof Integer || obj instanceof Short) {
					double number = rs.getNumber(rs.FIELD_NAME[i]);
					obj = new BigDecimal(number + "");
				} else if (obj instanceof Timestamp) {
					obj = rs.getTimestamp(rs.FIELD_NAME[i]);
				} else if (obj instanceof Blob || obj instanceof Clob) {
					continue;
				} else if (obj == null) {
					;
				} else {
					throw new DataException("不能识别的数据类型："
							+ obj.getClass().getName());
				}
				dataRow.set(rs.FIELD_NAME[i], obj);
			}
			dataRowList.add(dataRow);
		}
		return dataRowList;
	}

	public DataRow get(DataRow dataRow) {
		Conditions conds = Conditions.newAndConditions();
		OrderedHashSet<String> pkList = dataRow.getPkList();
		for (int i = 0; i < pkList.size(); i++) {
			String pk = pkList.get(i);
			conds.add(new Condition(pk, Condition.OT_EQUAL, dataRow.get(pk)));
		}
		List<DataRow> dataRowList = query(conds, null);
		if (dataRowList.size() > 0) {
			return dataRowList.get(0);
		} else {
			return null;
		}
	}

}
