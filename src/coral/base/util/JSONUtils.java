/**
 * Project: Coral
 * Source file: JSONUtils.java
 * Create At 2014-4-10 上午11:01:53
 * Create By 龚云
 */
package coral.base.util;

import org.json.JSONObject;

/**
 * @author 龚云
 * 
 */
public class JSONUtils {

	public static String optString(JSONObject jso, String key,
			String defaultValue) {
		String v = jso.optString(key, "");
		if (v.isEmpty())
			return defaultValue;
		return v;
	}

}
