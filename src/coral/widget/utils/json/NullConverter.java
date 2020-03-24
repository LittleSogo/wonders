package coral.widget.utils.json;

import org.json.JSONObject;

/**
 * null类型转换器
 * 
 * @author 龚云
 * 
 */
public class NullConverter extends JsonToFieldConverter {

	private static byte default_byte;
	private static short default_short;
	private static int default_int;
	private static long default_long;
	private static float default_float;
	private static double default_double;
	private static char default_char;
	private static boolean default_boolean;

	/**
	 * 若jsonValue为{@link JSONObject}.NULL则针对不同的fieldType取其默认值<br>
	 * 1.对于基本类型使用Java默认值<br>
	 * 2.对于Object返回null
	 * 
	 * @param jsonValue
	 *            待转换的值
	 * @param fieldType
	 *            指定转换的类型
	 * @return
	 */
	@Override
	public Object convertField(Object jsonValue, Class<?> fieldType) {
		if (jsonValue.equals(JSONObject.NULL)) {
			if (byte.class.equals(fieldType)) {
				return default_byte;
			} else if (short.class.equals(fieldType)) {
				return default_short;
			} else if (int.class.equals(fieldType)) {
				return default_int;
			} else if (long.class.equals(fieldType)) {
				return default_long;
			} else if (float.class.equals(fieldType)) {
				return default_float;
			} else if (double.class.equals(fieldType)) {
				return default_double;
			} else if (char.class.equals(fieldType)) {
				return default_char;
			} else if (boolean.class.equals(fieldType)) {
				return default_boolean;
			} else
				return null;
		}
		return next(jsonValue, fieldType);
	}
}
