/**
 * Project: Coral
 * Source file: WfcTransformer4Base.java 
 * Create At 2011-8-10 下午02:23:22
 * Create By 龚云
 */
package coral.base.data;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.persistence.Column;

import wfc.facility.tool.autocode.InternalDaoGenerator;
import wfc.facility.tool.autocode.Transformer4Base;
import wfc.service.database.Condition;
import wfc.service.database.Conditions;
import wfc.service.util.DateTimeHelper;

/**
 * @author 龚云
 * 
 */
public abstract class WfcTransformer4Base extends Transformer4Base {

	public Object toBean4MultiSuperClasses(Class<?> objClass, Class<?>[] clazz) {
		return toBeans4MultiSuperClasses(1, objClass, clazz)[0];
	}

	public Object toBean4MultiSuperClasses(Object bean, Class<?> objClass,
			Class<?>[] clazz) {
		return toBeans4MultiSuperClasses(new Object[] { bean }, objClass, clazz)[0];
	}

	/**
	 * 构造若干个 Bean 对象
	 * 
	 * @param beans 原始的 Bean 对象们
	 * @param clazz Bean 对象的类
	 * @return 最后生成的 Bean 对象们
	 */
	@Override
	public Object[] toBeans(Object[] beans, Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				Column column = (Column) fields[i].getAnnotation(Column.class);
				String fieldName = column.name();
				Class<?> fieldTypeClass = fields[i].getType();
				Object[] fieldValueObjs = this.getFieldValueObjects(fieldName,
						fieldTypeClass, beans.length);
				if (fieldValueObjs != null) {
					String setMethodName = "set"
							+ InternalDaoGenerator
									.getFieldName(fieldName, true);
					Method setMethod = clazz.getMethod(setMethodName,
							fieldTypeClass);
					for (int j = 0; j < beans.length; j++) {
						setMethod.invoke(beans[j], fieldValueObjs[j]);
					}
				}
			} catch (Exception ex) {
				continue;
			}
		}
		return beans;
	}

	public Object[] toBeans4MultiSuperClasses(int count, Class<?> objClass,
			Class<?>[] clazz) {
		if (clazz != null) {
			Object[] beans = (Object[]) Array.newInstance(objClass, count);
			for (int i = 0; i < count; i++) {
				try {
					if (beans[i] == null)
						beans[i] = objClass.newInstance();
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
			return toBeans4MultiSuperClasses(beans, objClass, clazz);
		}
		return null;
	}

	public Object[] toBeans4MultiSuperClasses(Object[] beans,
			Class<?> objClass, Class<?>[] clazz) {
		if (clazz != null) {
			for (Class<?> klass : clazz) {
				toBeans(beans, klass);
			}
		}
		return toBeans(beans, objClass);
	}

	@Override
	public Conditions toConditions(Class<?> clazz, String prefix) {
		Conditions conds = Conditions.newAndConditions();
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Column column = (Column) fields[i].getAnnotation(Column.class);
			if (column == null) {
				continue;
			}
			String fieldName = column.name();
			String fieldName2 = (prefix == null ? fieldName : prefix + "."
					+ fieldName);
			Class<?> fieldTypeClass = fields[i].getType();
			Object[] fieldValueObjs = this.getFieldValueObjects(fieldName,
					fieldTypeClass);
			if (fieldValueObjs != null) {
				if (fieldValueObjs.length == 0) {
				} else if (BigDecimal.class.equals(fieldTypeClass)) {
					if (fieldValueObjs.length == 1) {
						if (fieldValueObjs[0] != null) {
							if (getParameter(fieldName
									+ GREATER_CONDITION_SUFFIX) != null) {
								conds.add(new Condition(fieldName2,
										Condition.OT_GREATER_EQUAL,
										fieldValueObjs[0]));
							} else if (getParameter(fieldName
									+ LESS_CONDITION_SUFFIX) != null) {
								conds.add(new Condition(fieldName2,
										Condition.OT_LESS_EQUAL,
										fieldValueObjs[0]));
							} else {
								conds.add(new Condition(fieldName2,
										Condition.OT_EQUAL, fieldValueObjs[0]));
							}
						}
					} else if (fieldValueObjs.length == 2) {
						if (fieldValueObjs[0] != null) {
							conds.add(new Condition(fieldName2,
									Condition.OT_GREATER_EQUAL,
									fieldValueObjs[0]));
						}
						if (fieldValueObjs[1] != null) {
							conds
									.add(new Condition(fieldName2,
											Condition.OT_LESS_EQUAL,
											fieldValueObjs[1]));
						}
					} else {
						Conditions subconds = Conditions.newOrConditions();
						for (int j = 0; j < fieldValueObjs.length; j++) {
							subconds.add(new Condition(fieldName2,
									Condition.OT_EQUAL, fieldValueObjs[j]));
						}
						conds.add(subconds);
					}
				} else if (Timestamp.class.equals(fieldTypeClass)) {
					if (fieldValueObjs.length == 1) {
						if (fieldValueObjs[0] != null) {
							Timestamp startTs = null, endTs = null;
							if (fieldValueObjs[0] instanceof Long) {
								startTs = new Timestamp(
										(Long) fieldValueObjs[0]);
								endTs = new Timestamp(DateTimeHelper
										.getMaxDate(startTs).getTime() + 1);
							} else {
								Timestamp ts = (Timestamp) fieldValueObjs[0];
								startTs = new Timestamp(DateTimeHelper
										.getMinDate(ts).getTime());
								endTs = new Timestamp(DateTimeHelper
										.getMaxDate(ts).getTime() + 1);
							}
							if (getParameter(fieldName
									+ GREATER_CONDITION_SUFFIX) != null) {
								conds.add(new Condition(fieldName2,
										Condition.OT_GREATER_EQUAL, startTs));
							} else if (getParameter(fieldName
									+ LESS_CONDITION_SUFFIX) != null) {
								conds.add(new Condition(fieldName2,
										Condition.OT_LESS, endTs));
							} else {
								conds.add(new Condition(fieldName2,
										Condition.OT_GREATER_EQUAL, startTs));
								conds.add(new Condition(fieldName2,
										Condition.OT_LESS, endTs));
							}
						}
					} else if (fieldValueObjs.length == 2) {
						if (fieldValueObjs[0] != null) {
							Timestamp startTs = null;
							if (fieldValueObjs[0] instanceof Long) {
								startTs = new Timestamp(
										(Long) fieldValueObjs[0]);
							} else {
								Timestamp ts = (Timestamp) fieldValueObjs[0];
								startTs = new Timestamp(DateTimeHelper
										.getMinDate(ts).getTime());
							}
							conds.add(new Condition(fieldName2,
									Condition.OT_GREATER_EQUAL, startTs));
						}
						if (fieldValueObjs[1] != null) {
							Timestamp endTs = null;
							if (fieldValueObjs[1] instanceof Long) {
								endTs = new Timestamp((Long) fieldValueObjs[1]);
							} else {
								Timestamp ts = (Timestamp) fieldValueObjs[1];
								endTs = new Timestamp(DateTimeHelper
										.getMaxDate(ts).getTime() + 1);
							}
							conds.add(new Condition(fieldName2,
									Condition.OT_LESS, endTs));
						}
					} else {
						throw new RuntimeException("日期类型不支持或查询#" + fieldName);
					}
				} else {
					if (fieldValueObjs.length == 1) {
						if (fieldValueObjs[0] != null) {
							if (getParameter(fieldName + EQUAL_CONDITION_SUFFIX) != null) {
								conds.add(new Condition(fieldName2,
										Condition.OT_EQUAL, fieldValueObjs[0]));
							} else {
								conds.add(new Condition(fieldName2,
										Condition.OT_LIKE, fieldValueObjs[0]));
							}
						}
					} else {
						Conditions subconds = Conditions.newOrConditions();
						for (int j = 0; j < fieldValueObjs.length; j++) {
							if (fieldValueObjs[j] != null) {
								subconds.add(new Condition(fieldName2,
										Condition.OT_EQUAL, fieldValueObjs[j]));
							}
						}
						conds.add(subconds);
					}
				}
			}
		}
		return conds;
	}

	protected Object[] getFieldValueObjects(String fieldName,
			Class<?> fieldTypeClass) {
		String[] value = getParameterValues(fieldName);
		if (value == null) {
			return null;
		} else {
			Object[] objs = (Object[]) Array.newInstance(Object.class,
					value.length);
			for (int i = 0; i < objs.length; i++) {
				if ("".equals(value[i])) {
					objs[i] = null;
				} else if (BigDecimal.class.equals(fieldTypeClass)) {
					objs[i] = new BigDecimal(value[i]);
				} else if (Timestamp.class.equals(fieldTypeClass)) {
					try {
						Long dateTime = Long.parseLong(value[i]);
						objs[i] = new Timestamp(dateTime);
					} catch (NumberFormatException e) {
						try {
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd");
							objs[i] = new Timestamp(sdf.parse(value[i])
									.getTime());
						} catch (ParseException ex) {
							throw new RuntimeException(ex);
						}
					}
				} else {
					objs[i] = value[i];
				}
			}
			return objs;
		}
	}

	protected Object[] getFieldValueObjects(String fieldName,
			Class<?> fieldTypeClass, int count) {
		String[] value = getParameterValues(fieldName);
		if (value == null) {
			return null;
		} else {
			Object[] objs = (Object[]) Array.newInstance(Object.class, count);
			if (value.length < count) {
				Object obj;
				if ("".equals(value[0])) {
					obj = null;
				} else if (BigDecimal.class.equals(fieldTypeClass)) {
					obj = new BigDecimal(value[0]);
				} else if (Timestamp.class.equals(fieldTypeClass)) {
					try {
						Long dateTime = Long.parseLong(value[0]);
						obj = new Timestamp(dateTime);
					} catch (NumberFormatException e) {
						try {
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd");
							obj = new Timestamp(sdf.parse(value[0]).getTime());
						} catch (ParseException ex) {
							throw new RuntimeException(ex);
						}
					}
				} else {
					obj = value[0];
				}
				for (int i = 0; i < objs.length; i++) {
					objs[i] = obj;
				}
			} else {
				for (int i = 0; i < objs.length; i++) {
					if ("".equals(value[i])) {
						objs[i] = null;
					} else if (BigDecimal.class.equals(fieldTypeClass)) {
						objs[i] = new BigDecimal(value[i]);
					} else if (Timestamp.class.equals(fieldTypeClass)) {
						try {
							Long dateTime = Long.parseLong(value[i]);
							objs[i] = new Timestamp(dateTime);
						} catch (NumberFormatException e) {
							try {
								SimpleDateFormat sdf = new SimpleDateFormat(
										"yyyy-MM-dd");
								objs[i] = new Timestamp(sdf.parse(value[i])
										.getTime());
							} catch (ParseException ex) {
								throw new RuntimeException(ex);
							}
						}
					} else {
						objs[i] = value[i];
					}
				}
			}
			return objs;
		}
	}
}
