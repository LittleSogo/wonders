package coral.widget.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.persistence.Column;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import coral.widget.utils.json.JsonToFieldConverters;

/**
 * Json到JavaBean的转换器，同步Json数据到指定的JavaBean对象
 * 
 * @author 龚云
 * 
 */
public class JsonParser {

	/**
	 * 同步模式：<br>
	 * 1.FIELD_NAME：反射fieldName模式。<br>
	 * 2.COLUMN_ANNOTATION：{@Link Column}模式
	 * 
	 * @author 龚云
	 * 
	 */
	public static enum ParseMode {
		FIELD_NAME, COLUMN_ANNOTATION
	}

	/**
	 * 默认同步模式为：{@link ParseMode}.FIELD_NAME
	 */
	public static ParseMode DEFAULT_PARSE_MODE = ParseMode.FIELD_NAME;

	/**
	 * 使用默认同步模式转换Json对象为klass的实例
	 * 
	 * @param <T>
	 *            返回klass Class<T>中指定类型的对象
	 * @param klass
	 *            Class<T>指定待转换对象的类类型，使用无参构造函数构造对象
	 * @param jsonObject
	 *            转换依据的Json对象
	 * @return
	 */
	public static <T> T parseJson(Class<T> klass, JSONObject jsonObject) {
		return parseJson(klass, jsonObject, DEFAULT_PARSE_MODE);
	}

	/**
	 * 使用parseMode的同步模式转换Json对象为klass的实例
	 * 
	 * @param <T>
	 *            返回klass Class<T>中指定类型的对象
	 * @param klass
	 *            Class<T>指定待转换对象的类类型，使用无参构造函数构造对象
	 * @param jsonObject
	 *            转换依据的Json对象
	 * @param parseMode
	 *            同步模式{@link ParseMode}
	 * @return
	 */
	public static <T> T parseJson(Class<T> klass, JSONObject jsonObject,
			ParseMode parseMode) {
		return new JsonParser().parseJsonByClass(klass, jsonObject, parseMode);
	}

	/**
	 * 使用默认同步模式同步jsonObject到对象o
	 * 
	 * @param o
	 *            待同步对象
	 * @param jsonObject
	 *            同步依据的Json对象
	 */
	public static void parseJson(Object o, JSONObject jsonObject) {
		parseJson(o, jsonObject, DEFAULT_PARSE_MODE);
	}

	/**
	 * 使用parseMode同步模式同步jsonObject到对象o
	 * 
	 * @param o
	 *            待同步对象
	 * @param jsonObject
	 *            同步依据的Json对象
	 * @param parseMode
	 *            同步模式{@link ParseMode}
	 */
	public static void parseJson(Object o, JSONObject jsonObject,
			ParseMode parseMode) {
		new JsonParser().parseJsonByObject(o, jsonObject, parseMode);
	}

	/**
	 * 用于同步的转换器收集者
	 */
	protected JsonToFieldConverters jsonToFieldConverters;

	protected JsonParser() {
		this.jsonToFieldConverters = JsonToFieldConverters.newInstance();
	}

	/**
	 * 使用自定义的转换器收集者构造转换器
	 * 
	 * @param jsonToFieldConverters
	 */
	public JsonParser(JsonToFieldConverters jsonToFieldConverters) {
		this.jsonToFieldConverters = jsonToFieldConverters;
	}

	/**
	 * 转换值对象o为指定类型targetType的值
	 * 
	 * @param o
	 *            待转换对象
	 * @param targetType
	 *            指定要转换为的类类型
	 * @return
	 */
	protected Object convertField(Object o, Class<?> targetType) {
		return this.jsonToFieldConverters.getConverter().convertField(o,
				targetType);
	}

	/**
	 * 使用默认同步模式转换jsonObject为klass所对应的对象实例
	 * 
	 * @param <T>
	 *            返回klass Class<T>中指定类型的对象
	 * @param klass
	 *            Class<T>指定待转换对象的类类型，使用无参构造函数构造对象
	 * @param jsonObject
	 *            同步依据的Json对象
	 * @return
	 */
	public <T> T parseJsonByClass(Class<T> klass, JSONObject jsonObject) {
		return parseJsonByClass(klass, jsonObject, DEFAULT_PARSE_MODE);
	}

	/**
	 * 使用parseMode同步模式转换jsonObject为klass所对应的对象实例
	 * 
	 * @param <T>
	 *            返回klass Class<T>中指定类型的对象
	 * @param klass
	 *            Class<T>指定待转换对象的类类型，使用无参构造函数构造对象
	 * @param jsonObject
	 *            同步依据的Json对象
	 * @param parseMode
	 *            同步模式{@link ParseMode}
	 * @return
	 */
	public <T> T parseJsonByClass(Class<T> klass, JSONObject jsonObject,
			ParseMode parseMode) {
		try {
			T o = klass.newInstance();
			parseJsonByObject(o, jsonObject, parseMode);
			return o;
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new RuntimeException("无法实例化待转换的类对象。", e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException("无法实例化待转换的类对象。", e);
		}
	}

	/**
	 * 使用默认同步模式同步jsonObject到o
	 * 
	 * @param <T>
	 *            返回klass Class<T>中指定类型的对象
	 * @param o
	 *            待同步对象
	 * @param jsonObject
	 *            同步依据的Json对象
	 */
	public <T> void parseJsonByObject(T o, JSONObject jsonObject) {
		parseJsonByObject(o, jsonObject, DEFAULT_PARSE_MODE);
	}

	/**
	 * 使用parseMode同步模式同步jsonObject到o
	 * 
	 * @param <T>
	 *            返回klass Class<T>中指定类型的对象
	 * @param o
	 *            待同步对象
	 * @param jsonObject
	 *            同步依据的Json对象
	 * @param parseMode
	 *            同步模式{@link ParseMode}
	 */
	public <T> void parseJsonByObject(T o, JSONObject jsonObject,
			ParseMode parseMode) {
		PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(o);
		for (PropertyDescriptor pd : pds) {
			Method writeMethod = pd.getWriteMethod();
			Class<?> pType = pd.getPropertyType();
			String fieldName = pd.getName();
			String key = fieldName;
			if (writeMethod == null) {
				try {
					pd = new PropertyDescriptor(fieldName, o.getClass());
					writeMethod = pd.getWriteMethod();
				} catch (IntrospectionException e) {
				}
			}
			if (writeMethod != null) {
				switch (parseMode) {
					case COLUMN_ANNOTATION:
						try {
							Field field = o.getClass().getDeclaredField(
									fieldName);
							Column columnAnnotation = field
									.getAnnotation(Column.class);
							String columnName = StringUtils
									.trimToEmpty(columnAnnotation.name());
							if (!columnName.isEmpty())
								key = columnName;
						} catch (SecurityException e1) {
							e1.printStackTrace();
						} catch (NoSuchFieldException e1) {
							e1.printStackTrace();
						}
						break;
				}
				if (jsonObject.has(key)) {
					Object value = null;
					try {
						value = jsonObject.get(key);
						Object result = convertField(value, pType);
						if (writeMethod == null) {
							Field f = o.getClass().getDeclaredField(fieldName);
							f.setAccessible(true);
							f.set(o, result);
						} else {
							writeMethod.setAccessible(true);
							writeMethod.invoke(o, new Object[]{result});
						}
					} catch (Exception e) {
						throw new RuntimeException("无法转换json值：" + value
						                           + "为对应的field：" + key + "（" + pType + "）。", e);
					}
				}
			}
		}
	}
}
