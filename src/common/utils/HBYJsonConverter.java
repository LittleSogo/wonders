package common.utils;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import common.utils.construction.JacksonUtil;


public class HBYJsonConverter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static JSONObject convertDataSetToJson(List list) throws JSONException{
		
		JSONObject jso = new JSONObject();
		String rvl= JacksonUtil.writeListJSON(list);
		JSONArray jsarr = new JSONArray(rvl);
		jso.put("total", list.size());
		//jso.put("total", 13);
		jso.put("rows", jsarr);
		return jso;
		
	}

	public static JSONObject convertDataSetToJson2(List list,int count) throws JSONException{
		
		JSONObject jso = new JSONObject();
		String rvl= JacksonUtil.writeListJSON(list);
		JSONArray jsarr = new JSONArray(rvl);
		//jso.put("total", list.size());
		jso.put("total", count);
		jso.put("rows", jsarr);
		return jso;
		
	}
}
