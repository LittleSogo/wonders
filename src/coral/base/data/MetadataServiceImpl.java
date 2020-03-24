package coral.base.data;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wfc.service.database.DB;
import wfc.service.database.SQL;
import wfc.service.util.OrderedHashSet;

import common.base.app.AppContext;

@Service
@Transactional
public class MetadataServiceImpl implements MetadataService {

	private String dataType2DatabaseType(String dataType) {
		if (DB.DB_ORACLE.equals(AppContext.databaseName)) {
			if (MetadataConstant.DATA_TYPE_STRING.equals(dataType)) {
				return "varchar2";
			} else if (MetadataConstant.DATA_TYPE_NUMBER.equals(dataType)) {
				return "number";
			} else if (MetadataConstant.DATA_TYPE_TIMESTAMP.equals(dataType)) {
				return "timestamp";
			} else {
				throw new MetadataException("不支持的字段数据类型");
			}
		} else if (DB.DB_POSTGRESQL.equals(AppContext.databaseName)) {
			if (MetadataConstant.DATA_TYPE_STRING.equals(dataType)) {
				return "varchar";
			} else if (MetadataConstant.DATA_TYPE_NUMBER.equals(dataType)) {
				return "decimal";
			} else if (MetadataConstant.DATA_TYPE_TIMESTAMP.equals(dataType)) {
				return "timestamp";
			} else {
				throw new MetadataException("不支持的字段数据类型");
			}
		} else {
			throw new MetadataException("不支持的数据库类型");
		}
	}

	private String databaseType2DataType(int databaseType) {
		switch (databaseType) {
		case Types.DECIMAL:
		case Types.DOUBLE:
		case Types.FLOAT:
		case Types.NUMERIC:
		case Types.INTEGER:
		case Types.BIGINT:
			return MetadataConstant.DATA_TYPE_NUMBER;
		case Types.DATE:
		case Types.TIME:
		case Types.TIMESTAMP:
			return MetadataConstant.DATA_TYPE_TIMESTAMP;
		case Types.NVARCHAR:
		case Types.VARCHAR:
		case Types.CHAR:
		default:
			return MetadataConstant.DATA_TYPE_STRING;
		}
	}

	public MetadataTable extractMetadataFromDatabase(String tableName) {
		Connection con = DB.getConnection();
		try {
			MetadataTable mt = new MetadataTable();
			mt.setName(tableName);

			DatabaseMetaData dmd = con.getMetaData();
			String schema = null;
			if (DB.DB_ORACLE.equals(dmd.getDatabaseProductName())) {
				schema = dmd.getUserName();
			}

			// 获取表格主键的相关信息
			Set<String> pkSet = new HashSet<String>();
			ResultSet pkRs = dmd.getPrimaryKeys(null, schema, tableName);
			while (pkRs.next()) {
				pkSet.add(pkRs.getString("COLUMN_NAME"));
			}
			pkRs.close();

			// 获取表格普通字段信息
			ResultSet columnRs = dmd.getColumns(null, schema, tableName, null);
			while (columnRs.next()) {
				MetadataColumn mc = new MetadataColumn();
				String columnName = columnRs.getString("COLUMN_NAME");
				mc.setName(columnName);
				if (pkSet.contains(columnName)) {
					mc.setPk(true);
				} else {
					mc.setPk(false);
				}
				int type = columnRs.getInt("DATA_TYPE");
				mc.setType(databaseType2DataType(type));
				int length = columnRs.getInt("COLUMN_SIZE");
				mc.setLength(length);
				mt.add(mc);
			}

			if (mt.size() == 0) {
				return null;
			} else {
				return mt;
			}
		} catch (SQLException ex) {
			throw new MetadataException(ex);
		} finally {
			DB.closeConnection(con);
		}
	}

	public void applyMetadataToDatabase(MetadataTable mt) {
		String tableName = mt.getName();
		// 判断数据库表是否存在
		MetadataTable preMt = extractMetadataFromDatabase(tableName);
		if (preMt == null) { // 如果不存在，添加该数据库表
			StringBuffer sql = new StringBuffer();
			StringBuffer subSql = new StringBuffer();
			sql.append("create table ").append(tableName).append(" (");
			for (MetadataColumn mc : mt) {
				sql.append(mc.getName()).append(" ").append(
						dataType2DatabaseType(mc.getType()));
				if (MetadataConstant.DATA_TYPE_STRING.equals(mc.getType())) {
					sql.append("(" + mc.getLength() + ")");
				}
				if (mc.isPk()) {
					sql.append(" not null");
				}
				sql.append(",\r\n");
				if (mc.isPk()) {
					if (subSql.length() > 0) {
						subSql.append(",");
					}
					subSql.append(mc.getName());
				}
			}
			sql.append(" constraint PK_").append(tableName).append(
					" primary key (").append(subSql).append(")\r\n)");
			SQL.execute(sql.toString());
		} else { // 如果数据库表存在，则比较后再处理
			Map<String, MetadataColumn> preMcMap = new HashMap<String, MetadataColumn>();
			for (MetadataColumn preMc : preMt) {
				preMcMap.put(preMc.getName(), preMc);
			}
			for (MetadataColumn mc : mt) {
				if (mc.isPk()) {
					continue;
				}
				String columnName = mc.getName();
				String op;
				if (preMcMap.containsKey(columnName)) {
					MetadataColumn preMc = preMcMap.get(columnName);
					if (MetadataConstant.DATA_TYPE_STRING.equals(mc.getType())
							&& mc.getLength() > preMc.getLength()) {
						op = "modify";
					} else {
						continue;
					}
				} else {
					op = "add";
				}
				StringBuffer sql = new StringBuffer();
				sql.append("alter table ").append(tableName).append(" ")
						.append(op).append(" ").append(columnName).append(" ")
						.append(dataType2DatabaseType(mc.getType()));
				if (MetadataConstant.DATA_TYPE_STRING.equals(mc.getType())) {
					sql.append("(" + mc.getLength() + ")");
				}
				SQL.execute(sql.toString());
			}
		}
	}

	public OrderedHashSet<String> getPkList(MetadataTable mt) {
		OrderedHashSet<String> pkList = new OrderedHashSet<String>();
		for (MetadataColumn mc : mt) {
			if (mc.isPk()) {
				pkList.add(mc.getName());
			}
		}
		return pkList;
	}

}
