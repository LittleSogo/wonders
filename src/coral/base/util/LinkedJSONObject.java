/**
 * Project: Coral
 * Source file: LinkedJSONObject.java
 * Create At 2013-12-30 下午03:06:30
 * Create By 龚云
 */
package coral.base.util;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

/**
 * @author 龚云
 */
public class LinkedJSONObject extends JSONObject {

	public LinkedJSONObject() {
		super();
		forceToLinkedHashMap(null);
	}

	public LinkedJSONObject(Map map) {
		super();
		forceToLinkedHashMap(map);
	}

	@SuppressWarnings("unchecked")
	private void forceToLinkedHashMap(Map map) {
		try {
			Field[] fields = JSONObject.class.getDeclaredFields();
			for (Field field : fields) {
				Class<?> type = field.getType();
				if (Map.class.isAssignableFrom(type)) {
					field.setAccessible(true);
					LinkedHashMap m = new LinkedHashMap();
					if (map != null)
						m.putAll(map);
					field.set(this, m);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
