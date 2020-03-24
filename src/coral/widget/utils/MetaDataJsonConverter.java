package coral.widget.utils;

import java.lang.reflect.Array;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import coral.base.util.LinkedJSONObject;

import wfc.service.util.OrderedHashSet;

/**
 * 
 * Json对象转换器
 * 
 * @author 龚云
 * 
 */
public class MetaDataJsonConverter extends MetaDataConvert<JSONObject> {

	protected MetaDataJsonConverter() {
	}

	/**
	 * 转换对象为{@link JSONObject}
	 * 
	 * @param o
	 *            待转换的对象
	 * @return
	 */
	public JSONObject _convertToJson(Object o) {
		JSONObject jsonObject = new JSONObject();
		convertObject(o, o.getClass().getSimpleName(), jsonObject, null);
		return jsonObject;
	}

	/**
	 * 添加一个节点
	 * 
	 * @param from
	 *            父节点
	 * @param name
	 *            节点名称
	 * @param type
	 *            节点类型
	 * @param value
	 *            节点值
	 * @param originalObj
	 *            节点值的来源（即value的来源对象）
	 * @return 若type为空则返回新建的{@link JSONObject}
	 *         节点，否则返回父节点，也即是说对于无类型的value不在多建立一层json结构
	 */
	@Override
	protected JSONObject add(JSONObject from, String name, String type,
			String value, Object originalObj) {
		Object val = value;
		if (originalObj instanceof Boolean) {
			val = originalObj;
		} else if (originalObj instanceof Integer) {
			val = originalObj;
		} else if (originalObj instanceof JSONObject) {
			val = originalObj;
		} else if (originalObj instanceof JSONArray) {
			val = originalObj;
		}
		JSONObject jso = new LinkedJSONObject();
		boolean nullType = StringUtils.trimToEmpty(type).isEmpty();
		try {
			if (from.has(name)) {
				Object old = from.get(name);
				if (old instanceof JSONArray) {
					((JSONArray) old).put(nullType ? val : jso);
				} else {
					from.remove(name);
					JSONArray jsonArray = new JSONArray();
					jsonArray.put(old);
					jsonArray.put(nullType ? val : jso);
					from.put(name, jsonArray);
				}
			} else
				from.put(name, nullType ? val : jso);
			if (!nullType) {
				// jso.put("type", type);
				jso.put("value", val);
				return jso;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return from;
	}

	/**
	 * @see coral.widget.utils.MetaDataConvert#convertObject(java.lang.Object,
	 *      java.lang.String, java.lang.Object, java.lang.String)
	 */
	@Override
	protected void convertObject(Object o, String name, JSONObject root,
			String type) {
		if (o != null && (o instanceof JSONObject || o instanceof JSONArray)) {
			dealJSON(o, name, root, type);
		} else {
			super.convertObject(o, name, root, type);
		}
	}

	@Override
	protected void dealArray(Object o, String name, JSONObject root, String type) {
		try {
			root.put(name, new JSONArray());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < Array.getLength(o); i++) {
			Object item = Array.get(o, i);
			convertObject(item, name, root, null);
		}
	}

	/**
	 * {@link Collection}的转换方式
	 * 
	 * @param o
	 *            待转换的对象
	 * @param name
	 *            对象的名称，用作节点名称
	 * @param root
	 *            父节点
	 * @param type
	 *            对象类型，用作节点类型
	 */
	@Override
	protected void dealCollection(Object o, String name, JSONObject root,
			String type) {
		Collection<?> cO = (Collection<?>) o;
		try {
			root.put(name, new JSONArray());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (o instanceof OrderedHashSet<?>) {
			OrderedHashSet<?> oS = (OrderedHashSet<?>) o;
			for (int i = 0; i < oS.size(); i++) {
				convertObject(oS.get(i), name, root, null);
			}
		} else {
			for (Object value : cO) {
				convertObject(value, name, root, null);
			}
		}
	}

	protected void dealJSON(Object o, String name, JSONObject root, String type) {
		add(root, name, null, o.toString(), o);
	}

	/**
	 * 转换对象为{@link JSONObject}
	 * 
	 * @param o
	 *            待转换的对象
	 * @return
	 */
	public static JSONObject convertToJson(Object o) {
		return new MetaDataJsonConverter()._convertToJson(o);
	}
}
