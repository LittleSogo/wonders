/**
 * Project: Coral
 * Source file: EnumUtils.java
 * Create At 2014-2-14 上午09:56:41
 * Create By 龚云
 */
package coral.base.util;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

/**
 * @author 龚云
 * 
 */
public class EnumUtils {

	@SuppressWarnings("unchecked")
	public static <T, K> Map<T, K> toEnumMap(Class<K> klass, String fieldName) {
		try {
			Map<T, K> map = new LinkedHashMap<T, K>();
			Field fld = klass.getDeclaredField(fieldName);
			fld.setAccessible(true);
			Field[] items = klass.getFields();
			for (Field item : items) {
				if (klass.isAssignableFrom(item.getType())) {
					K val = (K) item.get(klass);
					T key = (T) fld.get(val);
					map.put(key, val);
				}
			}
			return map;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> JSONObject toEnumJson(Class<T> klass, String keyField,
			String valField) {
		try {
			JSONObject jso = new JSONObject();
			JSONObject kmJso = new LinkedJSONObject();
			jso.put("KM", kmJso);
			JSONObject emJso = new LinkedJSONObject();
			jso.put("EM", emJso);
			Field kFld = klass.getDeclaredField(keyField);
			kFld.setAccessible(true);
			Field vFld = klass.getDeclaredField(valField);
			vFld.setAccessible(true);
			Field[] items = klass.getFields();
			for (Field item : items) {
				if (klass.isAssignableFrom(item.getType())) {
					Object obj = item.get(klass);
					Object key = kFld.get(obj);
					if (key == null)
						throw new NullPointerException();
					Object val = vFld.get(obj);
					kmJso.put(String.valueOf(key), val);
					emJso.put(item.getName(), String.valueOf(key));
				}
			}
			return jso;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
