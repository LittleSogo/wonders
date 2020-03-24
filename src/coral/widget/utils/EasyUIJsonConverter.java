/**
 * Project: PrototypeFrame
 * Source file: EasyUIJsonConverter.java
 * Create At 2012-7-27 下午05:27:22
 * Create By 龚云
 */
package coral.widget.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import coral.widget.data.DataSet;

/**
 * @author 龚云
 * 
 */
public class EasyUIJsonConverter {

	public static JSONObject convertDataSetToJson(DataSet dataSet) {
		try {
			JSONObject o = MetaDataJsonConverter.convertToJson(dataSet);
			JSONObject ds = o.getJSONObject("DataSet");
			JSONObject jso = new JSONObject();
			JSONArray jsarr = new JSONArray();
			jso.put("total", ds.get("totalRecordCount"));
			jso.put("rows", jsarr);
			JSONArray arr = ds.getJSONArray("rows");
			for (int i = 0; i < arr.length(); i++) {
				JSONObject row = arr.getJSONObject(i);
				jsarr.put(row.getJSONObject("row"));
			}
			return jso;
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static JSONArray convertDataSetToJsonArray(DataSet dataSet,
			boolean addEmpty) {
		try {
			JSONObject o = MetaDataJsonConverter.convertToJson(dataSet);
			JSONObject ds = o.getJSONObject("DataSet");
			JSONArray jsarr = new JSONArray();
			JSONArray arr = ds.getJSONArray("rows");
			if (addEmpty)
				jsarr.put(new JSONObject());
			for (int i = 0; i < arr.length(); i++) {
				JSONObject row = arr.getJSONObject(i);
				jsarr.put(row.getJSONObject("row"));
			}
			return jsarr;
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	public static JSONArray convertDataSetToJsonArray(DataSet dataSet) {
		return convertDataSetToJsonArray(dataSet, false);
	}

}
