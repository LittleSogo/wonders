package coral.base.data;

import java.math.BigDecimal;
import java.sql.Timestamp;

import wfc.service.database.Condition;
import wfc.service.database.Conditions;
import wfc.service.util.DateTimeHelper;

public abstract class Transformer4Base {

	public static final String EQUAL_CONDITION_SUFFIX = "_equal";

	public static final String GREATER_CONDITION_SUFFIX = "_greater";

	public static final String LESS_CONDITION_SUFFIX = "_less";

	abstract public String getParameter(String name);

	abstract public String[] getParameterValues(String name);

	public DataRow toDataRow(DataRow dataRow, MetadataTable mt) {
		for (MetadataColumn mc : mt) {
			String columnName = mc.getName();
			String type = mc.getType();
			String value = getParameter(columnName);
			if (value != null) {
				if ("".equals(value.trim())) {
					dataRow.put(columnName, null);
				} else {
					if (MetadataConstant.DATA_TYPE_STRING.equals(type)) {
						dataRow.put(columnName, value);
					} else if (MetadataConstant.DATA_TYPE_NUMBER.equals(type)) {
						dataRow.put(columnName, new BigDecimal(value));
					} else if (MetadataConstant.DATA_TYPE_TIMESTAMP
							.equals(type)) {
						long date = Long.parseLong(value);
						dataRow.put(columnName, new Timestamp(date));
					}
				}
			}
		}
		return dataRow;
	}

	public DataRow toDataRow(MetadataTable mt) {
		DataRow dataRow = new DataRow();
		for (MetadataColumn mc : mt) {
			String columnName = mc.getName();
			if (mc.isPk()) {
				dataRow.getPkList().add(columnName);
			}
		}
		return toDataRow(dataRow, mt);
	}

	public Conditions toConditions(MetadataTable mt) {
		Conditions conds = Conditions.newAndConditions();
		for (MetadataColumn mc : mt) {
			String columnName = mc.getName();
			String type = mc.getType();
			String[] value = getParameterValues(columnName);
			if (value != null) {
				if (MetadataConstant.DATA_TYPE_STRING.equals(type)) {
					if (value.length == 1) {
						if (!"".equals(value[0])) {
							if (getParameter(columnName
									+ EQUAL_CONDITION_SUFFIX) != null) {
								conds.add(new Condition(columnName,
										Condition.OT_EQUAL, value[0]));
							} else {
								conds.add(new Condition(columnName,
										Condition.OT_LIKE, value[0]));
							}
						}
					} else {
						Conditions subconds = Conditions.newOrConditions();
						for (int j = 0; j < value.length; j++) {
							if (!"".equals(value[j])) {
								subconds.add(new Condition(columnName,
										Condition.OT_EQUAL, value[j]));
							}
						}
						conds.add(subconds);
					}
				} else if (MetadataConstant.DATA_TYPE_NUMBER.equals(type)) {
					if (value.length == 1) {
						if (!"".equals(value[0])) {
							if (getParameter(columnName
									+ GREATER_CONDITION_SUFFIX) != null) {
								conds.add(new Condition(columnName,
										Condition.OT_GREATER_EQUAL,
										new BigDecimal(value[0])));
							} else if (getParameter(columnName
									+ LESS_CONDITION_SUFFIX) != null) {
								conds.add(new Condition(columnName,
										Condition.OT_LESS_EQUAL,
										new BigDecimal(value[0])));
							} else {
								conds.add(new Condition(columnName,
										Condition.OT_EQUAL, new BigDecimal(
												value[0])));
							}
						}
					} else if (value.length == 2) {
						if (!"".equals(value[0])) {
							conds.add(new Condition(columnName,
									Condition.OT_GREATER_EQUAL, new BigDecimal(
											value[0])));
						}
						if (!"".equals(value[1])) {
							conds.add(new Condition(columnName,
									Condition.OT_LESS_EQUAL, new BigDecimal(
											value[1])));
						}
					} else {
						Conditions subconds = Conditions.newOrConditions();
						for (int j = 0; j < value.length; j++) {
							if (!"".equals(value[j])) {
								subconds.add(new Condition(columnName,
										Condition.OT_EQUAL, new BigDecimal(
												value[j])));
							}
						}
						conds.add(subconds);
					}
				} else if (MetadataConstant.DATA_TYPE_TIMESTAMP.equals(type)) {
					if (value.length == 1) {
						if (!"".equals(value[0])) {
							long date = Long.parseLong(value[0]);
							Timestamp ts = new Timestamp(date);
							Timestamp startTs = new Timestamp(DateTimeHelper
									.getMinDate(ts).getTime());
							Timestamp endTs = new Timestamp(DateTimeHelper
									.getMaxDate(ts).getTime() + 1);
							if (getParameter(columnName
									+ GREATER_CONDITION_SUFFIX) != null) {
								conds.add(new Condition(columnName,
										Condition.OT_GREATER_EQUAL, startTs));
							} else if (getParameter(columnName
									+ LESS_CONDITION_SUFFIX) != null) {
								conds.add(new Condition(columnName,
										Condition.OT_LESS, endTs));
							} else {
								conds.add(new Condition(columnName,
										Condition.OT_GREATER_EQUAL, startTs));
								conds.add(new Condition(columnName,
										Condition.OT_LESS, endTs));
							}
						}
					} else if (value.length == 2) {
						if (value[0] != null) {
							long date = Long.parseLong(value[0]);
							Timestamp ts = new Timestamp(date);
							Timestamp startTs = new Timestamp(DateTimeHelper
									.getMinDate(ts).getTime());
							conds.add(new Condition(columnName,
									Condition.OT_GREATER_EQUAL, startTs));
						}
						if (value[1] != null) {
							long date = Long.parseLong(value[1]);
							Timestamp ts = new Timestamp(date);
							Timestamp endTs = new Timestamp(DateTimeHelper
									.getMaxDate(ts).getTime() + 1);
							conds.add(new Condition(columnName,
									Condition.OT_LESS, endTs));
						}
					} else {
						throw new RuntimeException("日期类型不支持或查询#" + columnName);
					}
				}
			}
		}
		return conds;
	}

}
