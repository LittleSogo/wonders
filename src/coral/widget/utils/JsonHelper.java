package coral.widget.utils;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Json帮助类
 * 
 * @author 龚云
 * 
 */
public class JsonHelper {

	/**
	 * 同步jso中的name对应的{@link JSONArray}对象到defaultL
	 * 
	 * @param jso
	 *            同步依据jso对象
	 * @param name
	 *            同步依据的{@link JSONArray}对应的jso中的name
	 * @param defaultL
	 *            需要同步的List
	 * @throws JSONException
	 */
	public static void updateStringList(JSONObject jso, String name,
			List<String> defaultL) throws JSONException {
		if (!jso.has(name))
			return;
		defaultL.clear();
		JSONArray arr = jso.getJSONArray(name);
		for (int i = 0; i < arr.length(); i++) {
			String item = arr.getString(i);
			defaultL.add(item);
		}
	}
}
